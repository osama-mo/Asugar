package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.model.request.EpicCreateRequest;
import com.agilesekeri.asugar_api.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/{projectId}/epics/{epicId}")
@RequiredArgsConstructor
@Getter
@Setter
public class EpicController {
    private final AppUserService appUserService;
    private final IssueService issueService;
    private final ProjectService projectService;
    private final SprintService sprintService;
    private final EpicService epicService;

    @DeleteMapping(path = "/delete")
    public void deleteEpic(@PathVariable Long projectId,
                           @PathVariable Long epicId,
                           HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
            epicService.deleteEpic(epicService.getEpic(epicId));
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), "The issuer is not a member of the project team");
        }
    }

    @PutMapping(path = "/planned")
    public void SetPlannedTo(@PathVariable Long projectId,
                             @PathVariable Long epicId,
                             @RequestParam LocalDate date,
                             HttpServletRequest request,
                             HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
            epicService.setPlannedTo(epicId, date);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), "The issuer is not a member of the project team");
        }
    }

    @PutMapping(path = "/finish")
    public void finishEpic(@PathVariable Long projectId,
                           @PathVariable Long epicId,
                           HttpServletRequest request,
                           HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
            epicService.finishEpic(epicId);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), "The issuer is not a member of the project team");
        }
    }

    @GetMapping(path = "/issues")
    public void getSubIssues(@PathVariable Long projectId,
                             @PathVariable Long epicId,
                             HttpServletRequest request,
                             HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.MEMBER)) {
            response.setContentType(APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            new ObjectMapper().writeValue(
                    response.getOutputStream(),
                    projectService.getEpicSubIssues(projectId, epicId)
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
