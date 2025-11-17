package com.management.task.controller;

import com.management.task.dto.ProjectTaskCreateRequest;
import com.management.task.dto.ProjectTaskUpdateRequest;
import com.management.task.dto.ProjectTaskResponse;
import com.management.task.service.ProjectTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectTaskController {

    private final ProjectTaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<ProjectTaskResponse> createTask(@Valid @RequestBody ProjectTaskCreateRequest request) {
        ProjectTaskResponse createdTask = taskService.createTask(request);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<ProjectTaskResponse> getTaskById(@PathVariable Integer id) {
        ProjectTaskResponse task = taskService.findTaskById(id);
        return ResponseEntity.ok(task);
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<ProjectTaskResponse>> getTasksByProject(@PathVariable Integer projectId) {
        List<ProjectTaskResponse> tasks = taskService.listByProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<ProjectTaskResponse> updateTask(@PathVariable Integer id, @Valid @RequestBody ProjectTaskUpdateRequest request) {
        ProjectTaskResponse updatedTask = taskService.updateTask(id, request);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Integer id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
