package com.management.financial.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FinancialEntryResponseDTO {
    private Integer id;
    private String type;
    private String description;
    private BigDecimal amount;
    private LocalDate entryDate;
    private Boolean isPaid;
    private LocalDate dueDate;
    private Integer projectId;
    private String projectName;
    private Integer costCenterId;
    private String costCenterName;
    private Integer personId;
    private String personName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
