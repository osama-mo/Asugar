package com.agilesekeri.asugar_api.project.issue;

import com.agilesekeri.asugar_api.project.subTask.SubtaskEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

//    public IssueEntity(Long projectId, String title, AppUserEntity creator, Long epicId, Long sprintId, IssueTypeEnum issueType) {
//        this.title = title;
//        this.description = null;
//        this.projectId = projectId;
//        this.epicId = epicId;
//        this.sprintId = sprintId;
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