package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.dto.IssueDTO;
import com.agilesekeri.asugar_api.model.dto.SubtaskDTO;
import com.agilesekeri.asugar_api.model.entity.*;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.model.request.IssueCreateRequest;
import com.agilesekeri.asugar_api.repository.IssueRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class IssueService {
    private final IssueRepository issueRepository;

    public IssueEntity createIssue(IssueCreateRequest issueCreateRequest, AppUserEntity creator, ProjectEntity project) {
        IssueEntity issueEntity = IssueEntity.builder()
                .project(project)
                .title(issueCreateRequest.getTitle())
                .description(issueCreateRequest.getDescription())
                .creator(creator)
                .createdAt(LocalDateTime.now())
                .issueType(issueCreateRequest.getIssueType())
                .condition(TaskConditionEnum.TODO)
                .manHour(issueCreateRequest.getManHour())
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
                        new IllegalArgumentException("No issue with id " + issueId + " was found"));
    }

    public void deleteIssue(Long issueId) {
//        AbstractIssue issue = getIssue(issueId);

//        if(IssueEntity.class.equals(issue.getClass()))
//            ((IssueEntity) issue).getSubtasks().forEach(
//                    subtask -> subtask.setParentIssue(null)
//            );

        issueRepository.deleteById(issueId);
    }

    public void setCondition(Long issueId, TaskConditionEnum condition) {
        AbstractIssue issue = getIssue(issueId);
        issue.setCondition(condition);
        issueRepository.save(issue);
    }

    public void assignToMember(Long issueId, AppUserEntity user) {
        AbstractIssue issue = getIssue(issueId);
        issue.setAssigned(user);
        issueRepository.save(issue);
    }

    public void assignToSprint(Long issueId, SprintEntity sprint) {
        AbstractIssue issue = getIssue(issueId);
        issue.setSprint(sprint);
        issueRepository.save(issue);
    }

    public void assignToEpic(Long issueId, EpicEntity epic) {
        AbstractIssue issue = getIssue(issueId);
        issue.setEpic(epic);
        issueRepository.save(issue);
    }

    public AbstractIssueDTO getIssueInfo(Long issueId) {
        AbstractIssue issue = getIssue(issueId);
        AbstractIssueDTO dto;

        if(IssueEntity.class.equals(issue.getClass()))
            dto = IssueDTO.builder().subtasks(getSubTaskInfo(issue)).build();
        else if(SubtaskEntity.class.equals(issue.getClass()))
            dto = SubtaskDTO.builder().parentIssue(getParentIssueInfo(issue)).build();
        else
            throw new IllegalStateException("Unknown issue type");

        dto.setId(issue.getId());
        dto.setProjectId(issue.getProject().getId());
        dto.setIssueType(issue.getIssueType().name());
        dto.setCondition(issue.getCondition().name());
        dto.setManHour(issue.getManHour());
        dto.setDescription(issue.getDescription());
        dto.setCreatorUsername(issue.getCreator().getUsername());
        dto.setCreatedAt(issue.getCreatedAt().toString());
        dto.setTitle(issue.getTitle());

        if(issue.getAssigned() != null)
            dto.setAssignedTo(issue.getAssigned().getUsername());

        if(issue.getEpic() != null)
            dto.setEpicId(issue.getEpic().getId());

        if(issue.getSprint() != null)
            dto.setSprint(issue.getSprint().getId().toString());

        return dto;
    }
}
