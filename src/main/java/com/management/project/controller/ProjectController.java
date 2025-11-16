package com.management.project.controller;

import com.management.project.dto.ProjectCreateRequest;
import com.management.project.dto.ProjectResponse;
import com.management.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@RequestBody @Valid ProjectCreateRequest request) {
        return ResponseEntity.ok(projectService.create(request));
    }
}
