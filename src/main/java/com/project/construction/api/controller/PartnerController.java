package com.project.construction.api.controller;

import com.project.construction.api.dto.request.PartnerRequest;
import com.project.construction.api.dto.response.PartnerResponse;
import com.project.construction.model.Partner;
import com.project.construction.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/partners")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @PostMapping
    public PartnerResponse createPartner(@RequestBody PartnerRequest partnerRequest) {
        Partner partner = toEntity(partnerRequest);
        return toResponse(partnerService.save(partner));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponse> getPartnerById(@PathVariable Long id) {
        Optional<Partner> partner = partnerService.findById(id);
        return partner.map(p -> ResponseEntity.ok(toResponse(p)))
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<PartnerResponse> getAllPartners() {
        return partnerService.findAll().stream()
                             .map(this::toResponse)
                             .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerResponse> updatePartner(@PathVariable Long id, @RequestBody PartnerRequest partnerRequest) {
        Optional<Partner> partnerOptional = partnerService.findById(id);
        if (partnerOptional.isPresent()) {
            Partner partner = partnerOptional.get();
            partner.setName(partnerRequest.getName());
            partner.setCpf(partnerRequest.getCpf());
            partner.setOwnershipPercentage(partnerRequest.getOwnershipPercentage());
            return ResponseEntity.ok(toResponse(partnerService.save(partner)));
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

    private Partner toEntity(PartnerRequest request) {
        Partner partner = new Partner();
        partner.setName(request.getName());
        partner.setCpf(request.getCpf());
        partner.setOwnershipPercentage(request.getOwnershipPercentage());
        return partner;
    }

    private PartnerResponse toResponse(Partner partner) {
        PartnerResponse response = new PartnerResponse();
        response.setId(partner.getId());
        response.setName(partner.getName());
        response.setCpf(partner.getCpf());
        response.setOwnershipPercentage(partner.getOwnershipPercentage());
        return response;
    }
}
