package com.management.company.repository;

import com.management.company.model.CompanyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyMemberRepository extends JpaRepository<CompanyMember, Long> {
    List<CompanyMember> findByCompanyId(Long companyId);
}
