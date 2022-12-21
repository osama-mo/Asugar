package com.agilesekeri.asugar_api.model.entity;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@SuperBuilder
//@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_subtask_id"))
@Table(name = "subtask")
public class SubtaskEntity extends AbstractIssue {
    @ManyToOne
    @JoinColumn(name = "parent_issue", foreignKey = @ForeignKey(name = "fk_parent_issue_id"))
    private IssueEntity parentIssue;

//    SubtaskEntity(String title,
//                  ProjectEntity project,
//                  AppUserEntity creator,
//                  EpicEntity epic,
//                  SprintEntity sprint,
//                  IssueTypeEnum issueType,
//                  IssueEntity parentIssue) {
//        super(title, project, creator, issueType, epic, sprint);
//        this.parentIssue = parentIssue;
//    }
}
