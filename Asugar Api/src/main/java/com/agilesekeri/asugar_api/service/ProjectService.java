package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.dto.EpicDTO;
import com.agilesekeri.asugar_api.model.dto.IssueDTO;
import com.agilesekeri.asugar_api.model.dto.SubtaskDTO;
import com.agilesekeri.asugar_api.model.entity.*;
import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.model.request.EpicCreateRequest;
import com.agilesekeri.asugar_api.model.request.IssueCreateRequest;
import com.agilesekeri.asugar_api.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ProjectService {
    private final ProjectRepository projectRepository;

    private final AppUserService appUserService;

    private final SprintService sprintService;

    private final EpicService epicService;

    private final IssueService issueService;

    public Boolean checkAccess(Long projectId, String username, Role role) {
        switch (role) {
            case ADMIN:
                return getProject(projectId).getAdmin()
                        .equals(appUserService.loadUserByUsername(username));

            case PRODUCT_OWNER:
                return getProject(projectId).getProductOwner()
                        .equals(appUserService.loadUserByUsername(username));

            case MEMBER:
                return getProject(projectId).getMembers()
                        .contains(appUserService.loadUserByUsername(username));

            default:
                throw new IllegalArgumentException("Unknown role " + role.name());
        }
    }

    public ProjectEntity createProject(String projectName, AppUserEntity admin) {
        getUserProjects(admin.getId()).forEach((projectEntity) -> {
            if(projectEntity.getName().equals(projectName))
                throw new IllegalArgumentException("The same user can not create two projects with the same name");
        });

        ProjectEntity project = new ProjectEntity(projectName, admin);
        projectRepository.save(project);
        sprintService.createSprint(project).setStartedAt(LocalDateTime.now());
        sprintService.createSprint(project);
        return project;
    }

    public ProjectEntity getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow( () ->
                        new IllegalArgumentException("No project with this ID was found"));
    }

    public List<Map<String, String>> getMembersInfo(Long projectId) {
        ProjectEntity project = getProject(projectId);
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

    public void deleteProject(Long projectId) {
        ProjectEntity project = getProject(projectId);

        project.setMembers(null);
        project.setCreator(null);
        if(project.getSprints() != null)
            project.getSprints().forEach(sprintService::deleteSprint);

        if(project.getEpics() != null)
            project.getEpics().forEach(epicService::deleteEpic);

        if(project.getIssues() != null)
            project.getIssues().forEach(issue ->
                    issueService.deleteIssue(issue.getId()));

        projectRepository.flush();
        projectRepository.deleteById(project.getId());
    }

    public boolean addMember(Long projectId, String username) {
        AppUserEntity user = appUserService.loadUserByUsername(username);
        ProjectEntity project = getProject(projectId);
//        boolean result = project.addMember(user);
//        projectRepository.save(project);
//        return result;
        return project.addMember(user);
    }

    public boolean removeMember(Long projectId, String username) {
        AppUserEntity user = appUserService.loadUserByUsername(username);
        ProjectEntity project = getProject(projectId);

        if(project.getAdmin().equals(user))
            throw new IllegalArgumentException("The admin cannot be removed");

        return project.removeMember(user);
    }

    public void setProductOwner(Long productId, AppUserEntity user) {
        ProjectEntity product = getProject(productId);
        product.setProductOwner(user);
    }

    public void finishActiveSprint(Long projectId) {
        ProjectEntity project = getProject(projectId);
        SprintEntity active = getActiveSprint(projectId);
        SprintEntity next = getNextSprint(projectId);

        for(AbstractIssue issue : active.getIncludedIssues()) {
            if(issue.getCondition() != TaskConditionEnum.DONE)
                issue.setSprint(null);
        }

        active.setEndedAt(LocalDateTime.now());
        next.setStartedAt(LocalDateTime.now());

        sprintService.createSprint(project);
    }

    public Set<AbstractIssueDTO> getIssuesToDo(Long projectId) {
        ProjectEntity project = getProject(projectId);
        SprintEntity active = getActiveSprint(projectId);
        SprintEntity next = getNextSprint(projectId);

        Set<AbstractIssueDTO> result = new HashSet<>();
        project.getIssues().forEach(
                issue -> {
                    if(issue.getCondition() != TaskConditionEnum.DONE) {
                        if (issue.getSprint() == active) {
                            AbstractIssueDTO dto = issueService.getIssueInfo(issue.getId());
                            dto.setSprint("active");
                            result.add(dto);
                        }

                        else if(issue.getSprint() == next) {
                            AbstractIssueDTO dto = issueService.getIssueInfo(issue.getId());
                            dto.setSprint("next");
                            result.add(dto);
                        }

                        else if(issue.getSprint() == null) {
                            AbstractIssueDTO dto = issueService.getIssueInfo(issue.getId());
                            dto.setSprint(null);
                            result.add(dto);
                        }
                    }
                }
        );

        return result;
    }

    public Set<AbstractIssueDTO> getAllIssues(Long projectId) {
        ProjectEntity project = getProject(projectId);
        Set<AbstractIssueDTO> result = new HashSet<>();

        project.getIssues().forEach(
                issue -> {
                    AbstractIssueDTO dto = issueService.getIssueInfo(issue.getId());
                    if(dto.getSprint() != null)
                        dto.setSprint(getSprintState(projectId, Long.parseLong(dto.getSprint())));
                    result.add(dto);
                }
        );

        return result;
    }

    public SprintEntity getSprint(Long projectId, String condition) {
        if(condition == null)
            return null;
        else if(condition.equals("active"))
            return getActiveSprint(projectId);
        else if(condition.equals("next"))
            return getNextSprint(projectId);
        else
            return null;
    }

    public String getSprintState(Long projectId, Long sprintId) {
        SprintEntity sprint = sprintService.getSprint(sprintId);
        if(getActiveSprint(projectId).equals(sprint))
            return "active";
        else if(getNextSprint(projectId).equals(sprint))
            return "next";
        else
            return null;
    }

    public SprintEntity getActiveSprint(Long projectId) {
        ProjectEntity project = getProject(projectId);

        return project.getSprints().stream().filter(
                sprint -> (sprint.getStartedAt() != null && sprint.getEndedAt() == null)
        ).findFirst().orElse(null);
    }

    public SprintEntity getNextSprint(Long projectId) {
        ProjectEntity project = getProject(projectId);

        return project.getSprints().stream().filter(
                sprint -> (sprint.getStartedAt() == null && sprint.getEndedAt() == null)
        ).findFirst().orElse(null);
    }

    public Set<AbstractIssueDTO> getEpicSubIssues(Long projectId, Long epicId) {
        return getAllIssues(projectId).stream()
                .filter(issue -> Objects.equals(issue.getEpicId(), epicId))
                .collect(Collectors.toSet());
    }

    public Set<EpicDTO> getEpics(Long projectId) {
        ProjectEntity project = getProject(projectId);
        Set<EpicDTO> result = new HashSet<>();

        project.getEpics().forEach(
                epic -> result.add(epicService.getEpicInfo(epic))
        );

        return result;
    }

    public IssueEntity createIssue(Long projectId, String creatorUsername, IssueCreateRequest createRequest) {
        AppUserEntity issuer = appUserService.loadUserByUsername(creatorUsername);
        ProjectEntity project = getProject(projectId);
        IssueEntity issue = issueService.createIssue(createRequest, issuer, project);

        try {
            if (createRequest.getSprint() != null)
                sprintService.addIssue(
                        getSprint(projectId, createRequest.getSprint()).getId(),
                        issue.getId()
                );

            if (createRequest.getEpicId() != null)
                epicService.addIssue(
                        createRequest.getEpicId(),
                        issue.getId()
                );

            if (createRequest.getAssignedTo() != null && !createRequest.getAssignedTo().isEmpty())
                issueService.assignToMember(
                        issue.getId(),
                        appUserService.loadUserByUsername(createRequest.getAssignedTo())
                );
        } catch (Exception e) {
            issueService.deleteIssue(issue.getId());
            throw new IllegalArgumentException(e.getMessage());
        }

        projectRepository.flush();
        return issue;
    }

    public EpicEntity createEpic(Long projectId, String creatorUsername, EpicCreateRequest createRequest) {
        AppUserEntity issuer = appUserService.loadUserByUsername(creatorUsername);
        ProjectEntity project = getProject(projectId);
        EpicEntity epic = epicService.createEpic(createRequest, issuer, project);

        try {
            if (createRequest.getAssignedTo() != null && !createRequest.getAssignedTo().isEmpty())
                epic.setAssignedTo(issuer);
        } catch (Exception e) {
            epicService.deleteEpic(epic);
            throw new IllegalArgumentException(e.getMessage());
        }

        projectRepository.flush();
        return epic;
    }
}
