package com.insightdata.security.impl;

import com.insightdata.security.EncryptionException;
import com.insightdata.security.EncryptionService;
import com.insightdata.security.KeyInfo;
import com.insightdata.security.KeyManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

/**
 * AES-GCM 加密服务实现
 */
@Service
public class AESEncryptionService implements EncryptionService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    
    private final KeyManagementService keyManagementService;
    private final SecureRandom secureRandom;

    @Autowired
    public AESEncryptionService(KeyManagementService keyManagementService) {
        this.keyManagementService = keyManagementService;
        this.secureRandom = new SecureRandom();
    }

    @Override
    public String encrypt(String plainText, String purpose) throws EncryptionException {
        try {
            Optional<KeyInfo> keyInfo = keyManagementService.retrieveCurrentKey(purpose);
            if (!keyInfo.isPresent()) {
                throw new EncryptionException("No valid key found for purpose: " + purpose);
            }

            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);

            SecretKeySpec keySpec = new SecretKeySpec(
                Base64.getDecoder().decode(keyInfo.get().getKeyContent()), 
                "AES"
            );
            
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] cipherText = cipher.doFinal(plainText.getBytes());
            
            ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + cipherText.length);
            byteBuffer.putInt(keyInfo.get().getVersion());
            byteBuffer.put(iv);
            byteBuffer.put(cipherText);

            return Base64.getEncoder().encodeToString(byteBuffer.array());
            
        } catch (Exception e) {
            throw new EncryptionException("Encryption failed", e);
        }
    }

    @Override
    public String decrypt(String encryptedText, String purpose) throws EncryptionException {
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
            
            int keyVersion = byteBuffer.getInt();
            
            byte[] iv = new byte[GCM_IV_LENGTH];
            byteBuffer.get(iv);
            
            byte[] cipherText = new byte[byteBuffer.remaining()];
            byteBuffer.get(cipherText);

            Optional<KeyInfo> keyInfo = keyManagementService.retrieveKeyById(String.valueOf(keyVersion));
            if (!keyInfo.isPresent()) {
                throw new EncryptionException("No key found for version: " + keyVersion);
            }

            SecretKeySpec keySpec = new SecretKeySpec(
                Base64.getDecoder().decode(keyInfo.get().getKeyContent()), 
                "AES"
            );
            
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText);
            
        } catch (Exception e) {
            throw new EncryptionException("Decryption failed", e);
        }
    }

    @Override
    public boolean validate(String encryptedText) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
            
            int keyVersion = byteBuffer.getInt();
            return keyManagementService.retrieveKeyById(String.valueOf(keyVersion)).isPresent();
            
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String reencrypt(String encryptedText, String purpose) throws EncryptionException {
        String decrypted = decrypt(encryptedText, purpose);
        return encrypt(decrypted, purpose);
    }
}