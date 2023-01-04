package com.agilesekeri.asugar_api.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class EpicCreateRequest {
    private final String title;
    private final String description;
    private final Integer manHour;
    private final LocalDate due;
    private final String assignedTo;
}
