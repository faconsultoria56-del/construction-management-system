package com.project.construction.service;

import com.project.construction.model.FinanceTransaction;
import java.util.List;
import java.util.Optional;

public interface FinanceService {
    FinanceTransaction save(FinanceTransaction financeTransaction);
    Optional<FinanceTransaction> findById(Long id);
    List<FinanceTransaction> findAll();
    void deleteById(Long id);
}
