package com.management.project.service;

import com.management.address.repository.AddressRepository;
import com.management.company.repository.CompanyRepository;
import com.management.person.repository.PersonRepository;
import com.management.project.dto.ProjectCreateDTO;
import com.management.project.dto.ProjectResponseDTO;
import com.management.project.model.Project;
import com.management.project.model.ProjectMember;
import com.management.project.repository.ProjectMemberRepository;
import com.management.project.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public ProjectService(ProjectRepository projectRepository, ProjectMemberRepository projectMemberRepository, CompanyRepository companyRepository, PersonRepository personRepository, AddressRepository addressRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.projectMemberRepository = projectMemberRepository;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ProjectResponseDTO createProject(ProjectCreateDTO createDTO) {
        if (createDTO.getCompanyId() == null && createDTO.getOwnerPersonId() == null) {
            throw new IllegalArgumentException("Projeto precisa ter empresa (CNPJ) ou dono (CPF).");
        }

        Project project = modelMapper.map(createDTO, Project.class);

        if (createDTO.getCompanyId() != null) {
            project.setCompany(companyRepository.findById(createDTO.getCompanyId()).orElseThrow());
        }

        if (createDTO.getOwnerPersonId() != null) {
            project.setOwnerPerson(personRepository.findById(createDTO.getOwnerPersonId()).orElseThrow());
        }

        if (createDTO.getAddressId() != null) {
            project.setAddress(addressRepository.findById(createDTO.getAddressId()).orElseThrow());
        }

        project = projectRepository.save(project);

        if (project.getOwnerPerson() != null) {
            ProjectMember member = new ProjectMember();
            member.setProject(project);
            member.setPerson(project.getOwnerPerson());
            member.setRole("Owner");
            projectMemberRepository.save(member);
        }

        return modelMapper.map(project, ProjectResponseDTO.class);
    }
}
