package com.insightdata.domain.security.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Builder;

/**
 * 密钥信息
 */
@Data
@Builder
public class KeyInfo {
    
    /**
     * 密钥ID
     */
    private String id;
    
    /**
     * 密钥类型
     */
    private String type;
    
    /**
     * 密钥值
     */
    private String value;
    
    /**
     * 密钥状态
     */
    private KeyStatus status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 过期时间
     */
    private LocalDateTime expiresAt;
    
    /**
     * 版本号
     */
    private Integer version;
    
    /**
     * 密钥算法
     */
    private String algorithm;
    
    /**
     * 密钥长度
     */
    private Integer keySize;
    
    /**
     * 密钥用途
     */
    private String purpose;
    
    /**
     * 密钥状态枚举
     */
    public enum KeyStatus {
        ACTIVE,
        INACTIVE,
        EXPIRED,
        COMPROMISED
    }
}