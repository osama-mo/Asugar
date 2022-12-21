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
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());

                Algorithm refreshAlgorithm = Algorithm.HMAC256(refreshSecret);
                Algorithm accessAlgorithm = Algorithm.HMAC256(accessSecret);
                JWTVerifier verifier = JWT.require(refreshAlgorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);

                String username = decodedJWT.getSubject();
                UserDetails userDetails = appUserService.loadUserByUsername(username);
                String access_token = JWT.create()
                        .withSubject(userDetails.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .sign(accessAlgorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", access_token);
                tokens.put("refreshToken", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Token is missing");
        }
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
    public List<Pair<String, Long>> getProjectList(HttpServletRequest request) throws IOException {
        List<Pair<String, Long>> result = new ArrayList<>();
        String username = appUserService.getJWTUsername(request);

        if(username != null) {
            AppUserEntity user = appUserService.loadUserByUsername(username);
            List<ProjectEntity> list = projectService.getUserProjects(user);
            for(ProjectEntity project : list)
                result.add(Pair.of(project.getName(), project.getId()));
        }

        return result;
    }

    @DeleteMapping("/project/{projectId}")
    public void deleteProject(@PathVariable("projectId") Long projectId, HttpServletRequest request) throws IOException {
        String username = appUserService.getJWTUsername(request);
        if(username != null) {
            AppUserEntity user = appUserService.loadUserByUsername(username);
            projectService.deleteProject(projectId, user.getId());
        }
    }
}
