package com.project.construction.service;

import com.project.construction.model.Partner;
import java.util.List;
import java.util.Optional;

public interface PartnerService {
    Partner save(Partner partner);
    Optional<Partner> findById(Long id);
    List<Partner> findAll();
    void deleteById(Long id);
}
