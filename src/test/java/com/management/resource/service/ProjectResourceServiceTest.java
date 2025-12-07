package com.management.resource.service;

import com.management.project.model.Project;
import com.management.project.repository.ProjectRepository;
import com.management.resource.dto.ProjectResourceCreateRequest;
import com.management.resource.dto.ProjectResourceResponse;
import com.management.resource.mapper.ProjectResourceMapper;
import com.management.resource.model.ProjectResource;
import com.management.resource.repository.ProjectResourceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectResourceServiceTest {

    @Mock
    private ProjectResourceRepository resourceRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectResourceMapper resourceMapper;

    @InjectMocks
    private ProjectResourceService resourceService;

    @Test
    void create_shouldSucceed() {
        // Arrange
        ProjectResourceCreateRequest request = new ProjectResourceCreateRequest();
        request.setName("New Resource");
        request.setProjectId(1);

        Project project = new Project();
        ProjectResource resource = new ProjectResource();
        ProjectResourceResponse response = new ProjectResourceResponse();

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(resourceRepository.save(any(ProjectResource.class))).thenReturn(resource);
        when(resourceMapper.toResponse(resource)).thenReturn(response);

        // Act
        ProjectResourceResponse result = resourceService.create(request);

        // Assert
        assertNotNull(result);
        verify(resourceRepository, times(1)).save(any(ProjectResource.class));
    }
}
