package com.project.construction.api.controller;

import com.project.construction.api.dto.request.MaterialRequestRequest;
import com.project.construction.api.dto.response.ConstructionSiteResponse;
import com.project.construction.api.dto.response.EmployeeResponse;
import com.project.construction.api.dto.response.MaterialRequestResponse;
import com.project.construction.exception.MaterialApprovalException;
import com.project.construction.model.ConstructionSite;
import com.project.construction.model.Employee;
import com.project.construction.model.MaterialRequest;
import com.project.construction.service.ConstructionSiteService;
import com.project.construction.service.EmployeeService;
import com.project.construction.service.MaterialRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/material-requests")
public class MaterialRequestController {

    private final MaterialRequestService materialRequestService;
    private final ConstructionSiteService constructionSiteService;
    private final EmployeeService employeeService;

    public MaterialRequestController(MaterialRequestService materialRequestService,
                                     ConstructionSiteService constructionSiteService,
                                     EmployeeService employeeService) {
        this.materialRequestService = materialRequestService;
        this.constructionSiteService = constructionSiteService;
        this.employeeService = employeeService;
    }

    @PostMapping
    public MaterialRequestResponse createMaterialRequest(@RequestBody MaterialRequestRequest requestDto) {
        MaterialRequest materialRequest = toEntity(requestDto);
        return toResponse(materialRequestService.save(materialRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialRequestResponse> getMaterialRequestById(@PathVariable Long id) {
        MaterialRequest materialRequest = materialRequestService.findById(id);
        return ResponseEntity.ok(toResponse(materialRequest));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<String> getMaterialRequestStatus(@PathVariable Long id) {
        MaterialRequest materialRequest = materialRequestService.findById(id);
        return ResponseEntity.ok(materialRequest.getStatus());
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<MaterialRequestResponse> approveMaterialRequest(@PathVariable Long id) {
        MaterialRequest updatedRequest = materialRequestService.approve(id);
        return ResponseEntity.ok(toResponse(updatedRequest));
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<MaterialRequestResponse> rejectMaterialRequest(@PathVariable Long id) {
        MaterialRequest updatedRequest = materialRequestService.reject(id);
        return ResponseEntity.ok(toResponse(updatedRequest));
    }

    private MaterialRequest toEntity(MaterialRequestRequest requestDto) {
        MaterialRequest materialRequest = new MaterialRequest();
        materialRequest.setStatus(requestDto.getStatus());

        ConstructionSite site = constructionSiteService.findById(requestDto.getConstructionSiteId());
        materialRequest.setConstructionSite(site);

        Employee employee = employeeService.findById(requestDto.getRequestedByEmployeeId());
        materialRequest.setRequestedBy(employee);

        return materialRequest;
    }

    private MaterialRequestResponse toResponse(MaterialRequest materialRequest) {
        MaterialRequestResponse response = new MaterialRequestResponse();
        response.setId(materialRequest.getId());
        response.setStatus(materialRequest.getStatus());
        response.setConstructionSite(toConstructionSiteResponse(materialRequest.getConstructionSite()));
        response.setRequestedBy(toEmployeeResponse(materialRequest.getRequestedBy()));
        return response;
    }

    private ConstructionSiteResponse toConstructionSiteResponse(ConstructionSite site) {
        ConstructionSiteResponse response = new ConstructionSiteResponse();
        response.setId(site.getId());
        response.setName(site.getName());
        response.setLocation(site.getLocation());
        response.setStartDate(site.getStartDate());
        response.setEndDate(site.getEndDate());
        return response;
    }

    private EmployeeResponse toEmployeeResponse(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setCpf(employee.getCpf());
        response.setRole(employee.getRole());
        response.setContractType(employee.getContractType());
        return response;
    }
}
