package com.insightdata.domain.security;

import com.insightdata.domain.security.service.KeyManagementService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Service
public class CredentialEncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private final KeyManagementService keyManagementService;

    public CredentialEncryptionService(KeyManagementService keyManagementService) {
        this.keyManagementService = keyManagementService;
    }

    public EncryptionResult encrypt(String credential, String keyId) {
        try {
            SecretKey key = keyManagementService.getKey(keyId);
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, spec);
            byte[] encryptedData = cipher.doFinal(credential.getBytes());
            String encryptedCredential = Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(encryptedData);
            return new EncryptionResult(encryptedCredential, keyId);
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new SecurityException("Failed to encrypt credential", e);
        }
    }

    public String decrypt(String encryptedCredential, String keyId) {
        try {
            String[] parts = encryptedCredential.split(":");
            if (parts.length != 2) {
                throw new SecurityException("Invalid encrypted credential format");
            }
            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] encryptedData = Base64.getDecoder().decode(parts[1]);
            SecretKey key = keyManagementService.getKey(keyId);
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            byte[] decryptedData = cipher.doFinal(encryptedData);
            return new String(decryptedData);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new SecurityException("Failed to decrypt credential", e);
        }
    }

    @Data
    public static class EncryptionResult {
        private final String encryptedCredential;
        private final String keyId;
    }
}
