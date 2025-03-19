package com.insightdata.security;

/**
 * 加密服务接口
 * 提供数据加密和解密功能
 */
public interface EncryptionService {
    
    /**
     * 加密数据
     *
     * @param plainText 明文数据
     * @param purpose 加密用途（如：datasource-credentials, user-sensitive-data等）
     * @return 加密后的数据（格式：keyId:iv:encryptedData）
     * @throws EncryptionException 加密失败时抛出
     */
    String encrypt(String plainText, String purpose) throws EncryptionException;
    
    /**
     * 解密数据
     *
     * @param encryptedText 加密的数据（格式：keyId:iv:encryptedData）
     * @param purpose 加密用途
     * @return 解密后的明文
     * @throws EncryptionException 解密失败时抛出
     */
    String decrypt(String encryptedText, String purpose) throws EncryptionException;
    
    /**
     * 验证加密数据的有效性
     *
     * @param encryptedText 加密的数据
     * @return 如果数据格式正确且使用的密钥仍然有效则返回true
     */
    boolean validate(String encryptedText);
    
    /**
     * 重新加密数据（使用新密钥）
     *
     * @param encryptedText 原加密数据
     * @param purpose 加密用途
     * @return 使用新密钥加密的数据
     * @throws EncryptionException 重新加密失败时抛出
     */
    String reencrypt(String encryptedText, String purpose) throws EncryptionException;
}