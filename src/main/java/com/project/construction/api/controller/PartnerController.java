package com.project.construction.api.controller;

import com.project.construction.api.dto.request.PartnerRequest;
import com.project.construction.api.dto.response.PartnerResponse;
import com.project.construction.facade.PartnerFacade;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partners")
public class PartnerController {

    private final PartnerFacade partnerFacade;

    public PartnerController(PartnerFacade partnerFacade) {
        this.partnerFacade = partnerFacade;
    }

    @PostMapping
    public PartnerResponse createPartner(@RequestBody PartnerRequest partnerRequest) {
        return partnerFacade.createPartner(partnerRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponse> getPartnerById(@PathVariable Long id) {
        return ResponseEntity.ok(partnerFacade.getPartnerById(id));
    }

    @GetMapping
    public List<PartnerResponse> getAllPartners() {
        return partnerFacade.getAllPartners();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerResponse> updatePartner(@PathVariable Long id, @RequestBody PartnerRequest partnerRequest) {
        return ResponseEntity.ok(partnerFacade.updatePartner(id, partnerRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        partnerFacade.deletePartner(id);
        return ResponseEntity.noContent().build();
    }
}
