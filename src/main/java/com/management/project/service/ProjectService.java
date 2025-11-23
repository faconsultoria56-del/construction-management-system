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
import com.management.project.exception.ResourceNotFoundException;
import com.management.project.model.Project;
import com.management.project.model.ProjectMember;
import com.management.project.repository.ProjectMemberRepository;
import com.management.project.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final ProjectMapper projectMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository, CompanyRepository companyRepository, PersonRepository personRepository, AddressRepository addressRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
        this.projectMapper = projectMapper;
    }

    private void validateCreateRequest(ProjectCreateRequest request) {
        if (request.getCompanyId() == null && request.getOwnerPersonId() == null) {
            throw new BusinessException("Projeto precisa ter empresa (CNPJ) ou dono (CPF).");
        }
        if (request.getCompanyId() != null && request.getOwnerPersonId() != null) {
            throw new BusinessException("Projeto deve ser pessoal OU empresarial, não ambos.");
        }
    }

    @Transactional
    public ProjectResponse create(ProjectCreateRequest request) {

        validateCreateRequest(request);

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new BusinessException("Empresa não encontrada"));
            project.setCompany(company);
            project.setOwnerPerson(null);
        }

        if (request.getOwnerPersonId() != null) {
            Person owner = personRepository.findById(request.getOwnerPersonId())
                .orElseThrow(() -> new BusinessException("Pessoa não encontrada"));
            project.setOwnerPerson(owner);
            project.setCompany(null);
        }

        if (request.getAddressId() != null) {
            Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new BusinessException("Endereço não encontrado"));
            project.setAddresses(Collections.singletonList(address));
        }

        Project saved = projectRepository.save(project);

        if (request.getOwnerPersonId() != null) {
            ProjectMember member = new ProjectMember();
            member.setProject(saved);
            member.setPerson(saved.getOwnerPerson());
            member.setRole("Owner");
            projectMemberRepository.save(member);
        }

        return projectMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<ProjectResponse> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(Integer id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
        return projectMapper.toResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findByCompanyId(Integer companyId) {
        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Company not found with id: " + companyId);
        }
        return projectMapper.toResponse(projectRepository.findByCompanyId(companyId));
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findByPersonId(Integer personId) {
        if (!personRepository.existsById(personId)) {
            throw new ResourceNotFoundException("Person not found with id: " + personId);
        }
        List<Project> projectsAsOwner = projectRepository.findByOwnerPersonId(personId);
        List<ProjectMember> projectMembers = projectMemberRepository.findByPersonId(personId);
        List<Project> projectsAsMember = projectMembers.stream()
                .map(ProjectMember::getProject)
                .toList();

        return projectMapper.toResponse(
                Stream.concat(projectsAsOwner.stream(), projectsAsMember.stream())
                        .distinct()
                        .collect(Collectors.toList())
        );
    }
}
