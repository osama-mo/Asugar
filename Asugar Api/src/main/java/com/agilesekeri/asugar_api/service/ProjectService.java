package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.dto.IssueDTO;
import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.EpicEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import com.agilesekeri.asugar_api.model.enums.IssueTypeEnum;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.repository.IssueRepository;
import com.agilesekeri.asugar_api.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {
    private ProjectRepository projectRepository;

    private final SprintService sprintService;

    public ProjectEntity createProject(String projectName, AppUserEntity admin) {
        ProjectEntity project = new ProjectEntity(projectName, admin);
        projectRepository.save(project);
        sprintService.initializeProject(project);
        return project;
    }

    public ProjectEntity getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow( () ->
                        new IllegalArgumentException("No project with this ID was found"));
    }

    public List<ProjectEntity> getUserProjects(Long userID) {
        return projectRepository.findByMembers_Id(userID)
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

    public Set<AbstractIssueDTO> getIssuesToDo(ProjectEntity project) {
        Pair<SprintEntity, SprintEntity> sprints = sprintService.getMainSprints(project);
        SprintEntity active = sprints.getFirst();
        SprintEntity next = sprints.getSecond();

        Set<AbstractIssueDTO> result = new HashSet<>();
        for(AbstractIssue issue : project.getIssues()) {
            if(issue.getCondition() != TaskConditionEnum.DONE &&
                    (issue.getSprint() == active ||
                     issue.getSprint() == next ||
                     issue.getSprint() == null)) {
                result.add(AbstractIssueDTO.builder()
                        .id(issue.getId())
                        .issueType(issue.getIssueType().name())
                        .assignedUsername(issue.getAssigned().getUsername())
                        .condition(issue.getCondition().name())
                        .manHour(issue.getManHour())
                        .description(issue.getDescription())
                        .creatorUsername(issue.getCreator().getUsername())
                        .epicId(issue.getEpic().getId())
                        .sprint(String.valueOf((Callable<String>) () -> {
                            if(issue.getSprint() == active)
                                return "Active";
                            else if(issue.getSprint() == next)
                                return "Next";
                            else
                                return "NULL";
                        }))
                        .createdAt(issue.getCreatedAt())
                        .title(issue.getTitle())
                        .build());
            }
        }

        return result;
    }

    public Set<AbstractIssueDTO> getAllIssues(ProjectEntity project) {
        Set<AbstractIssueDTO> result = new HashSet<>();
        for(AbstractIssue issue : project.getIssues()) {
            result.add(AbstractIssueDTO.builder()
                    .id(issue.getId())
                    .issueType(issue.getIssueType().name())
                    .assignedUsername(issue.getAssigned().getUsername())
                    .condition(issue.getCondition().name())
                    .manHour(issue.getManHour())
                    .description(issue.getDescription())
                    .creatorUsername(issue.getCreator().getUsername())
                    .epicId(issue.getEpic().getId())
                    .sprint(project.getName().charAt(0) + issue.getSprint().getId().toString())
                    .createdAt(issue.getCreatedAt())
                    .title(issue.getTitle())
                    .build());
        }

        return result;
    }

//    public Set<SprintEntity> getSprintSet(Long projectId) {
//        ProjectEntity project = getProject(projectId);
//        return project.getSprints();
//    }

//    public Set<EpicEntity> getEpicSet(Long projectId) {
//        ProjectEntity project = getProject(projectId);
//        return project.getEpics();
//    }

    public EpicEntity getEpic(ProjectEntity project, String epicName) {
        var sprintSet = project.getEpics();

        for(EpicEntity epic : sprintSet)
            if (epic.getTitle().equals(epicName))
                return epic;

        return null;
    }

    public void createEpic(ProjectEntity project, String epicName, AppUserEntity creator) {
        if(getEpic(project, epicName) != null)
            throw new IllegalArgumentException("A sprint with the same name already exists.");

        EpicEntity newEpic = EpicEntity.builder()
                .createdAt(LocalDateTime.now())
                .title(epicName)
                .project(project)
                .creator(creator)
                .build();

        project.getEpics().add(newEpic);
    }
}
