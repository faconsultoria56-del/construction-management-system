package com.management.projectoccurrence.controller;

import com.management.projectoccurrence.dto.ProjectOccurrenceCreateRequest;
import com.management.projectoccurrence.dto.ProjectOccurrenceResponse;
import com.management.projectoccurrence.dto.ProjectOccurrenceUpdateRequest;
import com.management.projectoccurrence.service.ProjectOccurrenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProjectOccurrenceController {

    private final ProjectOccurrenceService projectOccurrenceService;

    public ProjectOccurrenceController(ProjectOccurrenceService projectOccurrenceService) {
        this.projectOccurrenceService = projectOccurrenceService;
    }

    @PostMapping("/occurrences")
    public ResponseEntity<ProjectOccurrenceResponse> create(@Valid @RequestBody ProjectOccurrenceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectOccurrenceService.create(request));
    }

    @GetMapping("/occurrences/{id}")
    public ResponseEntity<ProjectOccurrenceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectOccurrenceService.findById(id));
    }

    @GetMapping("/projects/{projectId}/occurrences")
    public ResponseEntity<List<ProjectOccurrenceResponse>> listByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectOccurrenceService.listByProject(projectId));
    }

    @PutMapping("/occurrences/{id}")
    public ResponseEntity<ProjectOccurrenceResponse> update(@PathVariable Long id, @Valid @RequestBody ProjectOccurrenceUpdateRequest request) {
        return ResponseEntity.ok(projectOccurrenceService.update(id, request));
    }

    @DeleteMapping("/occurrences/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectOccurrenceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
