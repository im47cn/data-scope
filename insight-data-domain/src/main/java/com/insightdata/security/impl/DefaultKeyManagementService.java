package com.insightdata.security.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.insightdata.security.KeyInfo;
import com.insightdata.security.KeyManagementService;
import com.insightdata.security.KeyStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认密钥管理服务实现
 */
@Slf4j
@Service
public class DefaultKeyManagementService implements KeyManagementService {

    private final Map<String, KeyInfo> keyStore = new ConcurrentHashMap<>();
    private final Map<String, String> currentKeys = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public KeyInfo createKey(String purpose) {
        LocalDateTime now = LocalDateTime.now();
        int version = getNextVersion(purpose);

        // 生成新的密钥内容
        byte[] keyBytes = new byte[32]; // 256位密钥
        secureRandom.nextBytes(keyBytes);
        String keyContent = Base64.getEncoder().encodeToString(keyBytes);

        // 创建密钥信息
        KeyInfo keyInfo = KeyInfo.builder()
                .id(UUID.randomUUID().toString())
                .version(version)
                .keyContent(keyContent)
                .purpose(purpose)
                .status(KeyStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .expiresAt(now.plusYears(1))
                .build();

        // 保存密钥
        keyStore.put(keyInfo.getId(), keyInfo);
        currentKeys.put(purpose, keyInfo.getId());

        return keyInfo;
    }

    @Override
    public Optional<KeyInfo> getCurrentKey(String purpose) {
        String currentKeyId = currentKeys.get(purpose);
        if (currentKeyId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(keyStore.get(currentKeyId));
    }

    @Override
    public Optional<KeyInfo> retrieveKeyById(String keyId) {
        return Optional.ofNullable(keyStore.get(keyId));
    }

    @Override
    public KeyInfo rotateKey(String purpose) {
        // 获取当前密钥
        Optional<KeyInfo> currentKey = getCurrentKey(purpose);
        if (currentKey.isPresent()) {
            // 将当前密钥设置为仅解密状态
            updateKeyStatus(currentKey.get().getId(), KeyStatus.DECRYPT_ONLY);
        }

        // 创建新密钥
        return createKey(purpose);
    }

    @Override
    public void disableKey(String keyId) {
        updateKeyStatus(keyId, KeyStatus.DISABLED);
    }

    @Override
    public void enableKey(String keyId) {
        updateKeyStatus(keyId, KeyStatus.ACTIVE);
    }

    @Override
    public void deleteKey(String keyId) {
        KeyInfo keyInfo = keyStore.get(keyId);
        if (keyInfo != null && keyId.equals(currentKeys.get(keyInfo.getPurpose()))) {
            currentKeys.remove(keyInfo.getPurpose());
        }
        keyStore.remove(keyId);
    }

    @Override
    public void updateKeyStatus(String keyId, KeyStatus status) {
        KeyInfo keyInfo = keyStore.get(keyId);
        if (keyInfo == null) {
            return;
        }

        // 创建更新后的密钥信息
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

        // 如果密钥被禁用且是当前活跃密钥，则移除当前密钥引用
        if (status != KeyStatus.ACTIVE && keyId.equals(currentKeys.get(keyInfo.getPurpose()))) {
            currentKeys.remove(keyInfo.getPurpose());
        }
    }

    @Override
    public List<KeyInfo> listKeys(String purpose) {
        return keyStore.values().stream()
                .filter(key -> key.getPurpose().equals(purpose))
                .sorted(Comparator.comparingInt(KeyInfo::getVersion).reversed())
                .collect(Collectors.toList());
    }

    private int getNextVersion(String purpose) {
        return keyStore.values().stream()
                .filter(key -> key.getPurpose().equals(purpose))
                .mapToInt(KeyInfo::getVersion)
                .max()
                .orElse(0) + 1;
    }
}