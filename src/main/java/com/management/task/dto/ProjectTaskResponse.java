package com.management.task.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectTaskResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
    private Long projectId;
    private Long responsibleId;
    private Long parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
