package com.insightdata.domain.security.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.insightdata.domain.security.enums.KeyStatus;
import com.insightdata.domain.security.model.KeyInfo;
import com.insightdata.domain.security.service.KeyManagementService;
import org.springframework.stereotype.Component;

/**
 * Default implementation of KeyManagementService
 */
@Component
public class KeyManagementServiceImpl implements KeyManagementService {

    private final Map<String, KeyInfo> keyStore = new ConcurrentHashMap<>();
    private final Map<String, String> currentKeys = new ConcurrentHashMap<>();

    @Override
    public KeyInfo createKey(String purpose) {
        int nextVersion = getNextKeyVersion(purpose);
        
        KeyInfo keyInfo = KeyInfo.builder()
                .id(UUID.randomUUID().toString())
                .version(nextVersion)
                .purpose(purpose)
                .keyContent(generateKeyContent())
                .status(KeyStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();

        keyStore.put(keyInfo.getId(), keyInfo);
        currentKeys.put(purpose, keyInfo.getId());
        
        return keyInfo;
    }

    @Override
    public Optional<KeyInfo> retrieveKeyById(String keyId) {
        return Optional.ofNullable(keyStore.get(keyId));
    }

    @Override
    public Optional<KeyInfo> getCurrentKey(String purpose) {
        String currentKeyId = currentKeys.get(purpose);
        return currentKeyId != null ? retrieveKeyById(currentKeyId) : Optional.empty();
    }

    @Override
    public List<KeyInfo> listKeys(String purpose) {
        return keyStore.values().stream()
                .filter(key -> key.getPurpose().equals(purpose))
                .collect(Collectors.toList());
    }

    @Override
    public KeyInfo rotateKey(String purpose) {
        Optional<KeyInfo> currentKey = getCurrentKey(purpose);
        if (currentKey.isPresent()) {
            updateKeyStatus(currentKey.get().getId(), KeyStatus.DECRYPT_ONLY);
        }
        return createKey(purpose);
    }

    @Override
    public void updateKeyStatus(String keyId, KeyStatus status) {
        KeyInfo keyInfo = keyStore.get(keyId);
        if (keyInfo == null) {
            throw new IllegalArgumentException("Key not found: " + keyId);
        }

        KeyInfo updatedKeyInfo = KeyInfo.builder()
                .id(keyInfo.getId())
                .version(keyInfo.getVersion())
                .keyContent(keyInfo.getKeyContent())
                .purpose(keyInfo.getPurpose())
                .status(status)
                .createdAt(keyInfo.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .expiresAt(keyInfo.getExpiresAt())
                .metadata(keyInfo.getMetadata())
                .build();

        keyStore.put(keyId, updatedKeyInfo);

        if (status != KeyStatus.ACTIVE && keyId.equals(currentKeys.get(keyInfo.getPurpose()))) {
            currentKeys.remove(keyInfo.getPurpose());
        }
    }

    @Override
    public int getNextKeyVersion(String purpose) {
        return keyStore.values().stream()
                .filter(key -> key.getPurpose().equals(purpose))
                .mapToInt(KeyInfo::getVersion)
                .max()
                .orElse(0) + 1;
    }

    @Override
    public void deleteKey(String keyId) {
        KeyInfo keyInfo = keyStore.get(keyId);
        if (keyInfo == null) {
            return;
        }

        if (keyId.equals(currentKeys.get(keyInfo.getPurpose()))) {
            currentKeys.remove(keyInfo.getPurpose());
        }
        keyStore.remove(keyId);
    }

    private String generateKeyContent() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}