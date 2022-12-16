package com.agilesekeri.asugar_api.project.epic;

import com.agilesekeri.asugar_api.appuser.AppUser;
import com.agilesekeri.asugar_api.project.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Epic {
    @Id
    @SequenceGenerator(
            name = "epic_sequence",
            sequenceName = "epic_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "epic_sequence"
    )
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    private Project project;

    @ManyToOne
    private AppUser reporter;

    @ManyToOne
    private AppUser assignedTo;

    //TODO included issues

    private LocalDateTime createdAt;

    private LocalDateTime plannedTo;

    private LocalDateTime endedAt;
}
