package com.management.person.repository;

import com.management.person.model.GlobalIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GlobalIdentityRepository extends JpaRepository<GlobalIdentity, Long> {

    // Busca todas as identidades globais por CPF para listar as empresas
    List<GlobalIdentity> findAllByDocument(String document);

    // Verifica se o usuário já tem vínculo com aquela empresa específica
    Optional<GlobalIdentity> findByDocumentAndCompanyId(String document, Long companyId);
}
