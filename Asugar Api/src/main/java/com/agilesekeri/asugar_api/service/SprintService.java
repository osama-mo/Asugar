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

//    public SprintEntity getSprint(Long id) {
//        return sprintRepository.findById(id)
//                .orElseThrow( () ->
//                        new IllegalArgumentException("No sprint with this id was found"));
//    }

    public void initializeProject(ProjectEntity project) {
        createSprint(project).setStartedAt(LocalDateTime.now());
        createSprint(project);
    }

    public Pair<SprintEntity, SprintEntity> getMainSprints(ProjectEntity project) {
        ListIterator<SprintEntity> listIterator = project.getSprints().listIterator(project.getSprints().size());
        SprintEntity active = null, next = null;

        while (listIterator.hasPrevious()) {
            next = active;
            active = (SprintEntity) listIterator.previous();
            if(active.getStartedAt() != null && active.getEndedAt() == null)
                break;
        }

        assert active != null;
        assert next != null;
        return Pair.of(active, next);
    }

    public SprintEntity createSprint(ProjectEntity project) {
        SprintEntity newSprint = SprintEntity.builder()
                .project(project)
                .build();

        return sprintRepository.save(newSprint);
    }

    public void finishActiveSprint(ProjectEntity project) {
        Pair<SprintEntity, SprintEntity> sprints = getMainSprints(project);
        SprintEntity active = sprints.getFirst();
        SprintEntity next = sprints.getSecond();

        for(AbstractIssue issue : active.getIncludedIssues()) {
            if(issue.getCondition() != TaskConditionEnum.DONE)
                issue.setSprint(null);
        }

        active.setEndedAt(LocalDateTime.now());
        next.setStartedAt(LocalDateTime.now());

        createSprint(project);
    }

//    public SprintEntity getSprint(ProjectEntity project, ) {
//        var sprintSet = project.getSprints();
//
//        for(SprintEntity sprint : sprintSet)
//            if (sprint.getLabel().equals(label))
//                return sprint;
//
//        return null;
//    }
}