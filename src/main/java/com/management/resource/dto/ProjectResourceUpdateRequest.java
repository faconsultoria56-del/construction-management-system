package com.management.resource.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProjectResourceUpdateRequest {

    private String name;

    private String unit;

    @PositiveOrZero(message = "A quantidade não pode ser negativa")
    private BigDecimal quantity;
}
