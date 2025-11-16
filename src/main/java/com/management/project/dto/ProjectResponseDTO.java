package com.management.project.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectResponseDTO {
    private Integer id;
    private String name;
    private String description;
    private Integer companyId;
    private Integer ownerPersonId;
    private Integer addressId;
    private LocalDate startDate;
    private LocalDate endDate;
}
