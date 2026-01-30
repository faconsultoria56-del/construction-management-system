package com.management.financial.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FinancialEntryRequestDTO {
    private String type;
    private String description;
    private BigDecimal amount;
    private LocalDate entryDate;
    private Boolean isPaid;
    private LocalDate dueDate;
    private Long projectId;
    private Long costCenterId;
    private Long personId;
}
