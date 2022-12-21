package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.EpicEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import com.agilesekeri.asugar_api.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {
    private ProjectRepository projectRepository;

    public ProjectEntity createProject(String projectName, AppUserEntity admin) {
        ProjectEntity project = new ProjectEntity(projectName, admin);
        projectRepository.save(project);
        return project;
    }

    public ProjectEntity getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow( () ->
                        new IllegalArgumentException("No project with this ID was found"));
    }

    public List<ProjectEntity> getUserProjects(AppUserEntity user) {
        return projectRepository.findByMembers_Id(user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException("There are no projects found for the user"));
    }

    public void deleteProject(Long projectId, Long userId){
        ProjectEntity target = projectRepository.findById(projectId)
                .orElseThrow( () ->
                        new IllegalStateException("No project with id " + projectId + " was found to delete"));

        if(target.getAdmin().getId() == userId)
            projectRepository.deleteById(projectId);
        else
            throw new IllegalStateException("Not qualified to delete the project with the id " + projectId);
    }

    public Set<AppUserEntity> getMemberSet(Long id) {
        ProjectEntity project = getProject(id);
        return project.getMembers();
    }

    public boolean addMember(Long projectId, AppUserEntity user) {
        ProjectEntity project = getProject(projectId);
        boolean result = project.addMember(user);
        projectRepository.save(project);
        return result;
    }

    public boolean removeMember(Long projectId, AppUserEntity user) {
        ProjectEntity project = getProject(projectId);
        boolean result = project.removeMember(user);
        projectRepository.save(project);
        return result;
    }

    public Set<SprintEntity> getSprintSet(Long projectId) {
        ProjectEntity project = getProject(projectId);
        return project.getSprints();
    }

    public Set<EpicEntity> getEpicSet(Long projectId) {
        ProjectEntity project = getProject(projectId);
        return project.getEpics();
    }
}
