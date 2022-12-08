package com.agilesekeri.asugar_api.project;

import com.agilesekeri.asugar_api.appuser.AppUser;
import com.agilesekeri.asugar_api.appuser.AppUserService;
import javafx.util.Pair;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping(path = "{projectId}")
public class ProjectController {
    private final ProjectService projectService;

    private final ProjectRepository projectRepository;

    private final AppUserService appUserService;


    @GetMapping(path = "/members")
    public List<Pair<String, String>> getMembers(@PathVariable Long projectId) {
        Set<AppUser> members = projectService.getMemberSet(projectId);
        List<Pair<String, String>> list = new ArrayList<>();

        for(AppUser user : members)
            list.add(new Pair<>(user.getFirstName(), user.getLastName()));

        return list;
    }

    @PutMapping(path = "/members")
    public boolean addMember(@PathVariable Long projectId, @RequestParam String username) {
        Project project = projectService.getProject(projectId);
        AppUser user = appUserService.loadUserByUsername(username);
        project.addMember(user);
        projectRepository.save(project);
        return true;
    }

    @DeleteMapping(path = "/members")
    public boolean removeMember(@PathVariable Long projectId, @RequestParam String username) {
        Project project = projectService.getProject(projectId);
        AppUser user = appUserService.loadUserByUsername(username);
        project.removeMember(user);
        projectRepository.save(project);
        return true;
    }

    @GetMapping(path = "/members/{username}")
    public void viewMember(@PathVariable Long projectId, @PathVariable String username) {
        //TODO
    }
}
