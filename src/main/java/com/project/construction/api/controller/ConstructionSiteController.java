package com.project.construction.api.controller;

import com.project.construction.api.dto.request.ConstructionSiteRequest;
import com.project.construction.api.dto.response.ConstructionSiteResponse;
import com.project.construction.model.ConstructionSite;
import com.project.construction.service.ConstructionSiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/construction-sites")
public class ConstructionSiteController {

    private final ConstructionSiteService constructionSiteService;

    public ConstructionSiteController(ConstructionSiteService constructionSiteService) {
        this.constructionSiteService = constructionSiteService;
    }

    @PostMapping
    public ConstructionSiteResponse createConstructionSite(@RequestBody ConstructionSiteRequest siteRequest) {
        ConstructionSite site = toEntity(siteRequest);
        return toResponse(constructionSiteService.save(site));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConstructionSiteResponse> getConstructionSiteById(@PathVariable Long id) {
        ConstructionSite site = constructionSiteService.findById(id);
        return ResponseEntity.ok(toResponse(site));
    }

    @GetMapping
    public List<ConstructionSiteResponse> getAllConstructionSites() {
        return constructionSiteService.findAll().stream()
                                      .map(this::toResponse)
                                      .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConstructionSiteResponse> updateConstructionSite(@PathVariable Long id, @RequestBody ConstructionSiteRequest siteRequest) {
        ConstructionSite site = constructionSiteService.findById(id);
        site.setName(siteRequest.getName());
        site.setLocation(siteRequest.getLocation());
        site.setStartDate(siteRequest.getStartDate());
        site.setEndDate(siteRequest.getEndDate());
        return ResponseEntity.ok(toResponse(constructionSiteService.save(site)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConstructionSite(@PathVariable Long id) {
        constructionSiteService.deleteById(id);
        return ResponseEntity.noContent().build();
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
