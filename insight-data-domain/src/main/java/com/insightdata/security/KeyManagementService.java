package com.insightdata.security;

import java.util.List;
import java.util.Optional;

/**
 * 密钥管理服务接口
 */
public interface KeyManagementService {

    /**
     * 创建新密钥
     *
     * @param purpose 密钥用途
     * @return 新创建的密钥信息
     */
    KeyInfo createKey(String purpose);

    /**
     * 获取当前活跃密钥
     *
     * @param purpose 密钥用途
     * @return 当前活跃密钥信息
     */
    Optional<KeyInfo> getCurrentKey(String purpose);

    /**
     * 根据ID获取密钥
     *
     * @param keyId 密钥ID
     * @return 密钥信息
     */
    Optional<KeyInfo> retrieveKeyById(String keyId);

    /**
     * 轮换密钥
     *
     * @param purpose 密钥用途
     * @return 新创建的密钥信息
     */
    KeyInfo rotateKey(String purpose);

    /**
     * 禁用密钥
     *
     * @param keyId 密钥ID
     */
    void disableKey(String keyId);

    /**
     * 启用密钥
     *
     * @param keyId 密钥ID
     */
    void enableKey(String keyId);

    /**
     * 删除密钥
     *
     * @param keyId 密钥ID
     */
    void deleteKey(String keyId);

    /**
     * 更新密钥状态
     *
     * @param keyId 密钥ID
     * @param status 新状态
     */
    void updateKeyStatus(String keyId, KeyStatus status);

    /**
     * 列出指定用途的所有密钥
     *
     * @param purpose 密钥用途
     * @return 密钥列表
     */
    List<KeyInfo> listKeys(String purpose);
}