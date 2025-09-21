package com.project.construction.facade;

import com.project.construction.api.dto.request.ConstructionSiteRequest;
import com.project.construction.api.dto.response.ConstructionSiteResponse;
import com.project.construction.model.ConstructionSite;
import com.project.construction.service.ConstructionSiteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConstructionSiteFacade {

    private final ConstructionSiteService constructionSiteService;

    public ConstructionSiteFacade(ConstructionSiteService constructionSiteService) {
        this.constructionSiteService = constructionSiteService;
    }

    @Transactional
    public ConstructionSiteResponse createConstructionSite(ConstructionSiteRequest siteRequest) {
        ConstructionSite site = toEntity(siteRequest);
        return toResponse(constructionSiteService.save(site));
    }

    @Transactional(readOnly = true)
    public ConstructionSiteResponse getConstructionSiteById(Long id) {
        return toResponse(constructionSiteService.findById(id));
    }

    @Transactional(readOnly = true)
    public List<ConstructionSiteResponse> getAllConstructionSites() {
        return constructionSiteService.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConstructionSiteResponse updateConstructionSite(Long id, ConstructionSiteRequest siteRequest) {
        ConstructionSite site = constructionSiteService.findById(id);
        site.setName(siteRequest.getName());
        site.setLocation(siteRequest.getLocation());
        site.setStartDate(siteRequest.getStartDate());
        site.setEndDate(siteRequest.getEndDate());
        return toResponse(constructionSiteService.save(site));
    }

    @Transactional
    public void deleteConstructionSite(Long id) {
        constructionSiteService.deleteById(id);
    }

    private ConstructionSite toEntity(ConstructionSiteRequest request) {
        ConstructionSite site = new ConstructionSite();
        site.setName(request.getName());
        site.setLocation(request.getLocation());
        site.setStartDate(request.getStartDate());
        site.setEndDate(request.getEndDate());
        return site;
    }

    private ConstructionSiteResponse toResponse(ConstructionSite site) {
        ConstructionSiteResponse response = new ConstructionSiteResponse();
        response.setId(site.getId());
        response.setName(site.getName());
        response.setLocation(site.getLocation());
        response.setStartDate(site.getStartDate());
        response.setEndDate(site.getEndDate());
        return response;
    }
}
