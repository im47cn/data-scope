package com.insightdata.security;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

/**
 * 密钥信息
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class KeyInfo {
    
    /**
     * 密钥ID
     */
    private String id;
    
    /**
     * 密钥版本号
     */
    private int version;
    
    /**
     * 密钥内容(Base64编码)
     */
    private String keyContent;
    
    /**
     * 密钥用途
     */
    private String purpose;
    
    /**
     * 密钥状态
     */
    private KeyStatus status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 过期时间
     */
    private LocalDateTime expiresAt;
}