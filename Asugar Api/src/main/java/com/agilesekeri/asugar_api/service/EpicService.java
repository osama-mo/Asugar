package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.dto.AbstractIssueDTO;
import com.agilesekeri.asugar_api.model.dto.EpicDTO;
import com.agilesekeri.asugar_api.model.dto.IssueDTO;
import com.agilesekeri.asugar_api.model.dto.SubtaskDTO;
import com.agilesekeri.asugar_api.model.entity.*;
import com.agilesekeri.asugar_api.model.request.EpicCreateRequest;
import com.agilesekeri.asugar_api.repository.EpicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
@Transactional
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

    public void setPlannedTo(Long epicId, LocalDate date) {
        EpicEntity epic = getEpic(epicId);
        epic.setPlannedTo(date);
        epicRepository.save(epic);
    }

    public void finishEpic(Long epicId) {
        EpicEntity epic = getEpic(epicId);
        epic.setEndedAt(LocalDateTime.now());
        epicRepository.save(epic);
    }

    public EpicDTO getEpicInfo(EpicEntity epic) {
        return EpicDTO.builder()
                .id(epic.getId())
                .projectId(epic.getProject().getId())
                .manHour(epic.getManHour())
                .description(epic.getDescription())
                .creatorUsername(epic.getCreator().getUsername())
                .createdAt(epic.getCreatedAt().toString())
                .endedAt(epic.getEndedAt().toLocalDate().toString())
                .plannedTo(epic.getPlannedTo().toString())
                .title(epic.getTitle())
                .build();
    }
}