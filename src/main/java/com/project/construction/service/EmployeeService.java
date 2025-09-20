package com.project.construction.service;

import com.project.construction.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee save(Employee employee);
    Optional<Employee> findById(Long id);
    List<Employee> findAll();
    void deleteById(Long id);
}
