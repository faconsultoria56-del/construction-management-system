package com.project.construction.api.controller;

import com.project.construction.api.dto.request.FinanceTransactionRequest;
import com.project.construction.api.dto.response.EmployeeResponse;
import com.project.construction.api.dto.response.FinanceTransactionResponse;
import com.project.construction.model.Employee;
import com.project.construction.model.FinanceTransaction;
import com.project.construction.service.EmployeeService;
import com.project.construction.service.FinanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/finance")
public class FinanceController {

    private final FinanceService financeService;
    private final EmployeeService employeeService;

    public FinanceController(FinanceService financeService, EmployeeService employeeService) {
        this.financeService = financeService;
        this.employeeService = employeeService;
    }

    @PostMapping("/transactions")
    public FinanceTransactionResponse createTransaction(@RequestBody FinanceTransactionRequest transactionRequest) {
        FinanceTransaction transaction = toEntity(transactionRequest);
        return toResponse(financeService.save(transaction));
    }

    @GetMapping("/transactions")
    public List<FinanceTransactionResponse> getAllTransactions() {
        return financeService.findAll().stream()
                              .map(this::toResponse)
                              .collect(Collectors.toList());
    }

    @PostMapping("/transactions/{id}/approve")
    public ResponseEntity<FinanceTransactionResponse> approveTransaction(@PathVariable Long id) {
        Optional<FinanceTransaction> transactionOptional = financeService.findById(id);
        if (transactionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        FinanceTransaction transaction = transactionOptional.get();
        transaction.setStatus("APPROVED");
        return ResponseEntity.ok(toResponse(financeService.save(transaction)));
    }

    @PostMapping("/transactions/{id}/release")
    public ResponseEntity<FinanceTransactionResponse> releaseTransaction(@PathVariable Long id) {
        Optional<FinanceTransaction> transactionOptional = financeService.findById(id);
        if (transactionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        FinanceTransaction transaction = transactionOptional.get();
        transaction.setStatus("RELEASED");
        return ResponseEntity.ok(toResponse(financeService.save(transaction)));
    }

    private FinanceTransaction toEntity(FinanceTransactionRequest request) {
        FinanceTransaction transaction = new FinanceTransaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setStatus(request.getStatus());

        Employee employee = employeeService.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.getEmployeeId()));
        transaction.setEmployee(employee);

        return transaction;
    }

    private FinanceTransactionResponse toResponse(FinanceTransaction transaction) {
        FinanceTransactionResponse response = new FinanceTransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setDate(transaction.getDate());
        response.setStatus(transaction.getStatus());
        response.setEmployee(toEmployeeResponse(transaction.getEmployee()));
        return response;
    }

    private EmployeeResponse toEmployeeResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setCpf(employee.getCpf());
        response.setRole(employee.getRole());
        response.setContractType(employee.getContractType());
        return response;
    }
}
