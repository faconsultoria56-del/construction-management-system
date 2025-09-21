package com.project.construction.facade;

import com.project.construction.api.dto.request.FinanceTransactionRequest;
import com.project.construction.api.dto.response.EmployeeResponse;
import com.project.construction.api.dto.response.FinanceTransactionResponse;
import com.project.construction.model.Employee;
import com.project.construction.model.FinanceTransaction;
import com.project.construction.service.EmployeeService;
import com.project.construction.service.FinanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceFacade {

    private final FinanceService financeService;
    private final EmployeeService employeeService;

    public FinanceFacade(FinanceService financeService, EmployeeService employeeService) {
        this.financeService = financeService;
        this.employeeService = employeeService;
    }

    @Transactional
    public FinanceTransactionResponse createTransaction(FinanceTransactionRequest transactionRequest) {
        FinanceTransaction transaction = toEntity(transactionRequest);
        return toResponse(financeService.save(transaction));
    }

    @Transactional(readOnly = true)
    public List<FinanceTransactionResponse> getAllTransactions() {
        return financeService.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FinanceTransactionResponse approveTransaction(Long id) {
        FinanceTransaction transaction = financeService.findById(id);
        transaction.setStatus("APPROVED");
        return toResponse(financeService.save(transaction));
    }

    @Transactional
    public FinanceTransactionResponse releaseTransaction(Long id) {
        FinanceTransaction transaction = financeService.findById(id);
        transaction.setStatus("RELEASED");
        return toResponse(financeService.save(transaction));
    }

    private FinanceTransaction toEntity(FinanceTransactionRequest request) {
        FinanceTransaction transaction = new FinanceTransaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setStatus(request.getStatus());

        Employee employee = employeeService.findById(request.getEmployeeId());
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
