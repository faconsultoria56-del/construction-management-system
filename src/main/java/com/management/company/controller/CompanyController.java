package com.management.company.controller;

import com.management.person.dto.PersonDTO;
import com.management.person.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@Tag(name = "Organizational")
public class CompanyController {

    private final PersonService personService;

    public CompanyController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{companyId}/persons")
    @Operation(summary = "Finds all persons for a company")
    public ResponseEntity<List<PersonDTO>> findPersonsByCompanyId(@PathVariable Integer companyId) {
        return ResponseEntity.ok(personService.findPersonsByCompanyId(companyId));
    }
}
