package com.agilesekeri.asugar_api.epic;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Epic {
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
    private long id;

    private String title;

    private boolean finished;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;
}
