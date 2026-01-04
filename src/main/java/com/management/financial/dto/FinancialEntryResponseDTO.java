package com.management.financial.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FinancialEntryResponseDTO {
    private Long id;
    private String type;
    private String description;
    private BigDecimal amount;
    private LocalDate entryDate;
    private Boolean isPaid;
    private LocalDate dueDate;
    private Long projectId;
    private String projectName;
    private Long costCenterId;
    private String costCenterName;
    private Long personId;
    private String personName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
