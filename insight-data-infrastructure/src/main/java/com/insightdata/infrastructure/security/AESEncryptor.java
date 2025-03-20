package com.insightdata.infrastructure.security;

import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES加密实现类
 */
@Component
public class AESEncryptor {
    
    private static final String ALGORITHM = "AES";
    private final KeyManager keyManager;
    
    public AESEncryptor(KeyManager keyManager) {
        this.keyManager = keyManager;
    }
    
    /**
     * 加密数据
     * @param data 待加密数据
     * @param keyId 密钥ID
     * @return 加密后的Base64编码字符串
     */
    public String encrypt(String data, String keyId) {
        try {
            String key = keyManager.retrieveKey(keyId);
            SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    /**
     * 解密数据
     * @param encryptedData Base64编码的加密数据
     * @param keyId 密钥ID
     * @return 解密后的原始数据
     */
    public String decrypt(String encryptedData, String keyId) {
        try {
            String key = keyManager.retrieveKey(keyId);
            SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    /**
     * 创建新的加密密钥
     * @return 新密钥的ID
     */
    public String createNewKey() {
        String keyId = java.util.UUID.randomUUID().toString();
        String key = keyManager.generateKey();
        keyManager.storeKey(keyId, key);
        return keyId;
    }
    
    /**
     * 轮换密钥
     * @param keyId 要轮换的密钥ID
     * @return 新的密钥ID
     */
    public String rotateKey(String keyId) {
        return keyManager.rotateKey(keyId);
    }
}