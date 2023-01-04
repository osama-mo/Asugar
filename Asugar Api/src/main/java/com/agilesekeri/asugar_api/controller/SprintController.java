package com.agilesekeri.asugar_api.controller;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.service.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
                                   HttpServletRequest request)
            throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        if(!projectService.checkAccess(projectId, issuerUsername, Role.ADMIN))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        projectService.finishActiveSprint(projectId);
    }
}
