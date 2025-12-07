package com.management.resource.mapper;

import com.management.resource.dto.ProjectResourceResponse;
import com.management.resource.model.ProjectResource;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectResourceMapper {

    private final ModelMapper modelMapper;

    public ProjectResourceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProjectResourceResponse toResponse(ProjectResource resource) {
        ProjectResourceResponse response = modelMapper.map(resource, ProjectResourceResponse.class);
        if (resource.getProject() != null) {
            response.setProjectId(resource.getProject().getId());
        }
        return response;
    }
}
