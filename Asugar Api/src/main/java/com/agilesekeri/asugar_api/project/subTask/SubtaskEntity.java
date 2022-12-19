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
@Table(name = "subtask")
public class SubtaskEntity extends AbstractIssue {
    @ManyToOne
    @JoinColumn(name = "parent_issue", foreignKey = @ForeignKey(name = "fk_parent_issue_id"))
    private IssueEntity parentIssue;

    SubtaskEntity(IssueEntity parentIssue) {
        super();
        this.parentIssue = parentIssue;
    }

}
