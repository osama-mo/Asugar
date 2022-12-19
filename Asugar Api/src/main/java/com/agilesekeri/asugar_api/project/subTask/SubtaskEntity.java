package com.agilesekeri.asugar_api.project.subTask;

import com.agilesekeri.asugar_api.project.issue.AbstractIssue;
import com.agilesekeri.asugar_api.project.issue.IssueEntity;
import jdk.jfr.Frequency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_subtask_id"))
@Table(name = "subtask")
public class SubtaskEntity extends AbstractIssue {
    @Id
    @SequenceGenerator(
            name = "subtask_sequence",
            sequenceName = "subtask_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "subtask_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_issue", foreignKey = @ForeignKey(name = "fk_parent_issue_id"))
    private IssueEntity parentIssue;

    SubtaskEntity(IssueEntity parentIssue) {
        super();
        this.parentIssue = parentIssue;
    }

}
