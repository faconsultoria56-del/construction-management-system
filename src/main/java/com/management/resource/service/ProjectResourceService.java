package com.management.resource.service;

import com.management.project.model.Project;
import com.management.project.repository.ProjectRepository;
import com.management.project.exception.ResourceNotFoundException;
import com.management.resource.dto.ProjectResourceCreateRequest;
import com.management.resource.dto.ProjectResourceUpdateRequest;
import com.management.resource.dto.ProjectResourceResponse;
import com.management.resource.mapper.ProjectResourceMapper;
import com.management.resource.model.ProjectResource;
import com.management.resource.repository.ProjectResourceRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectResourceService {

    private final ProjectResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private final ProjectResourceMapper resourceMapper;

    @Transactional
    public ProjectResourceResponse create(@Valid ProjectResourceCreateRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o id: " + request.getProjectId()));

        ProjectResource resource = new ProjectResource();
        resource.setProject(project);
        resource.setName(request.getName());
        resource.setUnit(request.getUnit());
        resource.setQuantity(request.getQuantity());

        ProjectResource savedResource = resourceRepository.save(resource);
        return resourceMapper.toResponse(savedResource);
    }

    @Transactional
    public ProjectResourceResponse update(Integer id, @Valid ProjectResourceUpdateRequest request) {
        ProjectResource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado com o id: " + id));

        if (request.getName() != null) {
            resource.setName(request.getName());
        }
        if (request.getUnit() != null) {
            resource.setUnit(request.getUnit());
        }
        if (request.getQuantity() != null) {
            resource.setQuantity(request.getQuantity());
        }

        ProjectResource updatedResource = resourceRepository.save(resource);
        return resourceMapper.toResponse(updatedResource);
    }

    @Transactional
    public void delete(Integer id) {
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado com o id: " + id);
        }
        resourceRepository.deleteById(id);
    }

    public ProjectResourceResponse findById(Integer id) {
        ProjectResource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado com o id: " + id));
        return resourceMapper.toResponse(resource);
    }

    public List<ProjectResourceResponse> listByProject(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Projeto não encontrado com o id: " + projectId);
        }
        return resourceRepository.findByProjectId(projectId).stream()
                .map(resourceMapper::toResponse)
                .collect(Collectors.toList());
    }
}
