package com.management.task.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectTaskResponse {
    private Integer id;
    private String title;
    private String description;
    private String status;
    private LocalDate dueDate;
    private Integer projectId;
    private Integer responsibleId;
    private Integer parentId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
