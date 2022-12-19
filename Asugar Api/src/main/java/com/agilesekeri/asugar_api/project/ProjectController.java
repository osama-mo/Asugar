package com.agilesekeri.asugar_api.project;

import com.agilesekeri.asugar_api.appuser.AppUserEntity;
import com.agilesekeri.asugar_api.appuser.AppUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.InvalidTransactionException;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
@RequestMapping(path = "{projectId}")
public class ProjectController {
    private final ProjectService projectService;

    private final AppUserService appUserService;


    @GetMapping(path = "/members")
    public void getMembers(@PathVariable Long projectId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Set<AppUserEntity> members = projectService.getMemberSet(projectId);
        String username = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(username);

        if(members.contains(issuer)) {
            List<Map<String, String>> list = new ArrayList<>();
            for(AppUserEntity user : members) {
                Map<String, String> userInfo = new HashMap<>();
                userInfo.put("First Name", user.getFirstName());
                userInfo.put("Last Name", user.getLastName());
                userInfo.put("Email Address", user.getUsername());
                list.add(userInfo);
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
        Project project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUserEntity user = appUserService.loadUserByUsername(username);
        projectService.addMember(projectId, user);
        return true;
    }

    @DeleteMapping(path = "/members")
    public boolean removeMember(@PathVariable Long projectId, @RequestParam String username, HttpServletRequest request) throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        Project project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUserEntity user = appUserService.loadUserByUsername(username);
        projectService.removeMember(projectId, user);
        return true;
    }

    @GetMapping(path = "/members/{username}")
    public void viewMember(@PathVariable Long projectId, @PathVariable String username, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUserEntity issuer = appUserService.loadUserByUsername(issuerUsername);
        Project project = projectService.getProject(projectId);
        Set<AppUserEntity> members = project.getMembers();

        if(!members.contains(issuer))
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUserEntity target = appUserService.loadUserByUsername(username);
        if(members.contains(target)) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("First Name", target.getFirstName());
            userInfo.put("Last Name", target.getLastName());
            userInfo.put("Email Address", target.getUsername());

            if(project.getAdmin() == target)
                userInfo.put("Title", "Admin");
            else if(project.getProductOwner() == target)
                userInfo.put("Title", "Product Owner");
            else
                userInfo.put("Title", "Member");

            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), userInfo);
        }

        else
           throw new InvalidTransactionException("The user is not a member of the project");
    }

    @GetMapping(path = "/sprints")
    public void getSprints(@PathVariable Long projectId, HttpServletResponse response) {
        //TODO
//        Set<Sprint> set = projectService.getSprintSet(projectId);
//
//        for(var sprint : set) {
//
//        }
    }
}
