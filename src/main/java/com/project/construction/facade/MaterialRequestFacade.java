package com.project.construction.facade;

import com.project.construction.api.dto.request.MaterialRequestRequest;
import com.project.construction.api.dto.response.ConstructionSiteResponse;
import com.project.construction.api.dto.response.EmployeeResponse;
import com.project.construction.api.dto.response.MaterialRequestResponse;
import com.project.construction.model.ConstructionSite;
import com.project.construction.model.Employee;
import com.project.construction.model.MaterialRequest;
import com.project.construction.service.ConstructionSiteService;
import com.project.construction.service.EmployeeService;
import com.project.construction.service.MaterialRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialRequestFacade {

    private final MaterialRequestService materialRequestService;
    private final ConstructionSiteService constructionSiteService;
    private final EmployeeService employeeService;

    public MaterialRequestFacade(MaterialRequestService materialRequestService,
                                 ConstructionSiteService constructionSiteService,
                                 EmployeeService employeeService) {
        this.materialRequestService = materialRequestService;
        this.constructionSiteService = constructionSiteService;
        this.employeeService = employeeService;
    }

    @Transactional
    public MaterialRequestResponse createMaterialRequest(MaterialRequestRequest requestDto) {
        MaterialRequest materialRequest = toEntity(requestDto);
        return toResponse(materialRequestService.save(materialRequest));
    }

    @Transactional(readOnly = true)
    public MaterialRequestResponse getMaterialRequestById(Long id) {
        return toResponse(materialRequestService.findById(id));
    }

    @Transactional
    public MaterialRequestResponse approveMaterialRequest(Long id) {
        return toResponse(materialRequestService.approve(id));
    }

    @Transactional
    public MaterialRequestResponse rejectMaterialRequest(Long id) {
        return toResponse(materialRequestService.reject(id));
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
