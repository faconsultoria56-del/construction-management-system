package com.management.employee.service;

import com.management.config.UserPrincipal;
import com.management.employee.dto.EmployeeRequestDTO;
import com.management.employee.dto.EmployeeResponseDTO;
import com.management.employee.model.Employee;
import com.management.employee.repository.EmployeeRepository;
import com.management.exception.ResourceNotFoundException;
import com.management.person.model.Person;
import com.management.role.model.Role;
import com.management.role.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
        // 1. Pega o dono logado via Token JWT
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        Person owner = principal.getUserAccount().getPerson();

        // 2. Busca a Role (Worker, Manager, etc)
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + dto.getRoleId()));

        // 3. Monta a entidade
        Employee employee = new Employee();
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setSalary(dto.getSalary());
        employee.setRole(role);
        employee.setOwner(owner); // Vínculo de segurança
        employee.setActive(true);

        Employee savedEmployee = employeeRepository.save(employee);

        return mapToDTO(savedEmployee);
    }

    private EmployeeResponseDTO mapToDTO(Employee employee) {
        EmployeeResponseDTO dto = modelMapper.map(employee, EmployeeResponseDTO.class);
        dto.setRoleName(employee.getRole().getName());
        dto.setOwnerId(employee.getOwner().getId());
        return dto;
    }
}
