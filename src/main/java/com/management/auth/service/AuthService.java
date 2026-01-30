package com.management.auth.service;

import com.management.person.model.UserAccount;
import com.management.person.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void generateEmailVerification(UserAccount user) {
        // 1. Gera um token único
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userAccountRepository.save(user);

        // 2. Envia o e-mail
        sendVerificationEmail(user.getEmail(), token);
    }

    private void sendVerificationEmail(String to, String token) {
        String confirmationUrl = "http://localhost:8080/auth/confirm?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject("Confirmação de Cadastro - Construction System");
        email.setText("Olá! Clique no link para verificar sua conta: " + confirmationUrl);

        mailSender.send(email);
    }
}
