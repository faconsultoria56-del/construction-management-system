package com.management.projectoccurrence.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectOccurrenceResponse {

    private Long id;
    private Long projectId;
    private Long personId;
    private LocalDate occurrenceDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
