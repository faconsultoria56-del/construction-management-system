package com.management.person.repository;

import com.management.person.model.PersonDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDocumentRepository extends JpaRepository<PersonDocument, Integer> {
}
