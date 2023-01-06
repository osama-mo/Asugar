package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.EpicEntity;
import com.agilesekeri.asugar_api.model.entity.IssueEntity;
import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.model.request.EpicCreateRequest;
import com.agilesekeri.asugar_api.model.request.IssueCreateRequest;
import com.agilesekeri.asugar_api.service.*;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(path = "{projectId}")
public class ProjectController {
    private final ProjectService projectService;

    private final AppUserService appUserService;

    @GetMapping(path = "/members")
    public void getMembers(@PathVariable Long projectId,
                           HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), projectService.getMembersInfo(projectId));
        }
        else {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            new ObjectMapper().writeValue(response.getOutputStream(), "The issue is not a member of the project team");
        }
    }

    @PutMapping(path = "/members")
    public void addMember(@PathVariable Long projectId,
                          @RequestParam String username,
                          HttpServletRequest request,
                          HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            if(!projectService.checkAccess(projectId, issuerUsername, Role.ADMIN))
                throw new IllegalCallerException("The issuer is not the admin of the project");

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(
                    response.getOutputStream(),
                    new HashMap<String, String>()
                            .put("completed", projectService.addMember(projectId, username).toString())
            );
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
        catch (IllegalCallerException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }

    @DeleteMapping(path = "/members")
    public void removeMember(@PathVariable Long projectId,
                                @RequestParam String username,
                                HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            if(!projectService.checkAccess(projectId, issuerUsername, Role.ADMIN))
                throw new IllegalCallerException("The issuer is not the admin of the project");

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(
                    response.getOutputStream(),
                    new HashMap<String, String>()
                            .put("completed", projectService.removeMember(projectId, username).toString())
            );
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
        catch (IllegalCallerException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }

    @PutMapping(path = "/product_owner")
    public void setProductOwner(@PathVariable Long projectId,
                                @RequestParam String username,
                                HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.ADMIN)) {
            AppUserEntity user = appUserService.loadUserByUsername(username);
            projectService.setProductOwner(projectId, user);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), "The issuer is not the admin of the project");
        }
    }

    @GetMapping(path = "/issues/active")
    public void getActiveIssues(@PathVariable Long projectId,
                                HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
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

    @GetMapping(path = "/issues/all")
    public void getBacklog(@PathVariable Long projectId,
                           HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(response.getOutputStream(), projectService.getAllIssues(projectId));
        }
        else {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            new ObjectMapper().writeValue(response.getOutputStream(), "The issuer is not a member of the project team");
        }
    }

    @PostMapping(path = "/issues/create")
    public void createIssue(@PathVariable Long projectId,
                            @RequestBody IssueCreateRequest createRequest,
                            HttpServletRequest request,
                            HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
            projectService.createIssue(projectId, issuerUsername, createRequest);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), "The issuer is not a member of the project team");
        }
    }

    @PostMapping(path = "/epics/create")
    public void createEpic(@PathVariable Long projectId,
                           @RequestBody EpicCreateRequest createRequest,
                           HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
            projectService.createEpic(projectId, issuerUsername, createRequest);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), "The issuer is not a member of the project team");
        }
    }

    @GetMapping(path = "/epics")
    public void getEpics(@PathVariable Long projectId,
                         HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(
                    response.getOutputStream(),
                    projectService.getEpics(projectId)
            );
        }
        else {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            new ObjectMapper().writeValue(
                    response.getOutputStream(), "The issuer is not a member of the project team");
        }
    }
}
