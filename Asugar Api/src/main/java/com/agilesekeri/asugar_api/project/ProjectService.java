package com.agilesekeri.asugar_api.project;

import com.agilesekeri.asugar_api.appuser.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public void deleteProject(Long projectId){
        if(!projectRepository.existsById(projectId)){
            throw new IllegalStateException("No project with id " + projectId + " was found to delete");
        }
        projectRepository.deleteById(projectId);
    }
}
