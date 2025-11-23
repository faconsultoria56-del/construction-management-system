package com.management.project.service;

import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.company.model.Company;
import com.management.company.repository.CompanyRepository;
import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.project.dto.ProjectCreateRequest;
import com.management.project.dto.ProjectMapper;
import com.management.project.dto.ProjectResponse;
import com.management.project.exception.BusinessException;
import com.management.project.model.Project;
import com.management.project.model.ProjectMember;
import com.management.project.repository.ProjectMemberRepository;
import com.management.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ProjectMapper projectMapper;

    @InjectMocks
    private ProjectService projectService;

    private ProjectCreateRequest projectCreateRequest;

    @BeforeEach
    void setUp() {
        projectCreateRequest = new ProjectCreateRequest();
        projectCreateRequest.setName("New Project");
        projectCreateRequest.setDescription("Project Description");
        projectCreateRequest.setStartDate(LocalDate.now());
        projectCreateRequest.setEndDate(LocalDate.now().plusMonths(6));
    }

    @Test
    void create_shouldThrowBusinessException_whenBothCompanyIdAndOwnerPersonIdAreProvided() {
        projectCreateRequest.setCompanyId(1);
        projectCreateRequest.setOwnerPersonId(1);

        assertThrows(BusinessException.class, () -> projectService.create(projectCreateRequest));
    }

    @Test
    void create_shouldThrowBusinessException_whenNeitherCompanyIdNorOwnerPersonIdIsProvided() {
        assertThrows(BusinessException.class, () -> projectService.create(projectCreateRequest));
    }

    @Test
    void create_shouldCreateCorporateProject_whenCompanyIdIsProvided() {
        projectCreateRequest.setCompanyId(1);

        Company company = new Company();
        company.setId(1);
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        Project project = new Project();
        project.setId(1);
        project.setName(projectCreateRequest.getName());
        project.setCompany(company);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(1);
        projectResponse.setName("New Project");
        projectResponse.setCompanyId(1);
        when(projectMapper.toResponse(any(Project.class))).thenReturn(projectResponse);

        ProjectResponse response = projectService.create(projectCreateRequest);

        assertNotNull(response);
        assertEquals(project.getId(), response.getId());
        assertEquals(project.getName(), response.getName());
        assertEquals(company.getId(), response.getCompanyId());
        assertNull(response.getOwnerPersonId());
        verify(projectMemberRepository, never()).save(any(ProjectMember.class));
    }

    @Test
    void create_shouldCreatePersonalProjectAndProjectMember_whenOwnerPersonIdIsProvided() {
        projectCreateRequest.setOwnerPersonId(1);

        Person person = new Person();
        person.setId(1);
        when(personRepository.findById(1)).thenReturn(Optional.of(person));

        Project project = new Project();
        project.setId(1);
        project.setName(projectCreateRequest.getName());
        project.setOwnerPerson(person);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(1);
        projectResponse.setName("New Project");
        projectResponse.setOwnerPersonId(1);
        when(projectMapper.toResponse(any(Project.class))).thenReturn(projectResponse);

        ProjectResponse response = projectService.create(projectCreateRequest);

        assertNotNull(response);
        assertEquals(project.getId(), response.getId());
        assertEquals(project.getName(), response.getName());
        assertEquals(person.getId(), response.getOwnerPersonId());
        assertNull(response.getCompanyId());
        verify(projectMemberRepository, times(1)).save(any(ProjectMember.class));
    }

    @Test
    void create_shouldSetAddress_whenAddressIdIsProvided() {
        projectCreateRequest.setCompanyId(1);
        projectCreateRequest.setAddressId(1);

        Company company = new Company();
        company.setId(1);
        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        Address address = new Address();
        address.setId(1);
        when(addressRepository.findById(1)).thenReturn(Optional.of(address));

        Project project = new Project();
        project.setId(1);
        project.setName(projectCreateRequest.getName());
        project.setCompany(company);
        project.setAddresses(Collections.singletonList(address));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setAddressId(1);
        when(projectMapper.toResponse(any(Project.class))).thenReturn(projectResponse);

        ProjectResponse response = projectService.create(projectCreateRequest);

        assertNotNull(response);
        assertEquals(address.getId(), response.getAddressId());
    }

    @Test
    void findAll_shouldReturnPageOfProjects() {
        Page<Project> page = new PageImpl<>(Collections.singletonList(new Project()));
        when(projectRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<ProjectResponse> response = projectService.findAll(Pageable.unpaged());

        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
    }

    @Test
    void findById_shouldReturnProject() {
        Project project = new Project();
        project.setId(1);
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        ProjectResponse projectResponse = new ProjectResponse();
        projectResponse.setId(1);
        when(projectMapper.toResponse(any(Project.class))).thenReturn(projectResponse);

        ProjectResponse response = projectService.findById(1);

        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    void findByCompanyId_shouldReturnListOfProjects() {
        when(companyRepository.existsById(1)).thenReturn(true);
        when(projectRepository.findByCompanyId(1)).thenReturn(Collections.singletonList(new Project()));
        when(projectMapper.toResponse(any(List.class))).thenReturn(Collections.singletonList(new ProjectResponse()));

        List<ProjectResponse> response = projectService.findByCompanyId(1);

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void findByPersonId_shouldReturnListOfProjects() {
        when(personRepository.existsById(1)).thenReturn(true);
        when(projectRepository.findByOwnerPersonId(1)).thenReturn(Collections.singletonList(new Project()));
        when(projectMemberRepository.findByPersonId(1)).thenReturn(Collections.emptyList());
        when(projectMapper.toResponse(any(List.class))).thenReturn(Collections.singletonList(new ProjectResponse()));

        List<ProjectResponse> response = projectService.findByPersonId(1);

        assertNotNull(response);
        assertEquals(1, response.size());
    }
}
