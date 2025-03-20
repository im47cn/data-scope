package com.insightdata.domain.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class KeyManagementService {

    private static final int KEY_SIZE = 256;
    private static final String KEY_ALGORITHM = "AES";
    
    @Value("${security.encryption.master-key}")
    private String masterKey;
    
    private final Map<String, SecretKey> keyCache = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    @PostConstruct
    public void init() {
        if (keyCache.isEmpty()) {
            generateNewKey();
        }
    }

    public SecretKey getKey(String keyId) {
        return keyCache.computeIfAbsent(keyId, k -> generateNewKey());
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
            log.error("Failed to generate new key", e);
            throw new SecurityException("Key generation failed", e);
        }
    }

    public void rotateKey(String keyId) {
        keyCache.remove(keyId);
        generateNewKey();
    }

    public String getCurrentKeyId() {
        return keyCache.keySet().iterator().next();
    }
}
