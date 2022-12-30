package com.agilesekeri.asugar_api.model.dto;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import java.time.LocalDateTime;

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
}
