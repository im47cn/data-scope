package com.insightdata.security.impl;

import com.insightdata.security.KeyInfo;
import com.insightdata.security.KeyManagementException;
import com.insightdata.security.KeyManagementService;
import com.insightdata.security.KeyStatus;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认密钥管理服务实现
 */
@Service
public class DefaultKeyManagementService implements KeyManagementService {

    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    
    // 内存中存储密钥，实际应用中应该使用数据库或KMS存储
    private final Map<String, KeyInfo> keyStore;
    private final Map<String, String> currentKeys;

    public DefaultKeyManagementService() {
        this.keyStore = new ConcurrentHashMap<>();
        this.currentKeys = new ConcurrentHashMap<>();
    }

    @Override
    public KeyInfo createKey(String purpose) throws KeyManagementException {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
            keyGen.init(KEY_SIZE);
            SecretKey secretKey = keyGen.generateKey();
            
            String keyId = UUID.randomUUID().toString();
            int version = getCurrentVersion(purpose) + 1;
            
            KeyInfo keyInfo = KeyInfo.builder()
                    .id(keyId)
                    .version(version)
                    .keyContent(Base64.getEncoder().encodeToString(secretKey.getEncoded()))
                    .purpose(purpose)
                    .status(KeyStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusMonths(3)) // 3个月后过期
                    .build();
            
            keyStore.put(keyId, keyInfo);
            currentKeys.put(purpose, keyId);
            
            return keyInfo;
            
        } catch (NoSuchAlgorithmException e) {
            throw new KeyManagementException("Failed to generate key", e);
        }
    }

    @Override
    public Optional<KeyInfo> retrieveKeyById(String keyId) {
        return Optional.ofNullable(keyStore.get(keyId));
    }

    @Override
    public Optional<KeyInfo> retrieveCurrentKey(String purpose) {
        String currentKeyId = currentKeys.get(purpose);
        return currentKeyId != null ? retrieveKeyById(currentKeyId) : Optional.empty();
    }

    @Override
    public List<KeyInfo> listKeys(String purpose) {
        return keyStore.values().stream()
                .filter(key -> key.getPurpose().equals(purpose))
                .sorted(Comparator.comparingInt(KeyInfo::getVersion).reversed())
                .toList();
    }

    @Override
    public KeyInfo rotateKey(String purpose) throws KeyManagementException {
        Optional<KeyInfo> currentKey = retrieveCurrentKey(purpose);
        if (currentKey.isPresent()) {
            updateKeyStatus(currentKey.get().getId(), KeyStatus.DECRYPT_ONLY);
        }
        return createKey(purpose);
    }

    @Override
    public void disableKey(String keyId) throws KeyManagementException {
        updateKeyStatus(keyId, KeyStatus.DISABLED);
    }

    @Override
    public void enableKey(String keyId) throws KeyManagementException {
        updateKeyStatus(keyId, KeyStatus.ACTIVE);
    }

    @Override
    public void deleteKey(String keyId) throws KeyManagementException {
        KeyInfo keyInfo = keyStore.remove(keyId);
        if (keyInfo != null && keyId.equals(currentKeys.get(keyInfo.getPurpose()))) {
            currentKeys.remove(keyInfo.getPurpose());
        }
    }

    @Override
    public void updateKeyStatus(String keyId, KeyStatus status) throws KeyManagementException {
        KeyInfo keyInfo = keyStore.get(keyId);
        if (keyInfo == null) {
            throw new KeyManagementException("Key not found: " + keyId);
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
                .build();
        
        keyStore.put(keyId, updatedKeyInfo);
        
        if (status != KeyStatus.ACTIVE && keyId.equals(currentKeys.get(keyInfo.getPurpose()))) {
            currentKeys.remove(keyInfo.getPurpose());
        }
    }

    private int getCurrentVersion(String purpose) {
        return keyStore.values().stream()
                .filter(key -> key.getPurpose().equals(purpose))
                .mapToInt(KeyInfo::getVersion)
                .max()
                .orElse(0);
    }
}