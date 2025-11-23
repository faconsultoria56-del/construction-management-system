package com.management.project.dto;

import com.management.project.model.Project;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    private final ModelMapper modelMapper;

    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ProjectResponse toResponse(Project project) {
        ProjectResponse response = modelMapper.map(project, ProjectResponse.class);
        if (project.getCompany() != null) {
            response.setCompanyId(project.getCompany().getId());
        }
        if (project.getOwnerPerson() != null) {
            response.setOwnerPersonId(project.getOwnerPerson().getId());
        }
        if (project.getAddresses() != null && !project.getAddresses().isEmpty()) {
            response.setAddressId(project.getAddresses().get(0).getId());
        }
        return response;
    }

    public List<ProjectResponse> toResponse(List<Project> projects) {
        return projects.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
