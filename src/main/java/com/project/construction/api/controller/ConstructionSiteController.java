package com.project.construction.api.controller;

import com.project.construction.model.ConstructionSite;
import com.project.construction.service.ConstructionSiteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/construction-sites")
public class ConstructionSiteController {

    private final ConstructionSiteService constructionSiteService;

    public ConstructionSiteController(ConstructionSiteService constructionSiteService) {
        this.constructionSiteService = constructionSiteService;
    }

    @PostMapping
    public ConstructionSite createConstructionSite(@RequestBody ConstructionSite constructionSite) {
        return constructionSiteService.save(constructionSite);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConstructionSite> getConstructionSiteById(@PathVariable Long id) {
        Optional<ConstructionSite> constructionSite = constructionSiteService.findById(id);
        return constructionSite.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<ConstructionSite> getAllConstructionSites() {
        return constructionSiteService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConstructionSite> updateConstructionSite(@PathVariable Long id, @RequestBody ConstructionSite siteDetails) {
        Optional<ConstructionSite> siteOptional = constructionSiteService.findById(id);
        if (siteOptional.isPresent()) {
            ConstructionSite site = siteOptional.get();
            site.setName(siteDetails.getName());
            site.setLocation(siteDetails.getLocation());
            site.setStartDate(siteDetails.getStartDate());
            site.setEndDate(siteDetails.getEndDate());
            return ResponseEntity.ok(constructionSiteService.save(site));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConstructionSite(@PathVariable Long id) {
        if (constructionSiteService.findById(id).isPresent()) {
            constructionSiteService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
