package com.agilesekeri.asugar_api.model.request;

import com.agilesekeri.asugar_api.model.entity.AppUserEntity;
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

    private final String description;

//    @NotNull
//    private final Long projectId;

    private final Long epicId;

    private final String sprint;

    private final int manHour;

    @NotNull
    private final IssueTypeEnum issueType;
}
