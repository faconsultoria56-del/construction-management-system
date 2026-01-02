package com.management.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.address.dto.ProjectAddressRequestDTO;
import com.management.address.dto.ProjectAddressResponseDTO;
import com.management.address.service.ProjectAddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ProjectAddressControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProjectAddressService projectAddressService;

    @InjectMocks
    private ProjectAddressController projectAddressController;

    private ObjectMapper objectMapper = new ObjectMapper();

    private ProjectAddressRequestDTO requestDTO;
    private ProjectAddressResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(projectAddressController).build();
        requestDTO = new ProjectAddressRequestDTO();
        requestDTO.setStreet("Test Street");
        requestDTO.setNumber("123");
        requestDTO.setNeighborhood("Test Neighborhood");
        requestDTO.setZipCode("12345-678");
        requestDTO.setCityId(1);

        responseDTO = new ProjectAddressResponseDTO();
        responseDTO.setStreet("Test Street");
    }

    @Test
    void testCreateProjectAddress() throws Exception {
        when(projectAddressService.createProjectAddress(anyInt(), any(ProjectAddressRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/projects/1/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value("Test Street"));
    }

    @Test
    void testGetProjectAddress() throws Exception {
        when(projectAddressService.getProjectAddress(anyInt())).thenReturn(responseDTO);

        mockMvc.perform(get("/api/projects/1/address"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Test Street"));
    }

    @Test
    void testUpdateProjectAddress() throws Exception {
        when(projectAddressService.updateProjectAddress(anyInt(), any(ProjectAddressRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/projects/1/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Test Street"));
    }

    @Test
    void testDeleteProjectAddress() throws Exception {
        doNothing().when(projectAddressService).deleteProjectAddress(anyInt());

        mockMvc.perform(delete("/api/projects/1/address"))
                .andExpect(status().isNoContent());
    }
}
