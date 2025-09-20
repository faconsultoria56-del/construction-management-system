package com.project.construction.service.impl;

import com.project.construction.exception.ResourceNotFoundException;
import com.project.construction.model.FinanceTransaction;
import com.project.construction.repository.FinanceTransactionRepository;
import com.project.construction.service.FinanceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FinanceServiceImpl implements FinanceService {

    private final FinanceTransactionRepository financeTransactionRepository;

    public FinanceServiceImpl(FinanceTransactionRepository financeTransactionRepository) {
        this.financeTransactionRepository = financeTransactionRepository;
    }

    @Override
    public FinanceTransaction save(FinanceTransaction financeTransaction) {
        return financeTransactionRepository.save(financeTransaction);
    }

    @Override
    public FinanceTransaction findById(Long id) {
        return financeTransactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FinanceTransaction not found with id: " + id));
    }

    @Override
    public List<FinanceTransaction> findAll() {
        return financeTransactionRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        financeTransactionRepository.deleteById(id);
    }
}
