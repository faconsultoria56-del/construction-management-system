package com.management.financial.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FinancialEntryDTO {
    private Integer id;
    private String type;
    private String description;
    private BigDecimal amount;
    private LocalDate entryDate;
    private Boolean isPaid;
    private LocalDate dueDate;
    private Integer projectId;
    private Integer costCenterId;
    private Integer personId;
}
