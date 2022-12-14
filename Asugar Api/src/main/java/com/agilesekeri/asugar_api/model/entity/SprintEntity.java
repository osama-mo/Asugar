package com.agilesekeri.asugar_api.model.entity;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sprint")
public class SprintEntity {
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

//    private String label;

//    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project_id"))
    private ProjectEntity project;

    @OneToMany(mappedBy = "sprint")
    private Set<AbstractIssue> includedIssues;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SprintEntity sprint = (SprintEntity) o;
        return id.equals(sprint.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
