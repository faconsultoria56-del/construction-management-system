package com.management.projectoccurrence.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectOccurrenceCreateRequest {

    @NotNull
    private Long projectId;

    @NotNull
    private Long personId;

    @NotNull
    private LocalDate occurrenceDate;

    @NotBlank
    private String description;
}
