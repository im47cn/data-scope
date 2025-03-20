package com.insightdata.security;

/**
 * 加密服务接口
 */
public interface EncryptionService {

    /**
     * 加密数据
     *
     * @param plaintext 明文数据
     * @param purpose 加密用途
     * @return 密文
     */
    String encrypt(String plaintext, String purpose);

    /**
     * 解密数据
     *
     * @param ciphertext 密文数据
     * @param purpose 加密用途
     * @return 明文
     */
    String decrypt(String ciphertext, String purpose);

    /**
     * 重新加密数据
     * 使用当前活跃密钥重新加密数据
     *
     * @param ciphertext 密文数据
     * @param purpose 加密用途
     * @return 新密文
     */
    String reencrypt(String ciphertext, String purpose);

    /**
     * 验证密文格式是否有效
     *
     * @param ciphertext 密文数据
     * @return 是否有效
     */
    boolean validate(String ciphertext);
}