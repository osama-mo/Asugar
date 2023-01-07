package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/{projectId}/sprints")
@RequiredArgsConstructor
@Getter
@Setter
public class SprintController {
    private final AppUserService appUserService;
    private final ProjectService projectService;

    @PutMapping(path = "/finish")
    public void finishActiveSprint(@PathVariable Long projectId,
                                   HttpServletRequest request,
                                   HttpServletResponse response)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(projectService.checkAccess(projectId, issuerUsername, Role.ADMIN)) {
            projectService.finishActiveSprint(projectId);
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
        }
        else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), "The issuer is not qualified for the operation");
        }
    }
}
