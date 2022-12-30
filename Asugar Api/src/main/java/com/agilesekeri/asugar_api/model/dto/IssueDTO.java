package com.agilesekeri.asugar_api.model.dto;

import com.agilesekeri.asugar_api.model.entity.SubtaskEntity;
import lombok.Builder;
import lombok.Setter;

import java.util.Collection;

@Builder
@Setter
public class IssueDTO extends AbstractIssueDTO {
    private Collection<String> subtasks;
}
