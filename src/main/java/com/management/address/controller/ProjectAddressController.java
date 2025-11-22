package com.management.address.controller;

import com.management.address.dto.ProjectAddressRequestDTO;
import com.management.address.dto.ProjectAddressResponseDTO;
import com.management.address.service.ProjectAddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/address")
public class ProjectAddressController {

    @Autowired
    private ProjectAddressService projectAddressService;

    @PostMapping
    public ResponseEntity<ProjectAddressResponseDTO> createProjectAddress(@PathVariable Long projectId, @Valid @RequestBody ProjectAddressRequestDTO requestDTO) {
        ProjectAddressResponseDTO responseDTO = projectAddressService.createProjectAddress(projectId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ProjectAddressResponseDTO> getProjectAddress(@PathVariable Long projectId) {
        ProjectAddressResponseDTO responseDTO = projectAddressService.getProjectAddress(projectId);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    public ResponseEntity<ProjectAddressResponseDTO> updateProjectAddress(@PathVariable Long projectId, @Valid @RequestBody ProjectAddressRequestDTO requestDTO) {
        ProjectAddressResponseDTO responseDTO = projectAddressService.updateProjectAddress(projectId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProjectAddress(@PathVariable Long projectId) {
        projectAddressService.deleteProjectAddress(projectId);
        return ResponseEntity.noContent().build();
    }
}
