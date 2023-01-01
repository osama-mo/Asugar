package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.service.*;
import com.agilesekeri.asugar_api.model.request.IssueCreateRequest;
import com.agilesekeri.asugar_api.model.entity.IssueEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/{projectId}/issues")
@RequiredArgsConstructor
@Getter
@Setter
public class IssueController {
    private final AppUserService appUserService;
    private final IssueService issueService;
    private final ProjectService projectService;
    private final SprintService sprintService;
    private final EpicService epicService;

    @PostMapping(path = "/create")
    public void createIssue(@PathVariable Long projectId,
                            @RequestBody IssueCreateRequest createRequest,
                            HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);
        IssueEntity issue = issueService.createIssue(createRequest, issuer, project);

        if(createRequest.getSprint() != null)
            sprintService.addIssue(
                    projectService.getSprint(projectId, createRequest.getSprint()).getId(),
                    issue.getId()
            );

        if(createRequest.getEpicId() != null)
            epicService.addIssue(
                    createRequest.getEpicId(),
                    issue.getId()
            );

        if(createRequest.getAssignedTo() != null)
            issueService.assignToMember(
                    issue.getId(),
                    appUserService.loadUserByUsername(createRequest.getAssignedTo())
            );
    }

    @PutMapping("/{issueId}/condition")
    public void setCondition(@PathVariable Long projectId,
                             @PathVariable Long issueId,
                             @RequestParam TaskConditionEnum condition,
                             HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        issueService.setCondition(issueId, condition);
    }

    @PostMapping("/delete")
    public void deleteIssue(@PathVariable Long projectId,
                            @RequestParam Long issueId,
                            HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        issueService.deleteIssue(issueId);
    }

    @GetMapping("/{issueId}/info")
    public AbstractIssueDTO getIssue(@PathVariable Long issueId,
                                     @PathVariable Long projectId,
                                     HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        return projectService.getIssueInfo(issueId);
    }

    @PutMapping("/{issueId}/assign")
    public void assignToMember(@PathVariable Long projectId,
                               @PathVariable Long issueId,
                               @RequestParam String username,
                               HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        issueService.assignToMember(issueId, appUserService.loadUserByUsername(username));
    }

    // I may need user to login aswell
//    @GetMapping("/info/{issueId}")
//    public AbstractIssueDTO viewInfo(@PathVariable Long issueId) {
//        return issueService.getIssueInfo(issueId);
//    }

//    @PatchMapping("/update-condition/{issueId}/{conditionType}")
//    public void updateCondition(@PathVariable Long issueId, @PathVariable("conditionType") TaskConditionEnum taskConditionEnum) {
//        issueService.updateCondition(issueId, taskConditionEnum);
//    }

//    @PutMapping("/update-plannedTo/{issueId}/{localDateTime}")
//    public void updatePlannedTo(@PathVariable Long issueId, @PathVariable("localDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime) {
//        issueService.updatePlannedTo(issueId, localDateTime);
//    }

//    @PutMapping("/assignTo/{issueId}")
//    public void assignToUser(@PathVariable("issueId") Long issueId, @RequestParam("username") String username){
//        issueService.assignToUser(issueId, username);
//    }

    // TODO: Below I'll be combining all of the set/edit methods which some are written above.
//    @PatchMapping("/update/{issueId}")
//    public void updateIssue(@PathVariable("issueId") Long issueId, @RequestBody IssueGenericUpdateRequest issueGenericUpdateRequest){
//        issueService.updateIssue(issueId, issueGenericUpdateRequest);
//    }
}
