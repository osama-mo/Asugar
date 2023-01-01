package com.agilesekeri.asugar_api.service;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import com.agilesekeri.asugar_api.repository.SprintRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class SprintService {
    private final SprintRepository sprintRepository;

    private final IssueService issueService;

    public SprintEntity createSprint(ProjectEntity project) {
        SprintEntity newSprint = SprintEntity.builder()
                .project(project)
                .build();

        return sprintRepository.save(newSprint);
    }

    public SprintEntity getSprint(Long sprintId) {
        return sprintRepository.findById(sprintId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Sprint with the id " +
                                sprintId.toString() +
                                " does not exist"));
    }

    public void addIssue(Long sprintId, Long issueId) {
        AbstractIssue issue = issueService.getIssue(issueId);
        SprintEntity sprint = getSprint(sprintId);
        issue.setSprint(sprint);
//        return true;
    }

    public boolean removeIssue(Long sprintId, Long issueId) {
        AbstractIssue issue = issueService.getIssue(issueId);
        SprintEntity sprint = getSprint(sprintId);
        return sprint.getIncludedIssues().remove(issue);
    }

    public void deleteSprint(SprintEntity sprint) {
        sprint.setProject(null);
        sprint.getIncludedIssues().forEach(issue -> issue.setSprint(null));
        sprintRepository.delete(sprint);
    }
}