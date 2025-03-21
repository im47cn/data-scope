package com.insightdata.domain.security.service;

import com.insightdata.domain.security.model.KeyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler for automatic encryption key rotation
 * Rotates keys based on configured interval
 */
@Slf4j
@Component
public class KeyRotationScheduler {

    private final KeyManagementService keyManagementService;

    @Value("${security.encryption.key-rotation-interval:30d}")
    private String keyRotationInterval;

    public KeyRotationScheduler(KeyManagementService keyManagementService) {
        this.keyManagementService = keyManagementService;
    }

    @Scheduled(cron = "${security.encryption.key-rotation-cron:0 0 0 */30 * *}")
    public void rotateKeys() {
        try {
            log.info("Starting scheduled key rotation");
            String purpose = "CREDENTIAL_ENCRYPTION"; // Default purpose for credential encryption
            KeyInfo currentKey = keyManagementService.getCurrentKey(purpose)
                .orElseThrow(() -> new SecurityException("No active key found for purpose: " + purpose));
            KeyInfo newKey = keyManagementService.rotateKey(purpose);
            log.info("Successfully rotated key from ID {} to new key ID {}", currentKey.getId(), newKey.getId());
        } catch (Exception e) {
            log.error("Failed to rotate encryption keys", e);
            throw new SecurityException("Key rotation failed", e);
        }
    }
}
