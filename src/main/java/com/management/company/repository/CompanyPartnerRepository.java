package com.management.company.repository;

import com.management.company.model.CompanyPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyPartnerRepository extends JpaRepository<CompanyPartner, Integer> {
}
