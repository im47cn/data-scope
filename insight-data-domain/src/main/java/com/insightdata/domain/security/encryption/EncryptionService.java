package com.insightdata.domain.security.encryption;

/**
 * 加密服务接口
 */
public interface EncryptionService {
    /**
     * 加密
     *
     * @param plaintext 明文
     * @param keyId    密钥ID
     * @return 密文
     */
    String encrypt(String plaintext, String keyId);

    /**
     * 解密
     *
     * @param ciphertext 密文
     * @param keyId     密钥ID
     * @return 明文
     */
    String decrypt(String ciphertext, String keyId);

    /**
     * 生成密钥
     *
     * @return 密钥ID
     */
    String generateKey();

    /**
     * 删除密钥
     *
     * @param keyId 密钥ID
     */
    void deleteKey(String keyId);

    /**
     * 验证密钥是否存在
     *
     * @param keyId 密钥ID
     * @return 是否存在
     */
    boolean validateKey(String keyId);

    /**
     * 获取密钥过期时间
     *
     * @param keyId 密钥ID
     * @return 过期时间戳
     */
    Long getKeyExpiration(String keyId);

    /**
     * 更新密钥过期时间
     *
     * @param keyId      密钥ID
     * @param expiration 过期时间戳
     */
    void updateKeyExpiration(String keyId, Long expiration);

    /**
     * 轮换密钥
     *
     * @param oldKeyId 旧密钥ID
     * @return 新密钥ID
     */
    String rotateKey(String oldKeyId);
}