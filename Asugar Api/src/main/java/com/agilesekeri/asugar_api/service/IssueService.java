package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.service.AppUserService;
import com.agilesekeri.asugar_api.repository.IssueRepository;
import com.agilesekeri.asugar_api.model.request.IssueCreateRequest;
import com.agilesekeri.asugar_api.model.request.IssueGenericUpdateRequest;
import com.agilesekeri.asugar_api.model.entity.IssueEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class IssueService {
    // TODO: project id belirtildiginde bir issue icin, o projenin varligini check et.
    private final IssueRepository issueRepository;
    private final AppUserService appUserService;

    public void createIssue(IssueCreateRequest issueCreateRequest){
        // TODO: Change below code to work with JWT, request sender will be used as creator.
        //AppUserEntity creator = appUserService.loadUserByUsername(issueCreateRequest.getUsername());
//        IssueEntity issueEntity = IssueEntity.builder()
//                .projectId(issueCreateRequest.getProjectId())
//                .title(issueCreateRequest.getTitle())
//                .creator(creator)
//                .epicId(issueCreateRequest.getEpicId())
//                .sprintId(issueCreateRequest.getSprintId())
//                .issueType(issueCreateRequest.getIssueType())
//                .build();

        IssueEntity issueEntity = new IssueEntity();
        issueEntity.setTitle(issueCreateRequest.getTitle());
        issueEntity.setProject(issueCreateRequest.getProject());
        issueEntity.setEpic(issueCreateRequest.getEpic());
        issueEntity.setSprint(issueCreateRequest.getSprint());
        issueEntity.setIssueType(issueCreateRequest.getIssueType());


        issueRepository.save(issueEntity);
    }

    // TODO: This should not reach to description. Instead, it will return a body of data.
    public String viewInfo(Long issueId){
        if(!issueRepository.existsById(issueId)){
            throw new IllegalStateException("No issue with id " + issueId + " was found to retrieve info");
        }
        return issueRepository.findById(issueId).get().getDescription();
    }

    public IssueEntity getIssue(Long issueId){
        return issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalStateException("No issue with id " + issueId + " was found to retrieve info"));
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

    @Transactional
    public void updateIssue(Long issueId, IssueGenericUpdateRequest issueGenericUpdateRequest){
        IssueEntity issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalStateException("No issue found with id " + issueId + " to update"));

        // TODO: Handle if epic/sprint/project with the given ID's doesn't exist throw exception.
        // TODO: Handle if given condition is not an option of enum types.
        // TODO: Handle if given issueType is not an option of enum types.
        // TODO: Handle if assigned/creator user does exist or not.
        // TODO: Handle if endedAt/plannedTo comes in LocalDateTime.ISO pattern or not.

        if(issueGenericUpdateRequest.getIssueType() != null)
            issue.setIssueType(issueGenericUpdateRequest.getIssueType());
        if(issueGenericUpdateRequest.getCondition() != null)
            issue.setCondition(issueGenericUpdateRequest.getCondition());
        if(issueGenericUpdateRequest.getAssigned() != null)
            issue.setAssigned(issueGenericUpdateRequest.getAssigned());
        if(issueGenericUpdateRequest.getCreator() != null)
            issue.setCreator(issueGenericUpdateRequest.getCreator());
        if(issueGenericUpdateRequest.getEndedAt() != null)
            issue.setEndedAt(issueGenericUpdateRequest.getEndedAt());
        if(issueGenericUpdateRequest.getManHour() != null)
            issue.setManHour(issueGenericUpdateRequest.getManHour());
        if(issueGenericUpdateRequest.getSubtasks() != null)
            issue.setSubtasks(issueGenericUpdateRequest.getSubtasks());
        if(issueGenericUpdateRequest.getPlannedTo() != null)
            issue.setPlannedTo(issueGenericUpdateRequest.getPlannedTo());
        if(issueGenericUpdateRequest.getDescription() != null)
            issue.setDescription(issueGenericUpdateRequest.getDescription());
        if(issueGenericUpdateRequest.getEpic() != null)
            issue.setEpic(issueGenericUpdateRequest.getEpic());
        if(issueGenericUpdateRequest.getProject() != null)
            issue.setProject(issueGenericUpdateRequest.getProject());
        if(issueGenericUpdateRequest.getSprint() != null)
            issue.setSprint(issueGenericUpdateRequest.getSprint()); // gotta handle here and request
        if(issueGenericUpdateRequest.getTitle() != null)
            issue.setTitle(issueGenericUpdateRequest.getTitle());

        issueRepository.save(issue);
    }

}
