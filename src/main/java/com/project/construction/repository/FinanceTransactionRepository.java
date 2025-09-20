package com.project.construction.repository;

import com.project.construction.model.FinanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanceTransactionRepository extends JpaRepository<FinanceTransaction, Long> {
}
