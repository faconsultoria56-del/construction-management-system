package com.management.resource.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProjectResourceCreateRequest {

    @NotNull(message = "O ID do projeto é obrigatório")
    private Long projectId;

    @NotBlank(message = "O nome do recurso é obrigatório")
    private String name;

    private String unit;

    @PositiveOrZero(message = "A quantidade não pode ser negativa")
    private BigDecimal quantity;
}
