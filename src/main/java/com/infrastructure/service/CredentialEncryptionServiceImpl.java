package com.infrastructure.service;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.domain.service.CredentialEncryptionService;

/**
 * 凭证加密服务实现类，使用AES-256算法加密数据源密码
 */
@Service
public class CredentialEncryptionServiceImpl implements CredentialEncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;
    private static final int IV_LENGTH = 16;

    @Value("${insight.security.encryption.master-key:#{environment.ENCRYPTION_MASTER_KEY}}")
    private String masterKey;

    /**
     * 加密密码，生成随机盐值
     *
     * @param plainPassword 明文密码
     * @return 加密结果，包含加密后的密码和盐值
     */
    @Override
    public EncryptionResult encrypt(String plainPassword) {
        // 生成随机盐值
        byte[] salt = generateRandomBytes(SALT_LENGTH);
        String saltBase64 = Base64.getEncoder().encodeToString(salt);
        
        // 使用盐值加密密码
        String encryptedPassword = encrypt(plainPassword, saltBase64);
        
        return new EncryptionResult(encryptedPassword, saltBase64);
    }

    /**
     * 使用指定的盐值加密密码
     *
     * @param plainPassword 明文密码
     * @param salt 盐值（Base64编码）
     * @return 加密后的密码（Base64编码）
     */
    @Override
    public String encrypt(String plainPassword, String salt) {
        try {
            // 解码盐值
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            
            // 生成密钥
            SecretKey secretKey = deriveKey(masterKey, saltBytes);
            
            // 生成随机IV
            byte[] iv = generateRandomBytes(IV_LENGTH);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            
            // 初始化加密器
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            
            // 加密
            byte[] encryptedBytes = cipher.doFinal(plainPassword.getBytes(StandardCharsets.UTF_8));
            
            // 组合IV和加密后的数据
            byte[] combined = new byte[IV_LENGTH + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, IV_LENGTH);
            System.arraycopy(encryptedBytes, 0, combined, IV_LENGTH, encryptedBytes.length);
            
            // Base64编码
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("加密密码失败", e);
        }
    }

    /**
     * 验证密码
     *
     * @param plainPassword 明文密码
     * @param encryptedPassword 加密后的密码（Base64编码）
     * @param salt 盐值（Base64编码）
     * @return 是否匹配
     */
    @Override
    public boolean verify(String plainPassword, String encryptedPassword, String salt) {
        try {
            // 解码盐值和加密密码
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            byte[] combined = Base64.getDecoder().decode(encryptedPassword);
            
            // 提取IV
            byte[] iv = new byte[IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            
            // 提取加密数据
            byte[] encryptedBytes = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, IV_LENGTH, encryptedBytes, 0, encryptedBytes.length);
            
            // 生成密钥
            SecretKey secretKey = deriveKey(masterKey, saltBytes);
            
            // 初始化解密器
            Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            
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
     * 从主密钥和盐值派生密钥
     *
     * @param masterKeyString 主密钥
     * @param salt 盐值
     * @return 派生的密钥
     */
    private SecretKey deriveKey(String masterKeyString, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(masterKeyString.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    }

    /**
     * 生成指定长度的随机字节数组
     *
     * @param length 长度
     * @return 随机字节数组
     */
    private byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }
}