package com.agilesekeri.asugar_api.model.entity;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import com.agilesekeri.asugar_api.model.enums.IssueTypeEnum;
import com.agilesekeri.asugar_api.model.enums.TaskConditionEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

//@Setter
//@Getter
//@Entity
//@SuperBuilder
//@NoArgsConstructor
//@AllArgsConstructor
@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_issue_id"))
@Table(name = "issue")
public class IssueEntity extends AbstractIssue {

    @OneToMany(mappedBy = "parentIssue")
    private Collection<SubtaskEntity> subtasks;

//    public IssueEntity(String title, ProjectEntity project, AppUserEntity creator, IssueTypeEnum issueType, EpicEntity epic, SprintEntity sprint) {
//        super(title, project, creator, );
//        this.title = title;
//        this.description = null;
//        this.project = project;
//        this.epic = epic;
//        this.sprint = sprint;
//        this.manHour = 0;
//        this.condition = TaskConditionEnum.TODO;
//        this.issueType = issueType;
//        this.creator = creator;
//        this.assigned = null;
//        this.createdAt = LocalDateTime.now();
//        this.plannedTo = null;
//        this.endedAt = null;
//
//        this.subtasks = null;
//    }

}