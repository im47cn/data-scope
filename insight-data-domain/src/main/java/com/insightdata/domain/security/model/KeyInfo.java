package com.insightdata.domain.security.model;

import java.time.LocalDateTime;
import java.util.Map;

import com.insightdata.domain.security.enums.KeyStatus;

import lombok.Builder;
import lombok.Data;

/**
 * Represents encryption key information
 */
@Data
@Builder
public class KeyInfo {
    
    /**
     * Unique identifier for the key
     */
    private String id;
    
    /**
     * Version number of the key
     */
    private int version;
    
    /**
     * Purpose/usage of the key (e.g. "CREDENTIAL_ENCRYPTION")
     */
    private String purpose;
    
    /**
     * Current status of the key
     */
    private KeyStatus status;
    
    /**
     * The actual key content
     */
    private String keyContent;
    
    /**
     * When the key was created
     */
    private LocalDateTime createdAt;
    
    /**
     * When the key was last updated
     */
    private LocalDateTime updatedAt;
    
    /**
     * When the key expires
     */
    private LocalDateTime expiresAt;
    
    /**
     * Additional metadata for the key
     */
    private Map<String, String> metadata;
}