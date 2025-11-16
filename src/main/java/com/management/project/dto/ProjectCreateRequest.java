package com.management.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectCreateRequest {

    @NotBlank
    private String name;

    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    // Empresa (CNPJ)
    private Integer companyId;

    // Pessoa física (CPF)
    private Integer ownerPersonId;

    private Integer addressId;
}
