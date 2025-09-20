package com.project.construction.service;

import com.project.construction.model.MaterialRequest;
import java.util.List;
import java.util.Optional;

public interface MaterialRequestService {
    MaterialRequest save(MaterialRequest materialRequest);
    Optional<MaterialRequest> findById(Long id);
    List<MaterialRequest> findAll();
    void deleteById(Long id);
}
