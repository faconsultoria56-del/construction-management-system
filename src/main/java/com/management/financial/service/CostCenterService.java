package com.management.financial.service;

import com.management.exception.ResourceNotFoundException;
import com.management.financial.dto.CostCenterDTO;
import com.management.financial.model.CostCenter;
import com.management.financial.repository.CostCenterRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CostCenterService {

    @Autowired
    private CostCenterRepository costCenterRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CostCenterDTO createCostCenter(CostCenterDTO costCenterDTO) {
        CostCenter costCenter = modelMapper.map(costCenterDTO, CostCenter.class);
        costCenter = costCenterRepository.save(costCenter);
        return modelMapper.map(costCenter, CostCenterDTO.class);
    }

    public List<CostCenterDTO> getAllCostCenters() {
        return costCenterRepository.findAll().stream()
                .map(costCenter -> modelMapper.map(costCenter, CostCenterDTO.class))
                .collect(Collectors.toList());
    }

    public CostCenterDTO getCostCenterById(Integer id) {
        CostCenter costCenter = costCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cost Center not found"));
        return modelMapper.map(costCenter, CostCenterDTO.class);
    }

    public CostCenterDTO updateCostCenter(Integer id, CostCenterDTO costCenterDTO) {
        CostCenter existingCostCenter = costCenterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cost Center not found"));
        existingCostCenter.setName(costCenterDTO.getName());
        existingCostCenter.setDescription(costCenterDTO.getDescription());
        existingCostCenter = costCenterRepository.save(existingCostCenter);
        return modelMapper.map(existingCostCenter, CostCenterDTO.class);
    }

    public void deleteCostCenter(Integer id) {
        if (!costCenterRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cost Center not found");
        }
        costCenterRepository.deleteById(id);
    }
}
