package com.insightdata.infrastructure.service;

import com.insightdata.domain.service.CredentialEncryptionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * AES加密实现的凭证加密服务
 */
@Service
public class AESCredentialEncryptionService implements CredentialEncryptionService {
    
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int KEY_LENGTH = 256;
    private static final int ITERATION_COUNT = 65536;
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;
    
    @Value("${insight.security.encryption.secret-key:your-secret-key-here}")
    private String secretKey;
    
    @Override
    public EncryptionResult encrypt(String plainPassword) {
        try {
            // 生成随机盐值
            byte[] salt = generateRandomBytes(SALT_LENGTH);
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            
            // 使用盐值加密密码
            String encryptedPassword = encrypt(plainPassword, saltBase64);
            
            return new EncryptionResult(encryptedPassword, saltBase64);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt password", e);
        }
    }
    
    @Override
    public String encrypt(String plainPassword, String salt) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            
            // 生成随机IV
            byte[] iv = generateRandomBytes(IV_LENGTH);
            
            // 从密码和盐值生成密钥
            SecretKey key = generateKey(secretKey, saltBytes);
            
            // 初始化加密器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            
            // 加密
            byte[] encryptedBytes = cipher.doFinal(plainPassword.getBytes(StandardCharsets.UTF_8));
            
            // 组合IV和加密后的数据
            byte[] combined = new byte[IV_LENGTH + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
            System.arraycopy(encryptedBytes, 0, combined, IV_LENGTH, encryptedBytes.length);
            
            // Base64编码
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt password", e);
        }
    }
    
    @Override
    public boolean verify(String plainPassword, String encryptedPassword, String salt) {
        try {
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            byte[] combined = Base64.getDecoder().decode(encryptedPassword);
            
            // 提取IV
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            
            // 提取加密数据
            byte[] encryptedBytes = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);
            
            // 从密码和盐值生成密钥
            SecretKey key = generateKey(secretKey, saltBytes);
            
            // 初始化解密器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            
            // 解密
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            String decryptedPassword = new String(decryptedBytes, StandardCharsets.UTF_8);
            
            // 比较
            return plainPassword.equals(decryptedPassword);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 生成随机字节数组
     */
    private byte[] generateRandomBytes(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return bytes;
    }
    
    /**
     * 从密码和盐值生成密钥
     */
    private SecretKey generateKey(String password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
}