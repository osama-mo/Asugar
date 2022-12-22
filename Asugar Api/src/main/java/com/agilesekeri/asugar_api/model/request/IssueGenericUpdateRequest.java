package com.agilesekeri.asugar_api.model.request;

import com.agilesekeri.asugar_api.model.entity.*;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.model.enums.IssueTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IssueGenericUpdateRequest {
    private final String title;
    private final String description;
    private final ProjectEntity project;
    private final EpicEntity epic;
    private final SprintEntity sprint;
    private final Integer manHour;
    private final TaskConditionEnum condition;
    private final IssueTypeEnum issueType;
    private final AppUserEntity creator;
    private final AppUserEntity assigned;
    private final LocalDateTime plannedTo;
    private final LocalDateTime endedAt;
    private Collection<SubtaskEntity> subtasks;
}
