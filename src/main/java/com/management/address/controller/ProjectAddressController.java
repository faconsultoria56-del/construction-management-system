package com.management.address.controller;

import com.management.address.dto.ProjectAddressRequestDTO;
import com.management.address.dto.ProjectAddressResponseDTO;
import com.management.address.service.ProjectAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/addresses")
@Tag(name = "Projects")
public class ProjectAddressController {

    @Autowired
    private ProjectAddressService projectAddressService;

    @PostMapping
    @Operation(summary = "Creates a new address for a project")
    public ResponseEntity<ProjectAddressResponseDTO> createProjectAddress(@PathVariable Long projectId, @Valid @RequestBody ProjectAddressRequestDTO requestDTO) {
        ProjectAddressResponseDTO responseDTO = projectAddressService.createProjectAddress(projectId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Finds the address for a project")
    public ResponseEntity<ProjectAddressResponseDTO> getProjectAddress(@PathVariable Long projectId) {
        ProjectAddressResponseDTO responseDTO = projectAddressService.getProjectAddress(projectId);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping
    @Operation(summary = "Updates the address for a project")
    public ResponseEntity<ProjectAddressResponseDTO> updateProjectAddress(@PathVariable Long projectId, @Valid @RequestBody ProjectAddressRequestDTO requestDTO) {
        ProjectAddressResponseDTO responseDTO = projectAddressService.updateProjectAddress(projectId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping
    @Operation(summary = "Deletes the address for a project")
    public ResponseEntity<Void> deleteProjectAddress(@PathVariable Long projectId) {
        projectAddressService.deleteProjectAddress(projectId);
        return ResponseEntity.noContent().build();
    }
}
