package com.agilesekeri.asugar_api.model.dto;

import com.agilesekeri.asugar_api.model.entity.IssueEntity;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.Set;

@SuperBuilder
public class SubtaskDTO extends AbstractIssueDTO {
    private Map<String, String> parentIssue;
}
