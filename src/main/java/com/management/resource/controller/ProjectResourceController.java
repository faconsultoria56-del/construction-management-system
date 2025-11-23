package com.management.resource.controller;

import com.management.resource.dto.ProjectResourceCreateRequest;
import com.management.resource.dto.ProjectResourceUpdateRequest;
import com.management.resource.dto.ProjectResourceResponse;
import com.management.resource.service.ProjectResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Resources")
public class ProjectResourceController {

    private final ProjectResourceService resourceService;

    @PostMapping("/resources")
    @Operation(summary = "Creates a new resource")
    public ResponseEntity<ProjectResourceResponse> create(@Valid @RequestBody ProjectResourceCreateRequest request) {
        ProjectResourceResponse createdResource = resourceService.create(request);
        return new ResponseEntity<>(createdResource, HttpStatus.CREATED);
    }

    @GetMapping("/resources/{id}")
    @Operation(summary = "Finds a resource by ID")
    public ResponseEntity<ProjectResourceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(resourceService.findById(id));
    }

    @GetMapping("/projects/{projectId}/resources")
    @Operation(summary = "Finds all resources for a project")
    public ResponseEntity<List<ProjectResourceResponse>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(resourceService.listByProject(projectId));
    }

    @PutMapping("/resources/{id}")
    @Operation(summary = "Updates a resource")
    public ResponseEntity<ProjectResourceResponse> update(@PathVariable Long id, @Valid @RequestBody ProjectResourceUpdateRequest request) {
        return ResponseEntity.ok(resourceService.update(id, request));
    }

    @DeleteMapping("/resources/{id}")
    @Operation(summary = "Deletes a resource")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        resourceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
