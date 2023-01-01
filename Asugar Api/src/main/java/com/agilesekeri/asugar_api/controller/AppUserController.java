package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.service.AppUserService;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.service.ProjectService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.agilesekeri.asugar_api.security.authentication.CustomAuthenticationFilter.accessSecret;
import static com.agilesekeri.asugar_api.security.authentication.CustomAuthenticationFilter.refreshSecret;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@AllArgsConstructor
@RequestMapping(path = "user")
public class AppUserController {

    private final AppUserService appUserService;

    private final ProjectService projectService;

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        appUserService.genRefreshToken(request, response);
    }

    @PostMapping("/project/create")
    public void createProject(@RequestParam String name, HttpServletRequest request) throws IOException {
        String username = appUserService.getJWTUsername(request);
        if(username != null) {
            AppUserEntity admin = appUserService.loadUserByUsername(username);
            projectService.createProject(name, admin);
        }
    }

    @GetMapping("/project/list")
    public void getProjectList(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
    public void deleteProject(@PathVariable("projectId") Long projectId, HttpServletRequest request) throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        ProjectEntity project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        projectService.deleteProject(project.getId());
    }
}
