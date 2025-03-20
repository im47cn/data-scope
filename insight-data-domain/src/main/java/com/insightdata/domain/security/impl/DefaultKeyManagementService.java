package com.insightdata.domain.security.impl;

import com.insightdata.domain.security.KeyStatus;
import com.insightdata.domain.security.model.KeyInfo;
import com.insightdata.security.KeyManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class DefaultKeyManagementService implements KeyManagementService {

    private static final int KEY_SIZE = 256;
    private static final String KEY_ALGORITHM = "AES";

    @Value("${security.encryption.master-key}")
    private String masterKey;

    private final Map<String, SecretKey> keyCache = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    @PostConstruct
    public void init() {
        if (keyCache.isEmpty()) {
            createKey("default");
        }
    }

    @Override
    public KeyInfo createKey(String purpose) {
        try {
            byte[] keyBytes = new byte[KEY_SIZE / 8];
            secureRandom.nextBytes(keyBytes);
            String keyId = Base64.getEncoder().encodeToString(keyBytes).substring(0, 8);
            SecretKey key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            keyCache.put(keyId, key);

            KeyInfo.KeyInfoBuilder builder = KeyInfo.builder();
            return builder.id(keyId)
                    .version(1)
                    .keyContent(Base64.getEncoder().encodeToString(keyBytes))
                    .purpose(purpose)
                    .status(KeyStatus.ACTIVE)
                    .build();
        } catch (Exception e) {
            log.error("Failed to generate new key: {}", e.getMessage(), e);
            throw new SecurityException("Key generation failed", e);
        }
    }

    @Override
    public Optional<KeyInfo> getCurrentKey(String purpose) {
        if (keyCache.isEmpty()) {
            return Optional.empty();
        }
        String keyId = keyCache.keySet().iterator().next();
        SecretKey secretKey = keyCache.get(keyId);

        byte[] keyBytes = secretKey.getEncoded();

        KeyInfo.KeyInfoBuilder builder = KeyInfo.builder();
        return Optional.of(builder.id(keyId)
                .version(1)
                .keyContent(Base64.getEncoder().encodeToString(keyBytes))
                .purpose(purpose)
                .status(KeyStatus.ACTIVE)
                .build());
    }

    @Override
    public Optional<KeyInfo> retrieveKeyById(String keyId) {
        if (!keyCache.containsKey(keyId)) {
            return Optional.empty();
        }
        SecretKey secretKey = keyCache.get(keyId);
        byte[] keyBytes = secretKey.getEncoded();

        KeyInfo.KeyInfoBuilder builder = KeyInfo.builder();
        return Optional.of(builder.id(keyId)
                .version(1)
                .keyContent(Base64.getEncoder().encodeToString(keyBytes))
                .purpose("default")
                .status(KeyStatus.ACTIVE)
                .build());
    }

    @Override
    public KeyInfo rotateKey(String purpose) {
        keyCache.clear();
        return createKey(purpose);
    }

    @Override
    public void disableKey(String keyId) {
        keyCache.remove(keyId);
    }

    @Override
    public void enableKey(String keyId) {
        // Not implemented
    }

    @Override
    public void deleteKey(String keyId) {
        // Not implemented
    }

    @Override
    public void updateKeyStatus(String keyId, KeyStatus status) {
        // Not implemented
    }

    @Override
    public List<KeyInfo> listKeys(String purpose) {
        return null;
    }

    @Override
    public String getCurrentKeyId() {
        if (keyCache.isEmpty()) {
            return null;
        }
        return keyCache.keySet().iterator().next();
    }

    private SecretKey generateNewKey() {
        try {
            byte[] keyBytes = new byte[KEY_SIZE / 8];
            secureRandom.nextBytes(keyBytes);
            String keyId = Base64.getEncoder().encodeToString(keyBytes).substring(0, 8);
            SecretKey key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            keyCache.put(keyId, key);
            return key;
        } catch (Exception e) {
            log.error("Failed to generate new key: {}", e.getMessage(), e);
            throw new SecurityException("Key generation failed", e);
        }
    }
}