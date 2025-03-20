package com.insightdata.security;

/**
 * 密钥状态
 */
public enum KeyStatus {
    /**
     * 活跃状态，可用于加密和解密
     */
    ACTIVE,

    /**
     * 仅解密状态，只能用于解密
     */
    DECRYPT_ONLY,

    /**
     * 禁用状态，不能用于加密和解密
     */
    DISABLED
}