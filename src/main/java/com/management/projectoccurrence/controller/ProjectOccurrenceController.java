package com.management.projectoccurrence.controller;

import com.management.projectoccurrence.dto.ProjectOccurrenceCreateRequest;
import com.management.projectoccurrence.dto.ProjectOccurrenceResponse;
import com.management.projectoccurrence.dto.ProjectOccurrenceUpdateRequest;
import com.management.projectoccurrence.service.ProjectOccurrenceService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Rastreability")
public class ProjectOccurrenceController {

    private final ProjectOccurrenceService projectOccurrenceService;

    public ProjectOccurrenceController(ProjectOccurrenceService projectOccurrenceService) {
        this.projectOccurrenceService = projectOccurrenceService;
    }

    @PostMapping("/occurrences")
    @Operation(summary = "Registers a new occurrence")
    public ResponseEntity<ProjectOccurrenceResponse> create(@Valid @RequestBody ProjectOccurrenceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectOccurrenceService.create(request));
    }

    @GetMapping("/occurrences/{id}")
    @Operation(summary = "Finds an occurrence by ID")
    public ResponseEntity<ProjectOccurrenceResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(projectOccurrenceService.findById(id));
    }

    @GetMapping("/projects/{projectId}/occurrences")
    @Operation(summary = "Finds all occurrences for a project")
    public ResponseEntity<List<ProjectOccurrenceResponse>> listByProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(projectOccurrenceService.listByProject(projectId));
    }

    @PutMapping("/occurrences/{id}")
    @Operation(summary = "Updates an occurrence")
    public ResponseEntity<ProjectOccurrenceResponse> update(@PathVariable Integer id, @Valid @RequestBody ProjectOccurrenceUpdateRequest request) {
        return ResponseEntity.ok(projectOccurrenceService.update(id, request));
    }

    @DeleteMapping("/occurrences/{id}")
    @Operation(summary = "Deletes an occurrence")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        projectOccurrenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
