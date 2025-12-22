package com.management.plantype.repository;

import com.management.plantype.model.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanTypeRepository extends JpaRepository<PlanType, Integer> {
    Optional<PlanType> findByCode(String code);
}
