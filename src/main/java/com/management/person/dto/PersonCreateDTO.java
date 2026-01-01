package com.management.person.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonCreateDTO {
    private String fullName;
    private String document;
    private String documentType; // CPF, RG, etc.
    private String email;
    private String password;
    private LocalDate birthDate;
    private String cnpj;
}
