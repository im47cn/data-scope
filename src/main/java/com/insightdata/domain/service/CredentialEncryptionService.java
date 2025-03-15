package com.insightdata.domain.service;

/**
 * 凭证加密服务接口
 */
public interface CredentialEncryptionService {
    
    /**
     * 加密密码
     *
     * @param plainPassword 明文密码
     * @return 加密结果，包含加密后的密码和盐值
     */
    EncryptionResult encrypt(String plainPassword);
    
    /**
     * 使用指定的盐值加密密码
     *
     * @param plainPassword 明文密码
     * @param salt 盐值
     * @return 加密后的密码
     */
    String encrypt(String plainPassword, String salt);
    
    /**
     * 验证密码
     *
     * @param plainPassword 明文密码
     * @param encryptedPassword 加密后的密码
     * @param salt 盐值
     * @return 是否匹配
     */
    boolean verify(String plainPassword, String encryptedPassword, String salt);
    
    /**
     * 加密结果
     */
    class EncryptionResult {
        private final String encryptedPassword;
        private final String salt;
        
        public EncryptionResult(String encryptedPassword, String salt) {
            this.encryptedPassword = encryptedPassword;
            this.salt = salt;
        }
        
        public String getEncryptedPassword() {
            return encryptedPassword;
        }
        
        public String getSalt() {
            return salt;
        }
    }
}