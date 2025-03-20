package com.insightdata.security.impl;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.insightdata.security.EncryptionService;
import com.insightdata.security.KeyInfo;
import com.insightdata.security.KeyManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AES加密服务实现
 * 使用AES-256-GCM算法进行加密
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AESEncryptionService implements EncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    private final KeyManagementService keyManagementService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String encrypt(String plaintext, String purpose) {
        try {
            // 获取当前活跃的密钥
            Optional<KeyInfo> keyInfo = keyManagementService.getCurrentKey(purpose);
            if (keyInfo.isEmpty()) {
                throw new IllegalStateException("No active encryption key available for purpose: " + purpose);
            }

            // 从Base64解码密钥内容
            byte[] keyBytes = Base64.getDecoder().decode(keyInfo.get().getKeyContent());
            SecretKey key = new SecretKeySpec(keyBytes, "AES");

            // 生成IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);

            // 初始化加密器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);

            // 加密数据
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes());

            // 组装版本号、IV和密文
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + ciphertext.length);
            byteBuffer.putInt(keyInfo.get().getVersion());
            byteBuffer.put(iv);
            byteBuffer.put(ciphertext);

            // 返回Base64编码的结果
            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    @Override
    public String decrypt(String ciphertext, String purpose) {
        try {
            // 解码Base64
            byte[] decoded = Base64.getDecoder().decode(ciphertext);
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

            // 读取版本号
            int version = byteBuffer.getInt();

            // 获取对应版本的密钥
            Optional<KeyInfo> keyInfo = keyManagementService.retrieveKeyById(String.valueOf(version));
            if (keyInfo.isEmpty()) {
                throw new IllegalStateException("Decryption key not found for version: " + version);
            }

            // 从Base64解码密钥内容
            byte[] keyBytes = Base64.getDecoder().decode(keyInfo.get().getKeyContent());
            SecretKey key = new SecretKeySpec(keyBytes, "AES");

            // 读取IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            byteBuffer.get(iv);

            // 读取密文
            byte[] encryptedText = new byte[byteBuffer.remaining()];
            byteBuffer.get(encryptedText);

            // 初始化解密器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

            // 解密并返回结果
            byte[] decryptedText = cipher.doFinal(encryptedText);
            return new String(decryptedText);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }

    @Override
    public String reencrypt(String ciphertext, String purpose) {
        // 先解密，再用新密钥加密
        String plaintext = decrypt(ciphertext, purpose);
        return encrypt(plaintext, purpose);
    }

    @Override
    public boolean validate(String ciphertext) {
        try {
            // 解码Base64
            byte[] decoded = Base64.getDecoder().decode(ciphertext);
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);

            // 验证长度是否足够包含版本号和IV
            if (byteBuffer.remaining() < 4 + GCM_IV_LENGTH) {
                return false;
            }

            // 读取版本号
            int version = byteBuffer.getInt();

            // 验证版本号对应的密钥是否存在
            Optional<KeyInfo> keyInfo = keyManagementService.retrieveKeyById(String.valueOf(version));
            if (keyInfo.isEmpty()) {
                return false;
            }

            // 验证剩余长度是否合理
            return byteBuffer.remaining() >= GCM_IV_LENGTH;
        } catch (Exception e) {
            return false;
        }
    }
}