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
     * @throws KeyManagementException 创建密钥失败时抛出
     */
    KeyInfo createKey(String purpose) throws KeyManagementException;

    /**
     * 根据ID获取密钥
     *
     * @param keyId 密钥ID
     * @return 密钥信息，如果不存在返回空
     */
    Optional<KeyInfo> retrieveKeyById(String keyId);

    /**
     * 获取指定用途的当前活跃密钥
     *
     * @param purpose 密钥用途
     * @return 当前活跃的密钥信息，如果不存在返回空
     */
    Optional<KeyInfo> retrieveCurrentKey(String purpose);

    /**
     * 获取指定用途的所有密钥
     *
     * @param purpose 密钥用途
     * @return 密钥列表
     */
    List<KeyInfo> listKeys(String purpose);

    /**
     * 轮换密钥
     *
     * @param purpose 密钥用途
     * @return 新创建的密钥信息
     * @throws KeyManagementException 轮换密钥失败时抛出
     */
    KeyInfo rotateKey(String purpose) throws KeyManagementException;

    /**
     * 禁用密钥
     *
     * @param keyId 密钥ID
     * @throws KeyManagementException 禁用密钥失败时抛出
     */
    void disableKey(String keyId) throws KeyManagementException;

    /**
     * 启用密钥
     *
     * @param keyId 密钥ID
     * @throws KeyManagementException 启用密钥失败时抛出
     */
    void enableKey(String keyId) throws KeyManagementException;

    /**
     * 删除密钥
     *
     * @param keyId 密钥ID
     * @throws KeyManagementException 删除密钥失败时抛出
     */
    void deleteKey(String keyId) throws KeyManagementException;

    /**
     * 更新密钥状态
     *
     * @param keyId 密钥ID
     * @param status 新状态
     * @throws KeyManagementException 更新状态失败时抛出
     */
    void updateKeyStatus(String keyId, KeyStatus status) throws KeyManagementException;
}