package com.management.project.controller;

import com.management.project.dto.ProjectCreateDTO;
import com.management.project.dto.ProjectResponseDTO;
import com.management.project.service.ProjectService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ProjectResponseDTO> createProject(@RequestBody ProjectCreateDTO createDTO) {
        ProjectResponseDTO createdProject = projectService.createProject(createDTO);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }
}
