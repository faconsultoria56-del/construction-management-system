package com.project.construction.service.impl;

import com.project.construction.model.Partner;
import com.project.construction.repository.PartnerRepository;
import com.project.construction.service.PartnerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;

    public PartnerServiceImpl(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    @Override
    public Partner save(Partner partner) {
        return partnerRepository.save(partner);
    }

    @Override
    public Optional<Partner> findById(Long id) {
        return partnerRepository.findById(id);
    }

    @Override
    public List<Partner> findAll() {
        return partnerRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        partnerRepository.deleteById(id);
    }
}
