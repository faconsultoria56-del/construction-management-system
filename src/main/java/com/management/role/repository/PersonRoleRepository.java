package com.management.role.repository;

import com.management.role.model.PersonRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRoleRepository extends JpaRepository<PersonRole, Integer> {
    List<PersonRole> findByPersonId(Integer personId);
}
