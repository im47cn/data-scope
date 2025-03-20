package com.insightdata.domain.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration for the application
 * Configures encryption, password hashing, and other security-related beans
 */
@Configuration
@EnableScheduling
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CredentialEncryptionService credentialEncryptionService(KeyManagementService keyManagementService) {
        return new CredentialEncryptionService(keyManagementService);
    }

    @Bean
    public KeyManagementService keyManagementService() {
        return new KeyManagementService();
    }
}
