package com.management.task.service;

import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.project.model.Project;
import com.management.project.repository.ProjectRepository;
import com.management.task.dto.ProjectTaskCreateRequest;
import com.management.task.dto.ProjectTaskResponse;
import com.management.task.mapper.ProjectTaskMapper;
import com.management.task.model.ProjectTask;
import com.management.task.repository.ProjectTaskRepository;
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
class ProjectTaskServiceTest {

    @Mock
    private ProjectTaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private ProjectTaskMapper taskMapper;

    @InjectMocks
    private ProjectTaskService taskService;

    @Test
    void createTask_shouldSucceed() {
        // Arrange
        ProjectTaskCreateRequest request = new ProjectTaskCreateRequest();
        request.setTitle("New Task");
        request.setProjectId(1);
        request.setResponsibleId(1);

        Project project = new Project();
        Person person = new Person();
        ProjectTask task = new ProjectTask();
        ProjectTaskResponse response = new ProjectTaskResponse();

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(personRepository.findById(1)).thenReturn(Optional.of(person));
        when(taskRepository.save(any(ProjectTask.class))).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(response);

        // Act
        ProjectTaskResponse result = taskService.createTask(request);

        // Assert
        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(ProjectTask.class));
    }
}
