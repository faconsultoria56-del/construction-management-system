package com.management.company.repository;

import com.management.company.model.CompanyPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyPartnerRepository extends JpaRepository<CompanyPartner, Long> {
    List<CompanyPartner> findByCompanyId(Long companyId);
}
