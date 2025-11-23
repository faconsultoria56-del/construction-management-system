package com.management.address.service;

import com.management.address.dto.ProjectAddressRequestDTO;
import com.management.address.dto.ProjectAddressResponseDTO;
import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.city.repository.CityRepository;
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
    private CityRepository cityRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ProjectAddressResponseDTO createProjectAddress(Long projectId, ProjectAddressRequestDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Address address = modelMapper.map(requestDTO, Address.class);
        address.setCity(cityRepository.findById(requestDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + requestDTO.getCityId())));

        project.setAddress(address);
        projectRepository.save(project);

        return modelMapper.map(address, ProjectAddressResponseDTO.class);
    }

    public ProjectAddressResponseDTO getProjectAddress(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (project.getAddress() == null) {
            throw new ResourceNotFoundException("Address not found for project with id: " + projectId);
        }

        return modelMapper.map(project.getAddress(), ProjectAddressResponseDTO.class);
    }

    public ProjectAddressResponseDTO updateProjectAddress(Long projectId, ProjectAddressRequestDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (project.getAddress() == null) {
            throw new ResourceNotFoundException("Address not found for project with id: " + projectId);
        }

        Address address = project.getAddress();
        modelMapper.map(requestDTO, address);
        address.setCity(cityRepository.findById(requestDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + requestDTO.getCityId())));

        addressRepository.save(address);

        return modelMapper.map(address, ProjectAddressResponseDTO.class);
    }

    public void deleteProjectAddress(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        if (project.getAddress() == null) {
            throw new ResourceNotFoundException("Address not found for project with id: " + projectId);
        }

        Address address = project.getAddress();
        project.setAddress(null);
        projectRepository.save(project);
        addressRepository.delete(address);
    }
}
