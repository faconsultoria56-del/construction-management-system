package com.project.construction.facade;

import com.project.construction.api.dto.request.PartnerRequest;
import com.project.construction.api.dto.response.PartnerResponse;
import com.project.construction.model.Partner;
import com.project.construction.service.PartnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PartnerFacade {

    private final PartnerService partnerService;

    public PartnerFacade(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @Transactional
    public PartnerResponse createPartner(PartnerRequest partnerRequest) {
        Partner partner = toEntity(partnerRequest);
        return toResponse(partnerService.save(partner));
    }

    @Transactional(readOnly = true)
    public PartnerResponse getPartnerById(Long id) {
        return toResponse(partnerService.findById(id));
    }

    @Transactional(readOnly = true)
    public List<PartnerResponse> getAllPartners() {
        return partnerService.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PartnerResponse updatePartner(Long id, PartnerRequest partnerRequest) {
        Partner partner = partnerService.findById(id);
        partner.setName(partnerRequest.getName());
        partner.setCpf(partnerRequest.getCpf());
        partner.setOwnershipPercentage(partnerRequest.getOwnershipPercentage());
        return toResponse(partnerService.save(partner));
    }

    @Transactional
    public void deletePartner(Long id) {
        partnerService.deleteById(id);
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
