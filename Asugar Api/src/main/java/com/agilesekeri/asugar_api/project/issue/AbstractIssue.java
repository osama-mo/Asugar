package com.agilesekeri.asugar_api.project.issue;

import com.agilesekeri.asugar_api.appuser.AppUserEntity;
import com.agilesekeri.asugar_api.project.Project;
import com.agilesekeri.asugar_api.project.enums.IssueTypeEnum;
import com.agilesekeri.asugar_api.project.enums.TaskConditionEnum;
import com.agilesekeri.asugar_api.project.epic.Epic;
import com.agilesekeri.asugar_api.project.sprint.Sprint;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.agilesekeri.asugar_api.project.enums.TaskConditionEnum.TODO;

//@Getter
//@Setter
//@MappedSuperclass
//@NoArgsConstructor
//@SuperBuilder
//@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
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
    private Project project;

    @ManyToOne
    @JoinColumn(name = "epic_id", foreignKey = @ForeignKey(name = "ik_epic_id"))
    private Epic epic;

    @ManyToOne
    @JoinColumn(name = "sprint_id", foreignKey = @ForeignKey(name = "ik_sprint_id"))
    private Sprint sprint;

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

    public AbstractIssue(String title, Project project, AppUserEntity creator, IssueTypeEnum issueType) {
        this.title = title;
        this.description = null;
        this.project = project;
        this.epic = null;
        this.sprint = null;
        this.manHour = 0;
        this.condition = TODO;
        this.issueType  = issueType;
        this.creator = creator;
        this.assigned = null;
        this.createdAt = LocalDateTime.now();
        this.plannedTo = null;
        this.endedAt = null;
    }

    public void complete() {
        this.endedAt = LocalDateTime.now();
    }
}