package com.management.company.repository;

import com.management.company.model.Company;
import com.management.company.model.CompanyMember;
import com.management.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyMemberRepository extends JpaRepository<CompanyMember, Integer> {

    Optional<CompanyMember> findByCompanyAndPerson(Company company, Person person);
}
