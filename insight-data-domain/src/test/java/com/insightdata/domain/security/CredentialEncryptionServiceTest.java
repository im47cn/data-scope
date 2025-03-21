package com.insightdata.domain.security;

import com.insightdata.domain.security.service.KeyManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CredentialEncryptionServiceTest {

    @Mock
    private KeyManagementService keyManagementService;

    private CredentialEncryptionService encryptionService;
    private SecretKey testKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Generate a test key
        byte[] keyBytes = new byte[256 / 8];
        new SecureRandom().nextBytes(keyBytes);
        testKey = new SecretKeySpec(keyBytes, "AES");
        
        when(keyManagementService.getKey(anyString())).thenReturn(testKey);
        encryptionService = new CredentialEncryptionService(keyManagementService);
    }

    @Test
    void encryptAndDecrypt_ShouldReturnOriginalValue() {
        // Given
        String originalCredential = "test-credential-123";
        String keyId = "test-key-1";

        // When
        CredentialEncryptionService.EncryptionResult result = encryptionService.encrypt(originalCredential, keyId);
        String decrypted = encryptionService.decrypt(result.getEncryptedCredential(), keyId);

        // Then
        assertNotNull(result.getEncryptedCredential());
        assertNotEquals(originalCredential, result.getEncryptedCredential());
        assertEquals(originalCredential, decrypted);
    }

    @Test
    void encrypt_ShouldGenerateDifferentCiphertextForSameInput() {
        // Given
        String credential = "test-credential-123";
        String keyId = "test-key-1";

        // When
        String encrypted1 = encryptionService.encrypt(credential, keyId).getEncryptedCredential();
        String encrypted2 = encryptionService.encrypt(credential, keyId).getEncryptedCredential();

        // Then
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    void decrypt_ShouldThrowException_WhenInvalidFormat() {
        // Given
        String invalidEncrypted = "invalid-format";
        String keyId = "test-key-1";

        // When/Then
        assertThrows(SecurityException.class, () -> 
            encryptionService.decrypt(invalidEncrypted, keyId)
        );
    }
}
