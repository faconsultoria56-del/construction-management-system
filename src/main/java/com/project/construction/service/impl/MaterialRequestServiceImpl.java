package com.project.construction.service.impl;

import com.project.construction.exception.MaterialApprovalException;
import com.project.construction.exception.ResourceNotFoundException;
import com.project.construction.model.MaterialRequest;
import com.project.construction.repository.MaterialRequestRepository;
import com.project.construction.service.MaterialRequestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialRequestServiceImpl implements MaterialRequestService {

    private final MaterialRequestRepository materialRequestRepository;

    public MaterialRequestServiceImpl(MaterialRequestRepository materialRequestRepository) {
        this.materialRequestRepository = materialRequestRepository;
    }

    @Override
    public MaterialRequest save(MaterialRequest materialRequest) {
        return materialRequestRepository.save(materialRequest);
    }

    @Override
    public MaterialRequest findById(Long id) {
        return materialRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialRequest not found with id: " + id));
    }

    @Override
    public List<MaterialRequest> findAll() {
        return materialRequestRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        materialRequestRepository.deleteById(id);
    }

    @Override
    public MaterialRequest approve(Long id) {
        MaterialRequest request = findById(id);
        if (!"PENDING".equals(request.getStatus())) {
            throw new MaterialApprovalException("Cannot approve a request that is not in PENDING state.");
        }
        request.setStatus("APPROVED");
        return save(request);
    }

    @Override
    public MaterialRequest reject(Long id) {
        MaterialRequest request = findById(id);
        if (!"PENDING".equals(request.getStatus())) {
            throw new MaterialApprovalException("Cannot reject a request that is not in PENDING state.");
        }
        request.setStatus("REJECTED");
        return save(request);
    }
}
