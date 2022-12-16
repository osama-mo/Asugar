package com.agilesekeri.asugar_api.project;

import com.agilesekeri.asugar_api.appuser.AppUser;
import com.agilesekeri.asugar_api.appuser.AppUserService;
import com.agilesekeri.asugar_api.project.sprint.Sprint;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping(path = "{projectId}")
public class ProjectController {
    private final ProjectService projectService;

    private final AppUserService appUserService;


    @GetMapping(path = "/members")
    public List<Pair<String, String>> getMembers(@PathVariable Long projectId, HttpServletRequest request) throws IOException {
        Set<AppUser> members = projectService.getMemberSet(projectId);
        String username = appUserService.getJWTUsername(request);
        AppUser issuer = appUserService.loadUserByUsername(username);

        if(members.contains(issuer)) {
            List<Pair<String, String>> list = new ArrayList<>();
            for(AppUser user : members)
                list.add(new Pair<>(user.getFirstName(), user.getLastName()));
            return list;
        }
        else
            throw new IllegalCallerException("The request came from user not a member of the project");
    }

    @PutMapping(path = "/members")
    public boolean addMember(@PathVariable Long projectId, @RequestParam String username, HttpServletRequest request) throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUser issuer = appUserService.loadUserByUsername(issuerUsername);
        Project project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUser user = appUserService.loadUserByUsername(username);
        projectService.addMember(projectId, user);
        return true;
    }

    @DeleteMapping(path = "/members")
    public boolean removeMember(@PathVariable Long projectId, @RequestParam String username, HttpServletRequest request) throws IOException {
        String issuerUsername = appUserService.getJWTUsername(request);
        AppUser issuer = appUserService.loadUserByUsername(issuerUsername);
        Project project = projectService.getProject(projectId);

        if(project.getAdmin() != issuer)
            throw new IllegalCallerException("The issuer is not qualified for the operation");

        AppUser user = appUserService.loadUserByUsername(username);
        projectService.removeMember(projectId, user);
        return true;
    }

    @GetMapping(path = "/members/{username}")
    public void viewMember(@PathVariable Long projectId, @PathVariable String username) {
        //TODO
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
