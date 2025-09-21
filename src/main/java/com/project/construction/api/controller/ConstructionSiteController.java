package com.project.construction.api.controller;

import com.project.construction.api.dto.request.ConstructionSiteRequest;
import com.project.construction.api.dto.response.ConstructionSiteResponse;
import com.project.construction.facade.ConstructionSiteFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/construction-sites")
public class ConstructionSiteController {

    private final ConstructionSiteFacade constructionSiteFacade;

    public ConstructionSiteController(ConstructionSiteFacade constructionSiteFacade) {
        this.constructionSiteFacade = constructionSiteFacade;
    }

    @PostMapping
    public ConstructionSiteResponse createConstructionSite(@RequestBody ConstructionSiteRequest siteRequest) {
        return constructionSiteFacade.createConstructionSite(siteRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConstructionSiteResponse> getConstructionSiteById(@PathVariable Long id) {
        return ResponseEntity.ok(constructionSiteFacade.getConstructionSiteById(id));
    }

    @GetMapping
    public List<ConstructionSiteResponse> getAllConstructionSites() {
        return constructionSiteFacade.getAllConstructionSites();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConstructionSiteResponse> updateConstructionSite(@PathVariable Long id, @RequestBody ConstructionSiteRequest siteRequest) {
        return ResponseEntity.ok(constructionSiteFacade.updateConstructionSite(id, siteRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConstructionSite(@PathVariable Long id) {
        constructionSiteFacade.deleteConstructionSite(id);
        return ResponseEntity.noContent().build();
    }
}
