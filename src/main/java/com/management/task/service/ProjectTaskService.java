package com.management.task.service;

import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.project.model.Project;
import com.management.project.repository.ProjectRepository;
import com.management.project.exception.ResourceNotFoundException;
import com.management.task.dto.ProjectTaskCreateRequest;
import com.management.task.dto.ProjectTaskUpdateRequest;
import com.management.task.dto.ProjectTaskResponse;
import com.management.task.mapper.ProjectTaskMapper;
import com.management.task.model.ProjectTask;
import com.management.task.repository.ProjectTaskRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectTaskService {

    private final ProjectTaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final PersonRepository personRepository;
    private final ProjectTaskMapper taskMapper;

    @Transactional
    public ProjectTaskResponse createTask(@Valid ProjectTaskCreateRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Projeto não encontrado com o id: " + request.getProjectId()));

        ProjectTask task = new ProjectTask();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setProject(project);

        if (request.getResponsibleId() != null) {
            Person responsible = personRepository.findById(request.getResponsibleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pessoa responsável não encontrada com o id: " + request.getResponsibleId()));
            task.setResponsible(responsible);
        }

        if (request.getParentId() != null) {
            ProjectTask parentTask = taskRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Tarefa pai não encontrada com o id: " + request.getParentId()));
            task.setParent(parentTask);
        }

        ProjectTask savedTask = taskRepository.save(task);
        return taskMapper.toResponse(savedTask);
    }

    @Transactional
    public ProjectTaskResponse updateTask(Integer id, @Valid ProjectTaskUpdateRequest request) {
        ProjectTask task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o id: " + id));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }
        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }
        if (request.getResponsibleId() != null) {
            Person responsible = personRepository.findById(request.getResponsibleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pessoa responsável não encontrada com o id: " + request.getResponsibleId()));
            task.setResponsible(responsible);
        }

        ProjectTask updatedTask = taskRepository.save(task);
        return taskMapper.toResponse(updatedTask);
    }

    public List<ProjectTaskResponse> listByProject(Integer projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Projeto não encontrado com o id: " + projectId);
        }
        return taskRepository.findByProjectId(projectId).stream()
                .map(taskMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ProjectTaskResponse findTaskById(Integer id) {
        ProjectTask task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o id: " + id));
        return taskMapper.toResponse(task);
    }

    @Transactional
    public void deleteTask(Integer id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tarefa não encontrada com o id: " + id);
        }
        taskRepository.deleteById(id);
    }
}
