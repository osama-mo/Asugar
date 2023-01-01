package com.agilesekeri.asugar_api.model.dto;

import com.agilesekeri.asugar_api.model.entity.SubtaskEntity;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@SuperBuilder
@Setter
public class IssueDTO extends AbstractIssueDTO {
    private Set<Map<String, String>> subtasks;
}
