package com.insightdata.infrastructure.security;

/**
 * 密钥管理接口
 */
public interface KeyManager {
    
    /**
     * 生成新的密钥
     * @return 生成的密钥
     */
    String generateKey();
    
    /**
     * 存储密钥
     * @param keyId 密钥ID
     * @param key 密钥内容
     */
    void storeKey(String keyId, String key);
    
    /**
     * 获取密钥
     * @param keyId 密钥ID
     * @return 密钥内容
     */
    String retrieveKey(String keyId);
    
    /**
     * 删除密钥
     * @param keyId 密钥ID
     */
    void deleteKey(String keyId);
    
    /**
     * 轮换密钥
     * @param keyId 要轮换的密钥ID
     * @return 新的密钥ID
     */
    String rotateKey(String keyId);
}