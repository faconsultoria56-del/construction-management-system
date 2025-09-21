package com.project.construction.api.controller;

import com.project.construction.api.dto.request.MaterialRequestRequest;
import com.project.construction.api.dto.response.MaterialRequestResponse;
import com.project.construction.facade.MaterialRequestFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/material-requests")
public class MaterialRequestController {

    private final MaterialRequestFacade materialRequestFacade;

    public MaterialRequestController(MaterialRequestFacade materialRequestFacade) {
        this.materialRequestFacade = materialRequestFacade;
    }

    @PostMapping
    public MaterialRequestResponse createMaterialRequest(@RequestBody MaterialRequestRequest requestDto) {
        return materialRequestFacade.createMaterialRequest(requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialRequestResponse> getMaterialRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(materialRequestFacade.getMaterialRequestById(id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getMaterialRequestStatus(@PathVariable Long id) {
        return ResponseEntity.ok(materialRequestFacade.getMaterialRequestById(id).getStatus());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<MaterialRequestResponse> approveMaterialRequest(@PathVariable Long id) {
        return ResponseEntity.ok(materialRequestFacade.approveMaterialRequest(id));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<MaterialRequestResponse> rejectMaterialRequest(@PathVariable Long id) {
        return ResponseEntity.ok(materialRequestFacade.rejectMaterialRequest(id));
    }
}
