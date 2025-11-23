package com.management.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "The request must include either fkCompanyId or fkOwnerPersonId, but not both.")
public class ProjectCreateRequest {

    @NotBlank
    private String name;

    private String description;

    private LocalDate startDate;
    private LocalDate endDate;

    // Empresa (CNPJ)
    private Long companyId;

    // Pessoa física (CPF)
    private Long ownerPersonId;

    private Long addressId;
}
