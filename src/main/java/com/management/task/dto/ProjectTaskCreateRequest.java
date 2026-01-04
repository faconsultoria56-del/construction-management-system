package com.management.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectTaskCreateRequest {

    @NotBlank(message = "O título é obrigatório")
    private String title;

    private String description;

    private LocalDate dueDate;

    @NotNull(message = "O ID do projeto é obrigatório")
    private Long projectId;

    private Long responsibleId;

    private Long parentId;
}
