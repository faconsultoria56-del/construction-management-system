package com.management.company.controller;

import com.management.person.dto.PersonDTO;
import com.management.person.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final PersonService personService;

    public CompanyController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/{companyId}/persons")
    public ResponseEntity<List<PersonDTO>> findPersonsByCompanyId(@PathVariable Integer companyId) {
        return ResponseEntity.ok(personService.findPersonsByCompanyId(companyId));
    }
}
