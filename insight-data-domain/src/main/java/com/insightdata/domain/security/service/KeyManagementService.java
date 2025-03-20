package com.insightdata.domain.security.service;

import com.insightdata.domain.security.model.KeyInfo;

/**
 * 密钥管理服务接口
 */
public interface KeyManagementService {
    
    /**
     * 创建新密钥
     * @param keyType 密钥类型
     * @return 密钥信息
     */
    KeyInfo createKey(String keyType);
    
    /**
     * 获取密钥
     * @param keyId 密钥ID
     * @return 密钥信息
     */
    KeyInfo getKey(String keyId);
    
    /**
     * 删除密钥
     * @param keyId 密钥ID
     */
    void deleteKey(String keyId);
    
    /**
     * 轮换密钥
     * @param keyId 密钥ID
     * @return 新的密钥信息
     */
    KeyInfo rotateKey(String keyId);
}