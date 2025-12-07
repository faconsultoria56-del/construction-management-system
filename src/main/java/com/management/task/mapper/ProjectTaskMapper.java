package com.management.task.mapper;

import com.management.task.dto.ProjectTaskResponse;
import com.management.task.model.ProjectTask;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectTaskMapper {

    private final ModelMapper modelMapper;

    public ProjectTaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProjectTaskResponse toResponse(ProjectTask projectTask) {
        ProjectTaskResponse response = modelMapper.map(projectTask, ProjectTaskResponse.class);
        if (projectTask.getProject() != null) {
            response.setProjectId(projectTask.getProject().getId());
        }
        if (projectTask.getResponsible() != null) {
            response.setResponsibleId(projectTask.getResponsible().getId());
        }
        if (projectTask.getParent() != null) {
            response.setParentId(projectTask.getParent().getId());
        }
        return response;
    }
}
