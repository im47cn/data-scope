package com.insightdata.infrastructure.security;

import org.springframework.stereotype.Component;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 密钥管理实现类
 */
@Component
public class KeyManagerImpl implements KeyManager {
    
    private static final String KEY_ALGORITHM = "AES";
    private static final int KEY_SIZE = 256;
    
    // 临时使用内存存储，生产环境应该使用安全的密钥存储系统
    private final Map<String, String> keyStore = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();
    
    @Override
    public String generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(KEY_ALGORITHM);
            keyGen.init(KEY_SIZE, secureRandom);
            SecretKey key = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate key", e);
        }
    }
    
    @Override
    public void storeKey(String keyId, String key) {
        if (keyId == null || keyId.isEmpty()) {
            throw new IllegalArgumentException("Key ID cannot be null or empty");
        }
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
        keyStore.put(keyId, key);
    }
    
    @Override
    public String retrieveKey(String keyId) {
        if (keyId == null || keyId.isEmpty()) {
            throw new IllegalArgumentException("Key ID cannot be null or empty");
        }
        String key = keyStore.get(keyId);
        if (key == null) {
            throw new IllegalStateException("Key not found: " + keyId);
        }
        return key;
    }
    
    @Override
    public void deleteKey(String keyId) {
        if (keyId == null || keyId.isEmpty()) {
            throw new IllegalArgumentException("Key ID cannot be null or empty");
        }
        if (keyStore.remove(keyId) == null) {
            throw new IllegalStateException("Key not found: " + keyId);
        }
    }
    
    @Override
    public String rotateKey(String keyId) {
        if (keyId == null || keyId.isEmpty()) {
            throw new IllegalArgumentException("Key ID cannot be null or empty");
        }
        
        // 验证旧密钥存在
        if (!keyStore.containsKey(keyId)) {
            throw new IllegalStateException("Key not found: " + keyId);
        }
        
        // 生成新密钥
        String newKey = generateKey();
        
        // 存储新密钥
        storeKey(keyId, newKey);
        
        return keyId;
    }
}