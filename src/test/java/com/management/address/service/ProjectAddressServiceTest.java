package com.management.address.service;

import com.management.address.dto.ProjectAddressRequestDTO;
import com.management.address.dto.ProjectAddressResponseDTO;
import com.management.address.model.Address;
import com.management.address.repository.AddressRepository;
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

    @Spy
    private ModelMapper modelMapper;

    private Project project;
    private Address address;
    private ProjectAddressRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setId(1L);
        address.setStreet("Test Street");
        address.setCityName("Test City");
        address.setStateName("Test State");
        address.setCountryName("Test Country");

        project = new Project();
        project.setId(1L);
        project.setAddress(address);

        requestDTO = new ProjectAddressRequestDTO();
        requestDTO.setStreet("Test Street");
        requestDTO.setCityName("Test City");
        requestDTO.setStateName("Test State");
        requestDTO.setCountryName("Test Country");
    }

    @Test
    void testCreateProjectAddress() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(new Project()));
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        ProjectAddressResponseDTO responseDTO = projectAddressService.createProjectAddress(1L, requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Test Street", responseDTO.getStreet());
        assertEquals("Test City", responseDTO.getCityName());
        verify(projectRepository, times(1)).save(any(Project.class));
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void testGetProjectAddress() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProjectAddressResponseDTO responseDTO = projectAddressService.getProjectAddress(1L);

        assertNotNull(responseDTO);
        assertEquals("Test Street", responseDTO.getStreet());
    }

    @Test
    void testGetProjectAddress_NotFound() {
        Project projectWithoutAddress = new Project();
        projectWithoutAddress.setAddress(null);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(projectWithoutAddress));

        assertThrows(ResourceNotFoundException.class, () -> {
            projectAddressService.getProjectAddress(1L);
        });
    }

    @Test
    void testUpdateProjectAddress() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        ProjectAddressResponseDTO responseDTO = projectAddressService.updateProjectAddress(1L, requestDTO);

        assertNotNull(responseDTO);
        assertEquals("Test Street", responseDTO.getStreet());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    void testDeleteProjectAddress() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        doNothing().when(addressRepository).delete(any(Address.class));

        projectAddressService.deleteProjectAddress(1L);

        verify(projectRepository, times(1)).save(project);
        verify(addressRepository, times(1)).delete(address);
        assertNull(project.getAddress());
    }
}
