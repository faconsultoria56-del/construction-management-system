package com.project.construction.api.controller;

import com.project.construction.model.FinanceTransaction;
import com.project.construction.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/finance")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @PostMapping("/transactions")
    public FinanceTransaction createTransaction(@RequestBody FinanceTransaction transaction) {
        return financeService.save(transaction);
    }

    @GetMapping("/transactions")
    public List<FinanceTransaction> getAllTransactions() {
        return financeService.findAll();
    }

    @PostMapping("/transactions/{id}/approve")
    public ResponseEntity<FinanceTransaction> approveTransaction(@PathVariable Long id) {
        Optional<FinanceTransaction> transactionOptional = financeService.findById(id);
        if (transactionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        FinanceTransaction transaction = transactionOptional.get();
        transaction.setStatus("APPROVED");
        return ResponseEntity.ok(financeService.save(transaction));
    }

    @PostMapping("/transactions/{id}/release")
    public ResponseEntity<FinanceTransaction> releaseTransaction(@PathVariable Long id) {
        Optional<FinanceTransaction> transactionOptional = financeService.findById(id);
        if (transactionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        FinanceTransaction transaction = transactionOptional.get();
        transaction.setStatus("RELEASED");
        return ResponseEntity.ok(financeService.save(transaction));
    }
}
