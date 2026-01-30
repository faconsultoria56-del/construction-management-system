package com.management.person.repository;

import com.management.person.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    // Para login do Owner
    Optional<UserAccount> findByEmail(String email);

    // Para login do Employee (CPF)
    Optional<UserAccount> findByAccessCode(String accessCode);

    // Para validar o token de email
    Optional<UserAccount> findByVerificationToken(String token);
}
