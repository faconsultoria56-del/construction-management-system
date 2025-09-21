package com.project.construction.configuration;

import com.project.construction.model.Employee;
import com.project.construction.model.Role;
import com.project.construction.repository.EmployeeRepository;
import com.project.construction.service.EmployeeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner dataLoader(EmployeeRepository employeeRepository, EmployeeService employeeService) {
        return args -> {
            if (employeeRepository.findByCpf("admin").isEmpty()) {
                Employee admin = new Employee();
                admin.setName("Admin User");
                admin.setCpf("admin");
                admin.setPassword("password"); // The service will encode this
                admin.setRole(Role.MANAGER);
                employeeService.save(admin);
            }
        };
    }
}
