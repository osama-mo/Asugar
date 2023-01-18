package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.service.AppUserService;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "user")
public class AppUserController {
    private final AppUserService appUserService;

    private final ProjectService projectService;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response)
            throws IOException {
        appUserService.genRefreshToken(request, response);
    }

    @PostMapping("/project/create")
    public void createProject(@RequestParam String name,
                              HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            projectService.createProject(name, issuerUsername);

            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }

    @GetMapping("/project/list")
    public void getProjectList(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {
        List<Map<String, String>> result = new ArrayList<>();
        String username = appUserService.getJWTUsername(request);

        if(username != null) {
            AppUserEntity user = appUserService.loadUserByUsername(username);
            List<ProjectEntity> list = projectService.getUserProjects(user.getId());
            for(ProjectEntity project : list)
                result.add(Map.of("id", project.getId().toString(), "name", project.getName()));
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), result);
    }

    @DeleteMapping("/project/{projectId}")
    public void deleteProject(@PathVariable("projectId") Long projectId,
                              HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {
        try {
            String issuerUsername = appUserService.getJWTUsername(request);
            if(!projectService.checkAccess(projectId, issuerUsername, Role.ADMIN))
                throw new IllegalCallerException("The issuer is not the admin of the project");

            projectService.deleteProject(projectId);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);

        } catch (IllegalCallerException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), e.getMessage());
        }
    }
}
