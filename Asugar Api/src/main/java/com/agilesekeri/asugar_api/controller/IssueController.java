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
@RequestMapping("/{projectId}/issues/{issueId}")
@RequiredArgsConstructor
@Getter
@Setter
public class IssueController {
    private final AppUserService appUserService;
    private final IssueService issueService;
    private final ProjectService projectService;
    private final SprintService sprintService;
    private final EpicService epicService;

    @PutMapping("/condition")
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

    @DeleteMapping("/delete")
    public void deleteIssue(@PathVariable Long projectId,
                            @PathVariable Long issueId,
                            HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        issueService.deleteIssue(issueId);
    }

    @GetMapping("/info")
    public AbstractIssueDTO getIssue(@PathVariable Long issueId,
                                     @PathVariable Long projectId,
                                     HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        return issueService.getIssueInfo(issueId);
    }

    @PutMapping("/assign/member")
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

    @PutMapping("/assign/sprint")
    public void assignToSprint(@PathVariable Long projectId,
                               @PathVariable Long issueId,
                               @RequestParam String sprint,
                               HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        issueService.assignToSprint(issueId, projectService.getSprint(projectId, sprint));
    }

    @PutMapping("/assign/epic")
    public void assignToEpic(@PathVariable Long projectId,
                             @PathVariable Long issueId,
                             @RequestParam Long epicId,
                             HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        if(epicId != null)
            issueService.assignToEpic(issueId, epicService.getEpic(epicId));

        else
            issueService.assignToEpic(issueId, null);
    }
}
