package com.management.projectoccurrence.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectOccurrenceResponse {

    private Integer id;
    private Integer projectId;
    private Integer personId;
    private LocalDate occurrenceDate;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
