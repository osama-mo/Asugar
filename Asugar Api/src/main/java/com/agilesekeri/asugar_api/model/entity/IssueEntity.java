package com.agilesekeri.asugar_api.model.entity;

import com.agilesekeri.asugar_api.common.AbstractIssue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Collection;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@PrimaryKeyJoinColumn(foreignKey = @ForeignKey(name = "fk_issue_id"))
@Table(name = "issue")
public class IssueEntity extends AbstractIssue {

    @OneToMany(mappedBy = "parentIssue")
    private Collection<SubtaskEntity> subtasks;
}