package com.management.financial.controller;

import com.management.financial.dto.CostCenterDTO;
import com.management.financial.service.CostCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cost-centers")
public class CostCenterController {

    @Autowired
    private CostCenterService costCenterService;

    @PostMapping
    public CostCenterDTO createCostCenter(@RequestBody CostCenterDTO costCenterDTO) {
        return costCenterService.createCostCenter(costCenterDTO);
    }

    @GetMapping
    public List<CostCenterDTO> getAllCostCenters() {
        return costCenterService.getAllCostCenters();
    }

    @GetMapping("/{id}")
    public CostCenterDTO getCostCenterById(@PathVariable Long id) {
        return costCenterService.getCostCenterById(id);
    }

    @PutMapping("/{id}")
    public CostCenterDTO updateCostCenter(@PathVariable Long id, @RequestBody CostCenterDTO costCenterDTO) {
        return costCenterService.updateCostCenter(id, costCenterDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCostCenter(@PathVariable Long id) {
        costCenterService.deleteCostCenter(id);
        return ResponseEntity.noContent().build();
    }
}
