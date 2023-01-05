package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.service.*;
import com.agilesekeri.asugar_api.model.request.IssueCreateRequest;
import com.agilesekeri.asugar_api.model.entity.IssueEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

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
                             HttpServletRequest request,
                             HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
                throw new IllegalCallerException("The issuer is not a member of the project team");

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            issueService.setCondition(issueId, condition);

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        } catch (IllegalCallerException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public void deleteIssue(@PathVariable Long projectId,
                            @PathVariable Long issueId,
                            HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
                throw new IllegalCallerException("The issuer is not a member of the project team");

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            issueService.deleteIssue(issueId);

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        } catch (IllegalCallerException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }

    @GetMapping("/info")
    public void getIssue(@PathVariable Long issueId,
                                     @PathVariable Long projectId,
                                     HttpServletRequest request,
                                     HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
                throw new IllegalCallerException("The issuer is not a member of the project team");

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), issueService.getIssueInfo(issueId));

        } catch (IllegalArgumentException|IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        } catch (IllegalCallerException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }

    @PutMapping("/assign/member")
    public void assignToMember(@PathVariable Long projectId,
                               @PathVariable Long issueId,
                               @RequestParam String username,
                               HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
                throw new IllegalCallerException("The issuer is not a member of the project team");

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            issueService.assignToMember(issueId, appUserService.loadUserByUsername(username));

        } catch (UsernameNotFoundException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        } catch (IllegalCallerException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }

    @PutMapping("/assign/sprint")
    public void assignToSprint(@PathVariable Long projectId,
                               @PathVariable Long issueId,
                               @RequestParam String sprint,
                               HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
                throw new IllegalCallerException("The issuer is not a member of the project team");

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            issueService.assignToSprint(issueId, projectService.getSprint(projectId, sprint));

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        } catch (IllegalCallerException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }

    @PutMapping("/assign/epic")
    public void assignToEpic(@PathVariable Long projectId,
                             @PathVariable Long issueId,
                             @RequestParam String epicId,
                             HttpServletRequest request,
                             HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
                throw new IllegalCallerException("The issuer is not a member of the project team");

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            issueService.assignToEpic(issueId, epicService.getEpic(Long.parseLong(epicId)));

        } catch (NumberFormatException e) {
            if("null".equals(epicId))
                issueService.assignToEpic(issueId, null);

            else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), "Invalid epic id: " + epicId);
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        } catch (IllegalCallerException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }
}
