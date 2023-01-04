package com.agilesekeri.asugar_api.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
public class EpicDTO {
    private Long id;
    private String title;
    private String description;
    private Long projectId;
    private Integer manHour;
    private String creatorUsername;
    private String assignedTo;
    private String createdAt;
    private String plannedTo;
    private String endedAt;
}
