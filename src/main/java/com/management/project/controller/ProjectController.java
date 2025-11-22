package com.management.project.controller;

import com.management.project.dto.ProjectCreateRequest;
import com.management.project.dto.ProjectResponse;
import com.management.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> create(@RequestBody @Valid ProjectCreateRequest request) {
        return ResponseEntity.ok(projectService.create(request));
    }

    @GetMapping("/projects")
    public ResponseEntity<Page<ProjectResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(projectService.findAll(pageable));
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<ProjectResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @GetMapping("/companies/{companyId}/projects")
    public ResponseEntity<List<ProjectResponse>> findByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(projectService.findByCompanyId(companyId));
    }

    @GetMapping("/persons/{personId}/projects")
    public ResponseEntity<List<ProjectResponse>> findByPersonId(@PathVariable Long personId) {
        return ResponseEntity.ok(projectService.findByPersonId(personId));
    }
}
