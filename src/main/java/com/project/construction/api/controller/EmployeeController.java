package com.project.construction.api.controller;

import com.project.construction.api.dto.request.EmployeeRequest;
import com.project.construction.api.dto.response.EmployeeResponse;
import com.project.construction.facade.EmployeeFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeFacade employeeFacade;

    public EmployeeController(EmployeeFacade employeeFacade) {
        this.employeeFacade = employeeFacade;
    }

    @PostMapping
    public EmployeeResponse createEmployee(@RequestBody EmployeeRequest employeeRequest) {
        return employeeFacade.createEmployee(employeeRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeFacade.getEmployeeById(id));
    }

    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {
        return employeeFacade.getAllEmployees();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest employeeRequest) {
        return ResponseEntity.ok(employeeFacade.updateEmployee(id, employeeRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeFacade.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
