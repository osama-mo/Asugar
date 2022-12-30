package com.agilesekeri.asugar_api.model.dto;

import com.agilesekeri.asugar_api.model.entity.IssueEntity;
import lombok.Builder;

@Builder
public class SubtaskDTO extends AbstractIssueDTO {
    private Long parentIssueId;
}
