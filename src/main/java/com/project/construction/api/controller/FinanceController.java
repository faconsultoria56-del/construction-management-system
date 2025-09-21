package com.project.construction.api.controller;

import com.project.construction.api.dto.request.FinanceTransactionRequest;
import com.project.construction.api.dto.response.FinanceTransactionResponse;
import com.project.construction.facade.FinanceFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance")
public class FinanceController {

    private final FinanceFacade financeFacade;

    public FinanceController(FinanceFacade financeFacade) {
        this.financeFacade = financeFacade;
    }

    @PostMapping("/transactions")
    public FinanceTransactionResponse createTransaction(@RequestBody FinanceTransactionRequest transactionRequest) {
        return financeFacade.createTransaction(transactionRequest);
    }

    @GetMapping("/transactions")
    public List<FinanceTransactionResponse> getAllTransactions() {
        return financeFacade.getAllTransactions();
    }

    @PostMapping("/transactions/{id}/approve")
    public ResponseEntity<FinanceTransactionResponse> approveTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(financeFacade.approveTransaction(id));
    }

    @PostMapping("/transactions/{id}/release")
    public ResponseEntity<FinanceTransactionResponse> releaseTransaction(@PathVariable Long id) {
        return ResponseEntity.ok(financeFacade.releaseTransaction(id));
    }
}
