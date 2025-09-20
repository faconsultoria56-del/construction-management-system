package com.project.construction.service.impl;

import com.project.construction.model.ConstructionSite;
import com.project.construction.repository.ConstructionSiteRepository;
import com.project.construction.service.ConstructionSiteService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConstructionSiteServiceImpl implements ConstructionSiteService {

    private final ConstructionSiteRepository constructionSiteRepository;

    public ConstructionSiteServiceImpl(ConstructionSiteRepository constructionSiteRepository) {
        this.constructionSiteRepository = constructionSiteRepository;
    }

    @Override
    public ConstructionSite save(ConstructionSite constructionSite) {
        return constructionSiteRepository.save(constructionSite);
    }

    @Override
    public Optional<ConstructionSite> findById(Long id) {
        return constructionSiteRepository.findById(id);
    }

    @Override
    public List<ConstructionSite> findAll() {
        return constructionSiteRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        constructionSiteRepository.deleteById(id);
    }
}
