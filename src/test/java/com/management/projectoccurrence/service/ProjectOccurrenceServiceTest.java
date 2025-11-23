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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectOccurrenceServiceTest {

    @Mock
    private ProjectOccurrenceRepository projectOccurrenceRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private PersonRepository personRepository;

    private ProjectOccurrenceMapper projectOccurrenceMapper;

    @InjectMocks
    private ProjectOccurrenceService projectOccurrenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<ProjectOccurrenceCreateRequest, ProjectOccurrence>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        });
        projectOccurrenceMapper = new ProjectOccurrenceMapper(modelMapper);
        projectOccurrenceService = new ProjectOccurrenceService(projectOccurrenceRepository, projectRepository, personRepository, projectOccurrenceMapper);
    }

    @Test
    void testCreate() {
        ProjectOccurrenceCreateRequest request = new ProjectOccurrenceCreateRequest();
        request.setProjectId(1);
        request.setPersonId(1);
        request.setDescription("Test Description");
        request.setOccurrenceDate(LocalDate.now());

        Project project = new Project();
        project.setId(1);
        Person person = new Person();
        person.setId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(personRepository.findById(1)).thenReturn(Optional.of(person));
        when(projectOccurrenceRepository.save(any(ProjectOccurrence.class))).thenAnswer(i -> i.getArguments()[0]);

        ProjectOccurrenceResponse response = projectOccurrenceService.create(request);

        assertNotNull(response);
        assertEquals(request.getProjectId(), response.getProjectId());
        assertEquals(request.getPersonId(), response.getPersonId());
        assertEquals(request.getDescription(), response.getDescription());
    }
     @Test
    void testUpdate() {
        ProjectOccurrenceUpdateRequest request = new ProjectOccurrenceUpdateRequest();
        request.setDescription("Updated Description");

        Project project = new Project();
        project.setId(1);
        Person person = new Person();
        person.setId(1);

        ProjectOccurrence occurrence = new ProjectOccurrence();
        occurrence.setId(1);
        occurrence.setDescription("Old Description");
        occurrence.setProject(project);
        occurrence.setPerson(person);


        when(projectOccurrenceRepository.findById(1)).thenReturn(Optional.of(occurrence));
        when(projectOccurrenceRepository.save(any(ProjectOccurrence.class))).thenAnswer(i -> i.getArguments()[0]);

        ProjectOccurrenceResponse response = projectOccurrenceService.update(1, request);

        assertNotNull(response);
        assertEquals("Updated Description", response.getDescription());
    }

    @Test
    void testDelete() {
        when(projectOccurrenceRepository.existsById(1)).thenReturn(true);
        doNothing().when(projectOccurrenceRepository).deleteById(1);

        projectOccurrenceService.delete(1);

        verify(projectOccurrenceRepository, times(1)).deleteById(1);
    }

    @Test
    void testListByProject() {
        Project project = new Project();
        project.setId(1);
        Person person = new Person();
        person.setId(1);

        ProjectOccurrence occurrence = new ProjectOccurrence();
        occurrence.setId(1);
        occurrence.setProject(project);
        occurrence.setPerson(person);

        when(projectRepository.existsById(1)).thenReturn(true);
        when(projectOccurrenceRepository.findByProjectId(1)).thenReturn(Collections.singletonList(occurrence));

        List<ProjectOccurrenceResponse> response = projectOccurrenceService.listByProject(1);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.get(0).getProjectId());
    }

    @Test
    void testFindById() {
        Project project = new Project();
        project.setId(1);
        Person person = new Person();
        person.setId(1);

        ProjectOccurrence occurrence = new ProjectOccurrence();
        occurrence.setId(1);
        occurrence.setProject(project);
        occurrence.setPerson(person);

        when(projectOccurrenceRepository.findById(1)).thenReturn(Optional.of(occurrence));

        ProjectOccurrenceResponse response = projectOccurrenceService.findById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    void testCreate_ProjectNotFound() {
        ProjectOccurrenceCreateRequest request = new ProjectOccurrenceCreateRequest();
        request.setProjectId(1);

        when(projectRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> projectOccurrenceService.create(request));
    }
}
