package com.agilesekeri.asugar_api.common;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.enums.IssueTypeEnum;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.model.entity.EpicEntity;
import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.agilesekeri.asugar_api.model.enums.TaskConditionEnum.TODO;

//@Getter
//@Setter
//@MappedSuperclass
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "abstract_issue")
public abstract class AbstractIssue {
    @Id
    @SequenceGenerator(
            name = "abstract_issue_sequence",
            sequenceName = "abstract_issue_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "abstract_issue_sequence"
    )
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "ik_project_id"))
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "epic_id", foreignKey = @ForeignKey(name = "ik_epic_id"))
    private EpicEntity epic;

    @ManyToOne
    @JoinColumn(name = "sprint_id", foreignKey = @ForeignKey(name = "ik_sprint_id"))
    private SprintEntity sprint;

    private Integer manHour;

    private TaskConditionEnum condition;

    private IssueTypeEnum issueType;

    @ManyToOne
    @JoinColumn(name = "creator_id", foreignKey = @ForeignKey(name = "ik_creator_id"))
    private AppUserEntity creator;

    @ManyToOne
    @JoinColumn(name = "assigned_id", foreignKey = @ForeignKey(name = "ik_assigned_id"))
    private AppUserEntity assigned;

    private LocalDateTime createdAt;
    private LocalDateTime plannedTo;
    private LocalDateTime endedAt;
    public void complete() {
        this.endedAt = LocalDateTime.now();
    }
}