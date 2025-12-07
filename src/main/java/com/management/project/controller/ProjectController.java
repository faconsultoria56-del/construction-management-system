package com.management.project.controller;

import com.management.project.dto.ProjectCreateRequest;
import com.management.project.dto.ProjectFinancialSummaryDTO;
import com.management.project.dto.ProjectResponse;
import com.management.project.service.ProjectFinancialService;
import com.management.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Projects")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectFinancialService projectFinancialService;

    public ProjectController(ProjectService projectService, ProjectFinancialService projectFinancialService) {
        this.projectService = projectService;
        this.projectFinancialService = projectFinancialService;
    }

    @PostMapping("/projects")
    @Operation(summary = "Creates a new project (CPF or CNPJ)")
    public ResponseEntity<ProjectResponse> create(@RequestBody @Valid ProjectCreateRequest request) {
        return ResponseEntity.ok(projectService.create(request));
    }

    @GetMapping("/projects")
    @Operation(summary = "Finds all projects")
    public ResponseEntity<Page<ProjectResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(projectService.findAll(pageable));
    }

    @GetMapping("/projects/{id}")
    @Operation(summary = "Finds a project by ID")
    public ResponseEntity<ProjectResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @GetMapping("/companies/{companyId}/projects")
    public ResponseEntity<List<ProjectResponse>> findByCompanyId(@PathVariable Integer companyId) {
        return ResponseEntity.ok(projectService.findByCompanyId(companyId));
    }

    @GetMapping("/persons/{personId}/projects")
    public ResponseEntity<List<ProjectResponse>> findByPersonId(@PathVariable Integer personId) {
        return ResponseEntity.ok(projectService.findByPersonId(personId));
    }

    @GetMapping("/projects/{projectId}/financial-summary")
    @Operation(summary = "Gets the financial summary for a project")
    public ResponseEntity<ProjectFinancialSummaryDTO> getFinancialSummary(@PathVariable Integer projectId) {
        return ResponseEntity.ok(projectFinancialService.getProjectFinancialSummary(projectId));
    }
}
