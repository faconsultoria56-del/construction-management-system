package com.project.construction.api.controller;

import com.project.construction.api.dto.request.EmployeeRequest;
import com.project.construction.api.dto.response.EmployeeResponse;
import com.project.construction.model.Employee;
import com.project.construction.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        Employee employee = toEntity(employeeRequest);
        return toResponse(employeeService.save(employee));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);
        return ResponseEntity.ok(toResponse(employee));
    }

    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.findAll().stream()
                              .map(this::toResponse)
                              .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeService.findById(id);
        employee.setName(employeeRequest.getName());
        employee.setCpf(employeeRequest.getCpf());
        employee.setRole(employeeRequest.getRole());
        employee.setContractType(employeeRequest.getContractType());
        if (employeeRequest.getPassword() != null && !employeeRequest.getPassword().isEmpty()) {
            employee.setPassword(employeeRequest.getPassword());
        }
        return ResponseEntity.ok(toResponse(employeeService.save(employee)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Employee toEntity(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setCpf(request.getCpf());
        employee.setRole(request.getRole());
        employee.setPassword(request.getPassword());
        employee.setContractType(request.getContractType());
        return employee;
    }

    private EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setCpf(employee.getCpf());
        response.setRole(employee.getRole());
        response.setContractType(employee.getContractType());
        return response;
    }
}
