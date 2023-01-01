package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.dto.IssueDTO;
import com.agilesekeri.asugar_api.model.dto.SubtaskDTO;
import com.agilesekeri.asugar_api.model.entity.*;
import com.agilesekeri.asugar_api.model.enums.Role;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
//        boolean result = project.removeMember(user);
//        projectRepository.save(project);
//        return result;
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
                    if(issue.getCondition() != TaskConditionEnum.DONE &&
                            (issue.getSprint() == active ||
                                    issue.getSprint() == next ||
                                    issue.getSprint() == null))
                        result.add(getIssueInfo(issue.getId()));
                }
        );

//        for(AbstractIssue issue : project.getIssues()) {
//            if(issue.getCondition() != TaskConditionEnum.DONE &&
//                    (issue.getSprint() == active ||
//                     issue.getSprint() == next ||
//                     issue.getSprint() == null)) {
//                AbstractIssueDTO.AbstractIssueDTOBuilder<?, ?> builder = AbstractIssueDTO.builder()
//                        .id(issue.getId())
//                        .issueType(issue.getIssueType().name())
//                        .condition(issue.getCondition().name())
//                        .manHour(issue.getManHour())
//                        .description(issue.getDescription())
//                        .creatorUsername(issue.getCreator().getUsername())
//                        .createdAt(issue.getCreatedAt().toString())
//                        .title(issue.getTitle());
//
//                if(issue.getAssigned() != null)
//                    builder.assignedUsername(issue.getAssigned().getUsername());
//
//                if(issue.getEpic() != null)
//                    builder.epicId(issue.getEpic().getId());
//
//                if(issue.getSprint() != null)
//                    builder.sprint(String.valueOf((Callable<String>) () -> {
//                        if(issue.getSprint() == active)
//                            return "Active";
//                        else if(issue.getSprint() == next)
//                            return "Next";
//                        else
//                            return "NULL";
//                    }));
//
//                result.add(builder.build());
//
////                result.add(getIssueInfo(issue.getId()));
//            }
//        }

        return result;
    }

    public Set<AbstractIssueDTO> getAllIssues(Long projectId) {
        ProjectEntity project = getProject(projectId);
        Set<AbstractIssueDTO> result = new HashSet<>();

        project.getIssues().forEach(
                issue -> result.add(getIssueInfo(issue.getId()))
        );

//        for(AbstractIssue issue : project.getIssues()) {
////            AbstractIssueDTO.AbstractIssueDTOBuilder<?, ?> builder = AbstractIssueDTO.builder()
////                    .id(issue.getId())
////                    .issueType(issue.getIssueType().name())
////                    .condition(issue.getCondition().name())
////                    .manHour(issue.getManHour())
////                    .description(issue.getDescription())
////                    .creatorUsername(issue.getCreator().getUsername())
////                    .createdAt(issue.getCreatedAt().toString())
////                    .title(issue.getTitle());
////
////            if(issue.getAssigned() != null)
////                builder.assignedUsername(issue.getAssigned().getUsername());
////
////            if(issue.getEpic() != null)
////                builder.epicId(issue.getEpic().getId());
////
////            if(issue.getSprint() != null)
////                builder.sprint(issue.getSprint().getId().toString());
//
//            result.add(getIssueInfo(issue.getId()));
//        }

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

    public String getSprintState(Long projectId, Long issueId) {
        AbstractIssue issue = issueService.getIssue(issueId);
        if(getActiveSprint(projectId).equals(issue.getSprint()))
            return "active";
        else if(getNextSprint(projectId).equals(issue.getSprint()))
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
//        ListIterator<SprintEntity> listIterator = project.getSprints().listIterator(0);
//        SprintEntity active = null, next = null;
//
//        while (listIterator.hasNext()) {
//            next = active;
//            active = listIterator.next();
//            if(active.getStartedAt() != null && active.getEndedAt() == null)
//                break;
//        }

        return project.getSprints().stream().filter(
                sprint -> (sprint.getStartedAt() == null && sprint.getEndedAt() == null)
        ).findFirst().orElse(null);

//        assert next != null;
//        return next;
    }

    public AbstractIssueDTO getIssueInfo(Long issueId) {
        AbstractIssue issue = issueService.getIssue(issueId);
        SprintEntity active = getActiveSprint(issue.getProject().getId());
        SprintEntity next = getNextSprint(issue.getProject().getId());
        AbstractIssueDTO dto = null;

        if(IssueEntity.class.equals(issue.getClass()))
            dto = IssueDTO.builder().subtasks(issueService.getSubTaskInfo(issue)).build();
        else if(SubtaskEntity.class.equals(issue.getClass()))
            dto = SubtaskDTO.builder().parentIssue(issueService.getParentIssueInfo(issue)).build();
        else
            throw new IllegalStateException("Unknown issue type");

        assert dto != null;
        dto.setId(issue.getId());
        dto.setProjectId(issue.getProject().getId());
        dto.setIssueType(issue.getIssueType().name());
        dto.setCondition(issue.getCondition().name());
        dto.setManHour(issue.getManHour());
        dto.setDescription(issue.getDescription());
        dto.setCreatorUsername(issue.getCreator().getUsername());
        dto.setCreatedAt(issue.getCreatedAt().toString());
        dto.setTitle(issue.getTitle());


        if(issue.getAssigned() != null)
            dto.setAssignedTo(issue.getAssigned().getUsername());

        if(issue.getEpic() != null)
            dto.setEpicId(issue.getEpic().getId());

        if(issue.getSprint() != null)
            dto.setSprint(getSprintState(issue.getProject().getId(), issueId));

        return dto;
    }
}
