package com.management.auth.service;

import com.management.person.model.UserAccount;
import com.management.person.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private AuthService authService;

    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount();
        userAccount.setEmail("test@example.com");
    }

    @Test
    void testGenerateEmailVerification() {
        // Arrange
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);

        // Act
        authService.generateEmailVerification(userAccount);

        // Assert
        assertNotNull(userAccount.getVerificationToken());
        verify(userAccountRepository).save(userAccount);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
