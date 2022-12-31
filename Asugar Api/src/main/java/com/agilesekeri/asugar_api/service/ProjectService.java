package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.dto.IssueDTO;
import com.agilesekeri.asugar_api.model.entity.*;
import com.agilesekeri.asugar_api.model.enums.IssueTypeEnum;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.repository.IssueRepository;
import com.agilesekeri.asugar_api.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
        admin.getProjectsCreated().forEach((projectEntity) -> {
            if(projectEntity.getName().equals(projectName))
                throw new IllegalArgumentException("The same user can not create two projects with the same name");
        });

        ProjectEntity project = new ProjectEntity(projectName, admin);
        admin.getProjectsCreated().add(project);
        projectRepository.save(project);
        sprintService.initializeProject(project);
        return project;
    }

    public ProjectEntity getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow( () ->
                        new IllegalArgumentException("No project with this ID was found"));
    }

    public List<Map<String, String>> getMembersInfo(ProjectEntity project) {
        List<Map<String, String>> result = new ArrayList<>();
        for(AppUserEntity user : project.getMembers()) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("first_name", user.getFirstName());
            userInfo.put("last_name", user.getLastName());
            userInfo.put("email", user.getUsername());

            if(project.getAdmin() == user)
                userInfo.put("title", "Admin");
            else if(project.getProductOwner() == user)
                userInfo.put("title", "Product Owner");
            else
                userInfo.put("title", "Member");

            result.add(userInfo);
        }

        return result;
    }

    public List<ProjectEntity> getUserProjects(Long userID) {
        return projectRepository.findByMembers_Id(userID)
                .orElseThrow(() ->
                        new IllegalArgumentException("There are no projects found for the user"));
    }

    public void deleteProject(ProjectEntity project, AppUserEntity user){
        user.getProjectsCreated().remove(project);
        projectRepository.deleteById(project.getId());
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

    public SprintEntity getActiveSprint(ProjectEntity project) {
        ListIterator<SprintEntity> listIterator = project.getSprints().listIterator(0);
        SprintEntity active = null;

        while (listIterator.hasNext()) {
            active = listIterator.next();
            if(active.getStartedAt() != null && active.getEndedAt() == null)
                break;
        }

        return active;
    }

    public SprintEntity getNextSprint(ProjectEntity project) {
        ListIterator<SprintEntity> listIterator = project.getSprints().listIterator(0);
        SprintEntity active = null, next = null;

        while (listIterator.hasNext()) {
            next = active;
            active = listIterator.next();
            if(active.getStartedAt() != null && active.getEndedAt() == null)
                break;
        }

        assert next != null;
        return next;
    }

    public SprintEntity getSprint(ProjectEntity project, String condition) {
        if(condition == null)
            return null;
        else if(condition.equals("active"))
            return getActiveSprint(project);
        else if(condition.equals("next"))
            return getNextSprint(project);
        else
            return null;
    }

    public void finishActiveSprint(ProjectEntity project) {
        SprintEntity active = getActiveSprint(project);
        SprintEntity next = getNextSprint(project);

        for(AbstractIssue issue : active.getIncludedIssues()) {
            if(issue.getCondition() != TaskConditionEnum.DONE)
                issue.setSprint(null);
        }

        active.setEndedAt(LocalDateTime.now());
        next.setStartedAt(LocalDateTime.now());

        sprintService.createSprint(project);
    }

    public Set<AbstractIssueDTO> getIssuesToDo(ProjectEntity project) {
        SprintEntity active = getActiveSprint(project);
        SprintEntity next = getNextSprint(project);

        Set<AbstractIssueDTO> result = new HashSet<>();
        for(AbstractIssue issue : project.getIssues()) {
            if(issue.getCondition() != TaskConditionEnum.DONE &&
                    (issue.getSprint() == active ||
                     issue.getSprint() == next ||
                     issue.getSprint() == null)) {
                AbstractIssueDTO.AbstractIssueDTOBuilder<?, ?> builder = AbstractIssueDTO.builder()
                        .id(issue.getId())
                        .issueType(issue.getIssueType().name())
                        .condition(issue.getCondition().name())
                        .manHour(issue.getManHour())
                        .description(issue.getDescription())
                        .creatorUsername(issue.getCreator().getUsername())
                        .createdAt(issue.getCreatedAt().toString())
                        .title(issue.getTitle());

                if(issue.getAssigned() != null)
                    builder.assignedUsername(issue.getAssigned().getUsername());

                if(issue.getEpic() != null)
                    builder.epicId(issue.getEpic().getId());

                if(issue.getSprint() != null)
                    builder.sprint(String.valueOf((Callable<String>) () -> {
                        if(issue.getSprint() == active)
                            return "Active";
                        else if(issue.getSprint() == next)
                            return "Next";
                        else
                            return "NULL";
                    }));

                result.add(builder.build());
            }
        }

        return result;
    }

    public Set<AbstractIssueDTO> getAllIssues(ProjectEntity project) {
        Set<AbstractIssueDTO> result = new HashSet<>();
        for(AbstractIssue issue : project.getIssues()) {
            AbstractIssueDTO.AbstractIssueDTOBuilder<?, ?> builder = AbstractIssueDTO.builder()
                    .id(issue.getId())
                    .issueType(issue.getIssueType().name())
                    .condition(issue.getCondition().name())
                    .manHour(issue.getManHour())
                    .description(issue.getDescription())
                    .creatorUsername(issue.getCreator().getUsername())
                    .createdAt(issue.getCreatedAt().toString())
                    .title(issue.getTitle());

            if(issue.getAssigned() != null)
                builder.assignedUsername(issue.getAssigned().getUsername());

            if(issue.getEpic() != null)
                builder.epicId(issue.getEpic().getId());

            if(issue.getSprint() != null)
                builder.sprint(issue.getSprint().getId().toString());

            result.add(builder.build());
        }

        return result;
    }

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
