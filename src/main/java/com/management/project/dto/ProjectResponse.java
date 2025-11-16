package com.management.project.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectResponse {
    private Integer id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer companyId;
    private Integer ownerPersonId;
    private Integer addressId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
