package com.management.project.service;

import com.management.financial.repository.FinancialEntryRepository;
import com.management.project.dto.ProjectFinancialSummaryDTO;
import com.management.resource.repository.ProjectResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ProjectFinancialService {

    @Autowired
    private ProjectResourceRepository projectResourceRepository;

    @Autowired
    private FinancialEntryRepository financialEntryRepository;

    @Transactional(readOnly = true)
    public ProjectFinancialSummaryDTO getProjectFinancialSummary(Integer projectId) {
        BigDecimal resourceBudget = Optional.ofNullable(projectResourceRepository.sumTotalCostByProjectId(projectId))
                .orElse(BigDecimal.ZERO);

        BigDecimal expenses = Optional.ofNullable(financialEntryRepository.sumAmountByProjectIdAndType(projectId, "DESPESA"))
                .orElse(BigDecimal.ZERO);

        BigDecimal revenues = Optional.ofNullable(financialEntryRepository.sumAmountByProjectIdAndType(projectId, "RECEITA"))
                .orElse(BigDecimal.ZERO);

        BigDecimal profitOrLoss = revenues.subtract(expenses);

        return new ProjectFinancialSummaryDTO(resourceBudget, expenses, revenues, profitOrLoss);
    }
}
