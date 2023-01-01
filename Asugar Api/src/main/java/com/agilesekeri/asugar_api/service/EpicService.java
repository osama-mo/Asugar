package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.EpicEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.request.EpicCreateRequest;
import com.agilesekeri.asugar_api.repository.EpicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class EpicService {
    private final EpicRepository epicRepository;

    private final IssueService issueService;

    public EpicEntity getEpic(Long epicId) {
        return epicRepository.findById(epicId)
                .orElseThrow( () ->
                        new IllegalArgumentException("No epic with this ID was found"));
    }

    public EpicEntity getEpic(ProjectEntity project, String epicName) {
        var sprintSet = project.getEpics();

        for(EpicEntity epic : sprintSet)
            if (epic.getTitle().equals(epicName))
                return epic;

        return null;
    }

    public EpicEntity createEpic(EpicCreateRequest request,
                                 AppUserEntity creator,
                                 ProjectEntity project) {
        EpicEntity epic = EpicEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .creator(creator)
                .createdAt(LocalDateTime.now())
                .project(project)
                .plannedTo(request.getDue())
                .build();

        return epicRepository.save(epic);
    }

    public void addIssue(Long epicId, Long issueId) {
        AbstractIssue issue = issueService.getIssue(issueId);
        EpicEntity epic = getEpic(epicId);
        issue.setEpic(epic);
        epicRepository.flush();
    }

    public void deleteEpic(EpicEntity epic) {
        epic.getIncludedIssues().forEach(issue -> issue.setEpic(null));
        epicRepository.delete(epic);
    }
}