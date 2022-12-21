package com.agilesekeri.asugar_api.model.request;

import com.agilesekeri.asugar_api.model.entity.EpicEntity;
import com.agilesekeri.asugar_api.model.entity.ProjectEntity;
import com.agilesekeri.asugar_api.model.entity.SprintEntity;
import com.agilesekeri.asugar_api.model.enums.IssueTypeEnum;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class IssueCreateRequest {
    @NotNull
    private final String title;
    @NotNull
    private final ProjectEntity project;
    @NotNull
    private final EpicEntity epic;
    @NotNull
    private final SprintEntity sprint;
    @NotNull
    private final IssueTypeEnum issueType;
}
