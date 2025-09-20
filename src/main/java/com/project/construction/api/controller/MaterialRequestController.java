package com.project.construction.api.controller;

import com.project.construction.model.MaterialRequest;
import com.project.construction.service.MaterialRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/material-requests")
public class MaterialRequestController {

    private final MaterialRequestService materialRequestService;

    public MaterialRequestController(MaterialRequestService materialRequestService) {
        this.materialRequestService = materialRequestService;
    }

    @PostMapping
    public MaterialRequest createMaterialRequest(@RequestBody MaterialRequest materialRequest) {
        // In a real app, we'd set the initial status here, e.g., "PENDING"
        return materialRequestService.save(materialRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialRequest> getMaterialRequestById(@PathVariable Long id) {
        Optional<MaterialRequest> materialRequest = materialRequestService.findById(id);
        return materialRequest.map(ResponseEntity::ok)
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getMaterialRequestStatus(@PathVariable Long id) {
        Optional<MaterialRequest> materialRequest = materialRequestService.findById(id);
        return materialRequest.map(request -> ResponseEntity.ok(request.getStatus()))
                              .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<MaterialRequest> approveMaterialRequest(@PathVariable Long id) {
        Optional<MaterialRequest> requestOptional = materialRequestService.findById(id);
        if (requestOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MaterialRequest request = requestOptional.get();
        request.setStatus("APPROVED");
        return ResponseEntity.ok(materialRequestService.save(request));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<MaterialRequest> rejectMaterialRequest(@PathVariable Long id) {
        Optional<MaterialRequest> requestOptional = materialRequestService.findById(id);
        if (requestOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        MaterialRequest request = requestOptional.get();
        request.setStatus("REJECTED");
        return ResponseEntity.ok(materialRequestService.save(request));
    }
}
