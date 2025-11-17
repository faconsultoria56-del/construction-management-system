package com.management.project.dto;

import com.management.project.model.Project;

public class ProjectMapper {

    public static ProjectResponse toResponse(Project project) {

        ProjectResponse response = new ProjectResponse();

        response.setId(project.getId());
        response.setName(project.getName());
        response.setDescription(project.getDescription());
        response.setStartDate(project.getStartDate());
        response.setEndDate(project.getEndDate());

        response.setCompanyId(
            project.getCompany() != null ? project.getCompany().getId() : null
        );
        response.setOwnerPersonId(
            project.getOwnerPerson() != null ? project.getOwnerPerson().getId() : null
        );

        response.setAddressId(
            project.getAddress() != null ? project.getAddress().getId() : null
        );

        response.setCreatedAt(project.getCreatedAt());
        response.setUpdatedAt(project.getUpdatedAt());

        return response;
    }
}
