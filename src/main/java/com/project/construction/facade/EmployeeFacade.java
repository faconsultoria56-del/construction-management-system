package com.project.construction.facade;

import com.project.construction.api.dto.request.EmployeeRequest;
import com.project.construction.api.dto.response.EmployeeResponse;
import com.project.construction.model.Employee;
import com.project.construction.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeFacade {

    private final EmployeeService employeeService;

    public EmployeeFacade(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        Employee employee = toEntity(employeeRequest);
        return toResponse(employeeService.save(employee));
    }

    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        return toResponse(employeeService.findById(id));
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        return employeeService.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        Employee employee = employeeService.findById(id);
        employee.setName(employeeRequest.getName());
        employee.setCpf(employeeRequest.getCpf());
        employee.setRole(employeeRequest.getRole());
        employee.setContractType(employeeRequest.getContractType());
        if (employeeRequest.getPassword() != null && !employeeRequest.getPassword().isEmpty()) {
            employee.setPassword(employeeRequest.getPassword());
        }
        return toResponse(employeeService.save(employee));
    }

    @Transactional
    public void deleteEmployee(Long id) {
        employeeService.deleteById(id);
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
