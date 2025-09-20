package com.project.construction.service;

import com.project.construction.model.ConstructionSite;
import java.util.List;
import java.util.Optional;

public interface ConstructionSiteService {
    ConstructionSite save(ConstructionSite constructionSite);
    Optional<ConstructionSite> findById(Long id);
    List<ConstructionSite> findAll();
    void deleteById(Long id);
}
