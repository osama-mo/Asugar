package com.agilesekeri.asugar_api.project.task;

import java.time.LocalDateTime;
import java.util.Collection;

public class Task {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private Long assignee;
    private Long assignedTo;
    private Collection<String> comments;
    private Task dependentOn;
}
