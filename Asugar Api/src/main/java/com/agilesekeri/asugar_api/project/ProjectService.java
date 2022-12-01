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

    public boolean projectExists(String name, AppUser admin) {
        List<Project> list = getUserProjects(admin);

        for(Project project : list)
            if(project.getName() == name)
                return true;

        return false;
    }

    public List<Project> getUserProjects(AppUser user) {
        return projectRepository.findByMembers_Id(user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("There are no projects were found for the user"));
    }
}
