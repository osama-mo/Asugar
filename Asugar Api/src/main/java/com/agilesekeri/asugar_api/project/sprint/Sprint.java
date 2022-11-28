package com.agilesekeri.asugar_api.project.sprint;

import com.agilesekeri.asugar_api.project.task.Task;

import java.time.LocalDateTime;
import java.util.Collection;

public class Sprint {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private LocalDateTime plannedTo;
    private Collection<Task> tasks;
}
