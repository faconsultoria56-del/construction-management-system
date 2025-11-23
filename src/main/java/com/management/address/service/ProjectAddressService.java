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

import java.util.List;
import java.util.stream.Collectors;

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

    public ProjectAddressResponseDTO createProjectAddress(Integer projectId, ProjectAddressRequestDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Address address = modelMapper.map(requestDTO, Address.class);
        address.setCity(cityRepository.findById(requestDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + requestDTO.getCityId())));

        project.getAddresses().add(address);
        projectRepository.save(project);

        return modelMapper.map(address, ProjectAddressResponseDTO.class);
    }

    public List<ProjectAddressResponseDTO> getAllProjectAddresses(Integer projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        return project.getAddresses().stream()
                .map(address -> modelMapper.map(address, ProjectAddressResponseDTO.class))
                .collect(Collectors.toList());
    }

    public ProjectAddressResponseDTO getProjectAddressById(Integer projectId, Integer addressId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Address address = project.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        return modelMapper.map(address, ProjectAddressResponseDTO.class);
    }

    public ProjectAddressResponseDTO updateProjectAddress(Integer projectId, Integer addressId, ProjectAddressRequestDTO requestDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Address address = project.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        modelMapper.map(requestDTO, address);
        address.setCity(cityRepository.findById(requestDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + requestDTO.getCityId())));

        addressRepository.save(address);

        return modelMapper.map(address, ProjectAddressResponseDTO.class);
    }

    public void deleteProjectAddress(Integer projectId, Integer addressId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + projectId));

        Address address = project.getAddresses().stream()
                .filter(a -> a.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        project.getAddresses().remove(address);
        projectRepository.save(project);
    }
}
