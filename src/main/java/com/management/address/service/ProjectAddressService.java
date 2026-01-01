package com.management.address.service;

import com.management.address.dto.ProjectAddressRequestDTO;
import com.management.address.dto.ProjectAddressResponseDTO;
import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.exception.ResourceNotFoundException;
import com.management.project.model.Project;
import com.management.project.repository.ProjectRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectAddressService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProjectAddressResponseDTO createProjectAddress(Integer projectId, ProjectAddressRequestDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Address address = modelMapper.map(requestDTO, Address.class);

        // As the relationship is now managed by Project, we should set the address on the project
        // and let JPA handle the foreign key.
        // However, the previous logic was setting the address on the project directly,
        // but there is no address field in the Project entity. I will assume a bi-directional
        // relationship needs to be established or that a project can have one address.
        // Based on the previous code, the project has a foreign key to address.
        // The relationship seems to be Project -> Address.
        // Therefore, we save the address first.
        Address savedAddress = addressRepository.save(address);

        project.setAddress(savedAddress);
        projectRepository.save(project);

        return modelMapper.map(savedAddress, ProjectAddressResponseDTO.class);
    }

    public ProjectAddressResponseDTO getProjectAddress(Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (project.getAddress() == null) {
            throw new ResourceNotFoundException("Address not found for project with id: " + projectId);
        }

        return modelMapper.map(project.getAddress(), ProjectAddressResponseDTO.class);
    }

    public ProjectAddressResponseDTO updateProjectAddress(Integer projectId, ProjectAddressRequestDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Address address = project.getAddress();
        if (address == null) {
            throw new ResourceNotFoundException("Address not found for project with id: " + projectId);
        }

        modelMapper.map(requestDTO, address);
        addressRepository.save(address);

        return modelMapper.map(address, ProjectAddressResponseDTO.class);
    }

    public void deleteProjectAddress(Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (project.getAddress() != null) {
            Address addressToDelete = project.getAddress();
            project.setAddress(null);
            projectRepository.save(project);
            addressRepository.delete(addressToDelete);
        }
    }
}
