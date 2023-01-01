package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.model.request.EpicCreateRequest;
import com.agilesekeri.asugar_api.service.*;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(path = "{projectId}")
public class ProjectController {
    private final ProjectService projectService;

    private final AppUserService appUserService;

    private final IssueService issueService;

    private final EpicService epicService;

    private final SprintService sprintService;

    @GetMapping(path = "/members")
    public void getMembers(@PathVariable Long projectId,
                           HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {
        ProjectEntity project = projectService.getProject(projectId);
        String username = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(username);

        if(project.getMembers().contains(issuer)) {
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), projectService.getMembersInfo(project.getId()));
        }
        else {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            new ObjectMapper().writeValue(response.getOutputStream(), "The request came from user not a member of the project");
        }
    }

    @PutMapping(path = "/members")
    public boolean addMember(@PathVariable Long projectId,
                             @RequestParam String username,
                             HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

//        AppUserEntity user = appUserService.loadUserByUsername(username);
        return projectService.addMember(projectId, username);
    }

    @DeleteMapping(path = "/members")
    public boolean removeMember(@PathVariable Long projectId,
                                @RequestParam String username,
                                HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

//        AppUserEntity user = appUserService.loadUserByUsername(username);
        return projectService.removeMember(projectId, username);
    }

    @PutMapping(path = "/product_owner")
    public void setProductOwner(@PathVariable Long projectId,
                                @RequestParam String username,
                                HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUserEntity user = appUserService.loadUserByUsername(username);

        projectService.setProductOwner(projectId, user);
    }

    @GetMapping(path = "/issues/all")
    public void getBacklog(@PathVariable Long projectId,
                           HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getMembers().contains(issuer)) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(response.getOutputStream(), projectService.getAllIssues(projectId));
        }
        else {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            new ObjectMapper().writeValue(response.getOutputStream(), "The request came from user not a member of the project");
        }
    }

    @PutMapping(path = "/sprints/finish")
    public void finishActiveSprint(@PathVariable Long projectId,
                                   HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(!project.getMembers().contains(issuer))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        projectService.finishActiveSprint(projectId);
    }

    @PostMapping(path = "/epics/create")
    public void createEpic(@PathVariable Long projectId,
                           @RequestBody EpicCreateRequest createRequest,
                           HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.MEMBER))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);
        epicService.createEpic(createRequest, issuer, project);
    }

    @GetMapping(path = "/issues/active")
    public void getActiveIssues(@PathVariable Long projectId,
                                HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getMembers().contains(issuer)) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(response.getOutputStream(), projectService.getIssuesToDo(projectId));
        }
        else {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            new ObjectMapper().writeValue(response.getOutputStream(), "The request came from user not a member of the project");
        }
    }
}
