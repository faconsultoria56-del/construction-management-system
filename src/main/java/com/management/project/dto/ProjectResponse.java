package com.management.project.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long companyId;
    private Long ownerPersonId;
    private Long addressId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
