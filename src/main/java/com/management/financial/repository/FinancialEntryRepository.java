package com.management.financial.repository;

import com.management.financial.model.FinancialEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialEntryRepository extends JpaRepository<FinancialEntry, Integer> {
}
