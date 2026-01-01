package com.management.persondocument.repository;

import com.management.persondocument.model.PersonDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonDocumentRepository extends JpaRepository<PersonDocument, Integer> {
}
