package com.agilesekeri.asugar_api.model.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
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
    private LocalDateTime createdAt;
    private LocalDateTime plannedTo;
    private LocalDateTime endedAt;
}
