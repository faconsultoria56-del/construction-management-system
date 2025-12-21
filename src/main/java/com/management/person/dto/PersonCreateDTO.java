package com.management.person.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PersonCreateDTO {
    private String fullName;
    private String document;
    private Integer documentType;
    private String password;
    private String email;
    private LocalDate birthDate;
    private String cnpj;
}
