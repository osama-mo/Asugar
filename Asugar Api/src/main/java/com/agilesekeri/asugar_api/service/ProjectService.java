package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.EpicEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import com.agilesekeri.asugar_api.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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

        if(target.getAdmin().getId().equals(userId))
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

    public void setProductOwner(Long productId, AppUserEntity user) {
        ProjectEntity product = getProject(productId);
        product.setProductOwner(user);
    }

    public SprintEntity getSprint(ProjectEntity project, String sprintName) {
        var sprintSet = project.getSprints();

        for(SprintEntity sprint : sprintSet)
            if (sprint.getTitle().equals(sprintName))
                return sprint;

        return null;
    }

    public EpicEntity getEpic(ProjectEntity project, String epicName) {
        var sprintSet = project.getEpics();

        for(EpicEntity epic : sprintSet)
            if (epic.getTitle().equals(epicName))
                return epic;

        return null;
    }

//    public Set<SprintEntity> getSprintSet(Long projectId) {
//        ProjectEntity project = getProject(projectId);
//        return project.getSprints();
//    }

//    public Set<EpicEntity> getEpicSet(Long projectId) {
//        ProjectEntity project = getProject(projectId);
//        return project.getEpics();
//    }

    public void createSprint(ProjectEntity project, String sprintName, AppUserEntity creator) {
        if(getSprint(project, sprintName) != null)
            throw new IllegalArgumentException("A sprint with the same name already exists.");

        SprintEntity newSprint = SprintEntity.builder()
                .createdAt(LocalDateTime.now())
                .title(sprintName)
                .project(project)
                .creator(creator).build();

        project.getSprints().add(newSprint);
    }

    public void createEpic(ProjectEntity project, String epicName, AppUserEntity creator) {
        if(getEpic(project, epicName) != null)
            throw new IllegalArgumentException("A sprint with the same name already exists.");

        EpicEntity newEpic = EpicEntity.builder()
                .createdAt(LocalDateTime.now())
                .title(epicName)
                .project(project)
                .creator(creator).build();

        project.getEpics().add(newEpic);
    }
}
