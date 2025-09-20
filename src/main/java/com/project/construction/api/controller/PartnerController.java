package com.project.construction.api.controller;

import com.project.construction.model.Partner;
import com.project.construction.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/partners")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping
    public Partner createPartner(@RequestBody Partner partner) {
        return partnerService.save(partner);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partner> getPartnerById(@PathVariable Long id) {
        Optional<Partner> partner = partnerService.findById(id);
        return partner.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Partner> getAllPartners() {
        return partnerService.findAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partner> updatePartner(@PathVariable Long id, @RequestBody Partner partnerDetails) {
        Optional<Partner> partnerOptional = partnerService.findById(id);
        if (partnerOptional.isPresent()) {
            Partner partner = partnerOptional.get();
            partner.setName(partnerDetails.getName());
            partner.setCpf(partnerDetails.getCpf());
            partner.setOwnershipPercentage(partnerDetails.getOwnershipPercentage());
            return ResponseEntity.ok(partnerService.save(partner));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePartner(@PathVariable Long id) {
        if (partnerService.findById(id).isPresent()) {
            partnerService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
