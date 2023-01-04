package com.agilesekeri.asugar_api.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "epic")
public class EpicEntity {
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
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project_id"))
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "reporter_id", foreignKey = @ForeignKey(name = "fk_reporter_id"))
    private AppUserEntity creator;

    @ManyToOne
    @JoinColumn(name = "assigned_id", foreignKey = @ForeignKey(name = "fk_assigned_id"))
    private AppUserEntity assignedTo;

    @OneToMany(mappedBy = "epic")
    private Set<IssueEntity> includedIssues;

    private int manHour;

    private LocalDateTime createdAt;

    private LocalDate plannedTo;

    private LocalDateTime endedAt;
}
