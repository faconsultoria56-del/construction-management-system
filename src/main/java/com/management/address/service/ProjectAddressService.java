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
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectAddressService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public ProjectAddressResponseDTO createProjectAddress(Long projectId, ProjectAddressRequestDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Address address = modelMapper.map(requestDTO, Address.class);
        Address savedAddress = addressRepository.save(address);

        project.setAddress(savedAddress);
        projectRepository.save(project);

        return modelMapper.map(savedAddress, ProjectAddressResponseDTO.class);
    }

    public ProjectAddressResponseDTO getProjectAddress(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (project.getAddress() == null) {
            throw new ResourceNotFoundException("Address not found for project with id: " + projectId);
        }

        return modelMapper.map(project.getAddress(), ProjectAddressResponseDTO.class);
    }

    @Transactional
    public ProjectAddressResponseDTO updateProjectAddress(Long projectId, ProjectAddressRequestDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Address address = project.getAddress();
        if (address == null) {
            throw new ResourceNotFoundException("Address not found for project with id: " + projectId);
        }

        modelMapper.map(requestDTO, address);
        Address updatedAddress = addressRepository.save(address);

        return modelMapper.map(updatedAddress, ProjectAddressResponseDTO.class);
    }

    @Transactional
    public void deleteProjectAddress(Long projectId) {
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
