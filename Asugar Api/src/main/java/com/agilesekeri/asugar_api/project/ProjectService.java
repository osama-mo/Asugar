package com.agilesekeri.asugar_api.project;

import com.agilesekeri.asugar_api.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {
    private ProjectRepository projectRepository;

    public Project createProject(String projectName, AppUser admin) {
        Project project = new Project(projectName, admin);
        projectRepository.save(project);
        return project;
    }

    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow( () ->
                        new IllegalArgumentException("No project with this ID was found"));
    }

    public List<Project> getUserProjects(AppUser user) {
        return projectRepository.findByMembers_Id(user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("There are no projects found for the user"));
    }

    public void deleteProject(Long projectId, Long userId){
        Project target = projectRepository.findById(projectId)
                .orElseThrow( () ->
                        new IllegalStateException("No project with id " + projectId + " was found to delete"));

        if(target.getAdmin().getId() == userId)
            projectRepository.deleteById(projectId);
        else
            throw new IllegalStateException("Not qualified to delete the project with the id " + projectId);
    }

    public Set<AppUser> getMemberSet(Long id) {
        Project project = projectRepository
                .findById(id).orElseThrow(() ->
                        new IllegalArgumentException("Project does not exist"));

        return project.getMembers();
    }

    public boolean addMember(Long projectId, AppUser user) {
        Project project = getProject(projectId);
        boolean result = project.addMember(user);
        projectRepository.save(project);
        return result;
    }

    public boolean removeMember(Long projectId, AppUser user) {
        Project project = getProject(projectId);
        boolean result = project.removeMember(user);
        projectRepository.save(project);
        return result;
    }
}
