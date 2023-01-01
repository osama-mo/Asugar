package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.dto.IssueDTO;
import com.agilesekeri.asugar_api.model.dto.SubtaskDTO;
import com.agilesekeri.asugar_api.model.entity.*;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.model.request.IssueCreateRequest;
import com.agilesekeri.asugar_api.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

@Service
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;

//    private final Clock clock;

//    private final SprintService sprintService;
//    private final EpicService epicService;

    public IssueEntity createIssue(IssueCreateRequest issueCreateRequest, AppUserEntity creator, ProjectEntity project) {
        IssueEntity issueEntity = IssueEntity.builder()
                .project(project)
                .title(issueCreateRequest.getTitle())
                .description(issueCreateRequest.getDescription())
                .creator(creator)
                .createdAt(LocalDateTime.now())
                .issueType(issueCreateRequest.getIssueType())
                .condition(TaskConditionEnum.TODO)
                .build();

        return issueRepository.save(issueEntity);
    }

    public Set<Map<String, String>> getSubTaskInfo(AbstractIssue issue) {
        if(!issue.getClass().equals(IssueEntity.class))
            throw new IllegalArgumentException("The abstract issue with the id " +
                    issue.getId() + " is not an issue entity");

        Set<Map<String, String>> result = new HashSet<>();
        for(SubtaskEntity subtask : ((IssueEntity) issue).getSubtasks()) {
            Map<String, String> info = new HashMap<>();
            info.put("id", issue.getId().toString());
            info.put("title", issue.getTitle());
            result.add(info);
        }
        return result;
    }

    public Map<String, String> getParentIssueInfo(AbstractIssue issue) {
        if(!issue.getClass().equals(SubtaskEntity.class))
            throw new IllegalArgumentException("The abstract issue with the id " +
                    issue.getId() + " is not a subtask entity");

        Map<String, String> info = new HashMap<>();
        info.put("id", issue.getId().toString());
        info.put("title", issue.getTitle());
        return info;
    }

    public AbstractIssue getIssue(Long issueId) {
        return issueRepository.findById(issueId)
                .orElseThrow(() ->
                        new IllegalStateException("No issue with id " + issueId + " was found to retrieve info"));
    }

    public void deleteIssue(Long issueId) {
        AbstractIssue issue = getIssue(issueId);

        if(IssueEntity.class.equals(issue.getClass()))
            ((IssueEntity) issue).getSubtasks().forEach(
                    subtask -> subtask.setParentIssue(null)
            );

        issueRepository.deleteById(issueId);
    }

//    @Transactional
//    public void updateCondition(Long issueId, TaskConditionEnum taskConditionEnum){
//        IssueEntity issue = issueRepository.findById(issueId)
//                .orElseThrow( () -> new IllegalStateException("No issue with id " + issueId + " was found to update condition"));
//        issue.setCondition(taskConditionEnum);
//        issueRepository.save(issue);
//    }
//
//    @Transactional
//    public void updatePlannedTo(Long issueId, LocalDateTime localDateTime){
//        IssueEntity issue = issueRepository.findById(issueId)
//                .orElseThrow(() -> new IllegalStateException("No issue with id " + issueId + " was found to update plannedTo"));
//        issue.setPlannedTo(localDateTime);
//        issueRepository.save(issue);
//    }
//
//    @Transactional
//    public void assignToUser(Long issueId, String username){
//        AppUser user = appUserService.loadUserByUsername(username); // TODO: Validate user existence.
//        IssueEntity issue = issueRepository.findById(issueId)
//                .orElseThrow(() -> new IllegalStateException("No issue with id " + issueId + " was found to assign a user"));
//        issue.setAssigned(user);
//        issueRepository.save(issue);
//    }

//    @Transactional
//    public void updateIssue(Long issueId, IssueGenericUpdateRequest issueGenericUpdateRequest){
//        AbstractIssue issue = issueRepository.findById(issueId)
//                .orElseThrow(() -> new IllegalStateException("No issue found with id " + issueId + " to update"));
//
//        // TODO: Handle if epic/sprint/project with the given ID's doesn't exist throw exception.
//        // TODO: Handle if given condition is not an option of enum types.
//        // TODO: Handle if given issueType is not an option of enum types.
//        // TODO: Handle if assigned/creator user does exist or not.
//        // TODO: Handle if endedAt/plannedTo comes in LocalDateTime.ISO pattern or not.
//
//        if(issueGenericUpdateRequest.getIssueType() != null)
//            issue.setIssueType(issueGenericUpdateRequest.getIssueType());
//        if(issueGenericUpdateRequest.getCondition() != null)
//            issue.setCondition(issueGenericUpdateRequest.getCondition());
//        if(issueGenericUpdateRequest.getAssigned() != null)
//            issue.setAssigned(issueGenericUpdateRequest.getAssigned());
//        if(issueGenericUpdateRequest.getCreator() != null)
//            issue.setCreator(issueGenericUpdateRequest.getCreator());
//        if(issueGenericUpdateRequest.getEndedAt() != null)
//            issue.setEndedAt(issueGenericUpdateRequest.getEndedAt());
//        if(issueGenericUpdateRequest.getManHour() != null)
//            issue.setManHour(issueGenericUpdateRequest.getManHour());
//        if(issueGenericUpdateRequest.getSubtasks() != null)
//            issue.setSubtasks(issueGenericUpdateRequest.getSubtasks());
//        if(issueGenericUpdateRequest.getPlannedTo() != null)
//            issue.setPlannedTo(issueGenericUpdateRequest.getPlannedTo());
//        if(issueGenericUpdateRequest.getDescription() != null)
//            issue.setDescription(issueGenericUpdateRequest.getDescription());
//        if(issueGenericUpdateRequest.getEpic() != null)
//            issue.setEpic(issueGenericUpdateRequest.getEpic());
//        if(issueGenericUpdateRequest.getProject() != null)
//            issue.setProject(issueGenericUpdateRequest.getProject());
//        if(issueGenericUpdateRequest.getSprint() != null)
//            issue.setSprint(issueGenericUpdateRequest.getSprint()); // gotta handle here and request
//        if(issueGenericUpdateRequest.getTitle() != null)
//            issue.setTitle(issueGenericUpdateRequest.getTitle());
//
//        issueRepository.save(issue);
//    }
}
