package com.project.construction.service.impl;

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
    public Optional<MaterialRequest> findById(Long id) {
        return materialRequestRepository.findById(id);
    }

    @Override
    public List<MaterialRequest> findAll() {
        return materialRequestRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        materialRequestRepository.deleteById(id);
    }
}
