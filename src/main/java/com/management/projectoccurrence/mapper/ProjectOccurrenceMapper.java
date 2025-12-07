package com.management.projectoccurrence.mapper;

import com.management.projectoccurrence.dto.ProjectOccurrenceCreateRequest;
import com.management.projectoccurrence.dto.ProjectOccurrenceResponse;
import com.management.projectoccurrence.model.ProjectOccurrence;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class ProjectOccurrenceMapper {

    private final ModelMapper modelMapper;

    public ProjectOccurrenceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
     @PostConstruct
    public void configureMappings() {
        modelMapper.addMappings(new PropertyMap<ProjectOccurrenceCreateRequest, ProjectOccurrence>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
    }

    public ProjectOccurrence toEntity(ProjectOccurrenceCreateRequest request) {
        return modelMapper.map(request, ProjectOccurrence.class);
    }

    public ProjectOccurrenceResponse toResponse(ProjectOccurrence entity) {
        ProjectOccurrenceResponse response = modelMapper.map(entity, ProjectOccurrenceResponse.class);
        response.setProjectId(entity.getProject().getId());
        response.setPersonId(entity.getPerson().getId());
        return response;
    }
}
