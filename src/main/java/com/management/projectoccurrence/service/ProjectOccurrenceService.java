package com.management.projectoccurrence.service;

import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.project.exception.ResourceNotFoundException;
import com.management.project.model.Project;
import com.management.project.repository.ProjectRepository;
import com.management.projectoccurrence.dto.ProjectOccurrenceCreateRequest;
import com.management.projectoccurrence.dto.ProjectOccurrenceResponse;
import com.management.projectoccurrence.dto.ProjectOccurrenceUpdateRequest;
import com.management.projectoccurrence.mapper.ProjectOccurrenceMapper;
import com.management.projectoccurrence.model.ProjectOccurrence;
import com.management.projectoccurrence.repository.ProjectOccurrenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectOccurrenceService {

    private final ProjectOccurrenceRepository projectOccurrenceRepository;
    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    private final ProjectOccurrenceMapper projectOccurrenceMapper;

    public ProjectOccurrenceService(ProjectOccurrenceRepository projectOccurrenceRepository,
                                    ProjectRepository projectRepository,
                                    PersonRepository personRepository,
                                    ProjectOccurrenceMapper projectOccurrenceMapper) {
        this.projectOccurrenceRepository = projectOccurrenceRepository;
        this.projectRepository = projectRepository;
        this.personRepository = personRepository;
        this.projectOccurrenceMapper = projectOccurrenceMapper;
    }

    @Transactional
    public ProjectOccurrenceResponse create(ProjectOccurrenceCreateRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + request.getProjectId()));
        Person person = personRepository.findById(request.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException("Person not found with id: " + request.getPersonId()));

        ProjectOccurrence occurrence = projectOccurrenceMapper.toEntity(request);
        occurrence.setProject(project);
        occurrence.setPerson(person);

        return projectOccurrenceMapper.toResponse(projectOccurrenceRepository.save(occurrence));
    }

    @Transactional
    public ProjectOccurrenceResponse update(Long id, ProjectOccurrenceUpdateRequest request) {
        ProjectOccurrence occurrence = projectOccurrenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Occurrence not found with id: " + id));

        if (request.getOccurrenceDate() != null) {
            occurrence.setOccurrenceDate(request.getOccurrenceDate());
        }
        if (request.getDescription() != null) {
            occurrence.setDescription(request.getDescription());
        }

        return projectOccurrenceMapper.toResponse(projectOccurrenceRepository.save(occurrence));
    }

    @Transactional
    public void delete(Long id) {
        if (!projectOccurrenceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Occurrence not found with id: " + id);
        }
        projectOccurrenceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProjectOccurrenceResponse> listByProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }
        return projectOccurrenceRepository.findByProjectId(projectId).stream()
                .map(projectOccurrenceMapper::toResponse)
                .collect(Collectors.toList());
    }
     @Transactional(readOnly = true)
    public ProjectOccurrenceResponse findById(Long id) {
        ProjectOccurrence occurrence = projectOccurrenceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Occurrence not found with id: " + id));
        return projectOccurrenceMapper.toResponse(occurrence);
    }
}
