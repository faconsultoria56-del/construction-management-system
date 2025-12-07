package com.management.address.service;

import com.management.address.dto.ProjectAddressRequestDTO;
import com.management.address.dto.ProjectAddressResponseDTO;
import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
import com.management.city.model.City;
import com.management.city.repository.CityRepository;
import com.management.exception.ResourceNotFoundException;
import com.management.project.model.Project;
import com.management.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectAddressServiceTest {

    @InjectMocks
    private ProjectAddressService projectAddressService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CityRepository cityRepository;

    @Spy
    private ModelMapper modelMapper;

    private Project project;
    private Address address;
    private City city;
    private ProjectAddressRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        city = new City();
        city.setId(1);
        city.setName("Test City");

        address = new Address();
        address.setId(1);
        address.setStreet("Test Street");
        address.setCity(city);

        project = new Project();
        project.setId(1);
        project.setAddress(address);

        requestDTO = new ProjectAddressRequestDTO();
        requestDTO.setCityId(1);
        requestDTO.setStreet("Test Street");
    }

    @Test
    void testCreateProjectAddress() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectAddressResponseDTO responseDTO = projectAddressService.createProjectAddress(1, requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Test Street", responseDTO.getStreet());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void testGetProjectAddress() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        ProjectAddressResponseDTO responseDTO = projectAddressService.getProjectAddress(1);

        assertNotNull(responseDTO);
        assertEquals("Test Street", responseDTO.getStreet());
    }

    @Test
    void testGetProjectAddress_NotFound() {
        Project project = new Project();
        project.setAddress(null);
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));

        assertThrows(ResourceNotFoundException.class, () -> {
            projectAddressService.getProjectAddress(1);
        });
    }

    @Test
    void testUpdateProjectAddress() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(cityRepository.findById(1)).thenReturn(Optional.of(city));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        ProjectAddressResponseDTO responseDTO = projectAddressService.updateProjectAddress(1, requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Test Street", responseDTO.getStreet());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testDeleteProjectAddress() {
        when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        projectAddressService.deleteProjectAddress(1);

        verify(projectRepository, times(1)).save(project);
        assertNull(project.getAddress());
    }
}
