package com.agilesekeri.asugar_api.project;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
public class Project {
    private Long id;
    private String projectName;
    private Long adminId;
    private Collection<Long> members;
    private Collection<Long> sprints;
    private Collection<Long> Epics;
    private LocalDateTime createdAt;
    private LocalDateTime endedAt;
    private LocalDateTime plannedEndAt;


}
