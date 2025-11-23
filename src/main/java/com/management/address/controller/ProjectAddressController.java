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

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/addresses")
@Tag(name = "Projects")
public class ProjectAddressController {

    @Autowired
    private ProjectAddressService projectAddressService;

    @PostMapping
    @Operation(summary = "Creates a new address for a project")
    public ResponseEntity<ProjectAddressResponseDTO> createProjectAddress(@PathVariable Integer projectId, @Valid @RequestBody ProjectAddressRequestDTO requestDTO) {
        ProjectAddressResponseDTO responseDTO = projectAddressService.createProjectAddress(projectId, requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Finds all addresses for a project")
    public ResponseEntity<List<ProjectAddressResponseDTO>> getAllProjectAddresses(@PathVariable Integer projectId) {
        List<ProjectAddressResponseDTO> responseDTO = projectAddressService.getAllProjectAddresses(projectId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "Finds an address by ID for a project")
    public ResponseEntity<ProjectAddressResponseDTO> getProjectAddressById(@PathVariable Integer projectId, @PathVariable Integer addressId) {
        ProjectAddressResponseDTO responseDTO = projectAddressService.getProjectAddressById(projectId, addressId);
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Updates an address for a project")
    public ResponseEntity<ProjectAddressResponseDTO> updateProjectAddress(@PathVariable Integer projectId, @PathVariable Integer addressId, @Valid @RequestBody ProjectAddressRequestDTO requestDTO) {
        ProjectAddressResponseDTO responseDTO = projectAddressService.updateProjectAddress(projectId, addressId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Deletes an address for a project")
    public ResponseEntity<Void> deleteProjectAddress(@PathVariable Integer projectId, @PathVariable Integer addressId) {
        projectAddressService.deleteProjectAddress(projectId, addressId);
        return ResponseEntity.noContent().build();
    }
}
