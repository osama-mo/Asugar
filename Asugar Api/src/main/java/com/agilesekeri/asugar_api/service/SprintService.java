package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.repository.SprintRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ListIterator;

@Service
@AllArgsConstructor
@Transactional
public class SprintService {
    private final SprintRepository sprintRepository;

    public void initializeProject(ProjectEntity project) {
        createSprint(project).setStartedAt(LocalDateTime.now());
        createSprint(project);
    }

    public SprintEntity createSprint(ProjectEntity project) {
        SprintEntity newSprint = SprintEntity.builder()
                .project(project)
                .build();

        return sprintRepository.save(newSprint);
    }
}