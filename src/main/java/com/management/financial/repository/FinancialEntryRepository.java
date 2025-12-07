package com.management.financial.repository;

import com.management.financial.model.FinancialEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface FinancialEntryRepository extends JpaRepository<FinancialEntry, Integer> {

    @Query("SELECT SUM(fe.amount) FROM FinancialEntry fe WHERE fe.project.id = :projectId AND fe.type = :type")
    BigDecimal sumAmountByProjectIdAndType(@Param("projectId") Integer projectId, @Param("type") String type);
}
