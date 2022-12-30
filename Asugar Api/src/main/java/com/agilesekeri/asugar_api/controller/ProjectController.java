package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.service.AppUserService;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.service.ProjectService;
import com.agilesekeri.asugar_api.service.SprintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(path = "{projectId}")
public class ProjectController {
    private final ProjectService projectService;

    private final AppUserService appUserService;

    private final SprintService sprintService;

    @GetMapping(path = "/members")
    public void getMembers(@PathVariable Long projectId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ProjectEntity project = projectService.getProject(projectId);
        Set<AppUserEntity> members = project.getMembers();
        String username = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(username);

        if(members.contains(issuer)) {
            List<Map<String, String>> list = new ArrayList<>();
            for(AppUserEntity user : members) {
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("first_name", user.getFirstName());
                userInfo.put("last_name", user.getLastName());
                userInfo.put("email", user.getUsername());
                list.add(userInfo);

                if(project.getAdmin() == user)
                    userInfo.put("title", "Admin");
                else if(project.getProductOwner() == user)
                    userInfo.put("title", "Product Owner");
                else
                    userInfo.put("title", "Member");
            }

            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), list);
        }
        else
            throw new IllegalCallerException("The request came from user not a member of the project");
    }

    @PutMapping(path = "/members")
    public boolean addMember(@PathVariable Long projectId, @RequestParam String username, HttpServletRequest request) throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUserEntity user = appUserService.loadUserByUsername(username);
        return projectService.addMember(projectId, user);
    }

    @DeleteMapping(path = "/members")
    public boolean removeMember(@PathVariable Long projectId, @RequestParam String username, HttpServletRequest request) throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUserEntity user = appUserService.loadUserByUsername(username);
        return projectService.removeMember(projectId, user);
    }

    @PutMapping(path = "/product_owner")
    public void setProductOwner(@PathVariable Long projectId, @RequestParam String username, HttpServletRequest request) throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUserEntity user = appUserService.loadUserByUsername(username);

        projectService.setProductOwner(projectId, user);
    }

    @GetMapping(path = "/backlog")
    public void getBacklog(@PathVariable Long projectId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getMembers().contains(issuer)) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(response.getOutputStream(), projectService.getAllIssues(project));
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

        if(project.getMembers().contains(issuer))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        sprintService.finishActiveSprint(project);
    }

    @PostMapping(path = "/epics")
    public void createEpic(@PathVariable Long projectId,
                           @RequestParam String epicName,
                           HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getMembers().contains(issuer))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        projectService.createEpic(project, epicName, issuer);
    }

    @GetMapping(path = "/issues/active")
    public void getIssuesToDo(@PathVariable Long projectId,
                              HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getMembers().contains(issuer)) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(response.getOutputStream(), projectService.getIssuesToDo(project));
        }
        else {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            new ObjectMapper().writeValue(response.getOutputStream(), "The request came from user not a member of the project");
        }
    }
}
