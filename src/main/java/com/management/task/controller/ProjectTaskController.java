package com.management.task.controller;

import com.management.task.dto.ProjectTaskCreateRequest;
import com.management.task.dto.ProjectTaskUpdateRequest;
import com.management.task.dto.ProjectTaskResponse;
import com.management.task.service.ProjectTaskService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tasks")
public class ProjectTaskController {

    private final ProjectTaskService taskService;

    @PostMapping("/tasks")
    @Operation(summary = "Creates a new task")
    public ResponseEntity<ProjectTaskResponse> createTask(@Valid @RequestBody ProjectTaskCreateRequest request) {
        ProjectTaskResponse createdTask = taskService.createTask(request);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{id}")
    @Operation(summary = "Finds a task by ID")
    public ResponseEntity<ProjectTaskResponse> getTaskById(@PathVariable Long id) {
        ProjectTaskResponse task = taskService.findTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/projects/{projectId}/tasks")
    @Operation(summary = "Finds all tasks for a project")
    public ResponseEntity<List<ProjectTaskResponse>> getTasksByProject(@PathVariable Long projectId) {
        List<ProjectTaskResponse> tasks = taskService.listByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/tasks/{id}")
    @Operation(summary = "Updates a task")
    public ResponseEntity<ProjectTaskResponse> updateTask(@PathVariable Long id, @Valid @RequestBody ProjectTaskUpdateRequest request) {
        ProjectTaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/tasks/{id}")
    @Operation(summary = "Deletes a task")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
