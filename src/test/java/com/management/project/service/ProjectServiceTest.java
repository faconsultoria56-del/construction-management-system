package com.management.project.service;

import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.company.model.Company;
import com.management.company.repository.CompanyRepository;
import com.management.person.model.Person;
import com.management.person.repository.PersonRepository;
import com.management.project.dto.ProjectCreateRequest;
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

import java.time.LocalDate;
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
        project.setAddress(address);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectResponse response = projectService.create(projectCreateRequest);

        assertNotNull(response);
        assertEquals(address.getId(), response.getAddressId());
    }
}
