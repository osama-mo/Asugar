package com.agilesekeri.asugar_api.project.sprint;

import com.agilesekeri.asugar_api.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Sprint {
    @Id
    @SequenceGenerator(
            name = "sprint_sequence",
            sequenceName = "sprint_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "sprint_sequence"
    )
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    private Project project;

    //TODO included issues

    private LocalDateTime createdAt;

    private LocalDateTime plannedTo;

    private LocalDateTime endedAt;
}
