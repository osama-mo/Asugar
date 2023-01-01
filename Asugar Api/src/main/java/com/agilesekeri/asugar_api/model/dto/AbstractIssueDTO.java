package com.agilesekeri.asugar_api.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Setter
@Getter
@SuperBuilder
public class AbstractIssueDTO {
    private Long id;
    private String title;
    private String description;
    private Long projectId;
    private Long epicId;
    private String sprint;
    private Integer manHour;
    private String condition;
    private String issueType;
    private String creatorUsername;
    private String assignedUsername;
    private String createdAt;
//    private LocalDateTime plannedTo;
//    private LocalDateTime endedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractIssueDTO that = (AbstractIssueDTO) o;
        return id.equals(that.id) &&
                title.equals(that.title) &&
                Objects.equals(description, that.description) &&
                projectId.equals(that.projectId) &&
                Objects.equals(epicId, that.epicId) &&
                Objects.equals(sprint, that.sprint) &&
                Objects.equals(manHour, that.manHour) &&
                condition.equals(that.condition) &&
                issueType.equals(that.issueType) &&
                creatorUsername.equals(that.creatorUsername) &&
                Objects.equals(assignedUsername, that.assignedUsername) &&
                createdAt.equals(that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, projectId, epicId, sprint, manHour, condition, issueType, creatorUsername, assignedUsername, createdAt);
    }
}
