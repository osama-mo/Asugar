package com.agilesekeri.asugar_api.sprint;

import com.agilesekeri.asugar_api.epic.Epic;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
public class Sprint {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private Long id;

    private String title;

    @OneToMany
    private Collection<Epic> epics;

    private String description;

    private boolean finished;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;
}
