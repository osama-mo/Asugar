package com.agilesekeri.asugar_api.project;

import com.agilesekeri.asugar_api.sprint.Sprint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Project {

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

    private String projectName;

    private Long manager;

    @ElementCollection
    private Collection<Long> members;

    @OneToMany
    private Collection<Sprint> sprints;

    private boolean finished;

    private LocalDateTime createdAt;

    private LocalDateTime finishedAt;


    public Project(String firstName,
                   long manager,
                   Collection<Long> members,
                   boolean finished) {

        this.projectName = firstName;
        this.manager = manager;
        this.members = members;
        this.finished = finished;
    }
}
