package com.management.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFinancialSummaryDTO {

    private BigDecimal resourceBudget;
    private BigDecimal actualCost;
    private BigDecimal revenue;
    private BigDecimal profitOrLoss;
}
