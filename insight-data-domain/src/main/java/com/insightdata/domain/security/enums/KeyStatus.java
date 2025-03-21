package com.insightdata.domain.security.enums;

/**
 * Represents the status of an encryption key
 */
public enum KeyStatus {
    /**
     * Key is active and can be used for both encryption and decryption
     */
    ACTIVE,
    
    /**
     * Key can only be used for decryption of existing data
     */
    DECRYPT_ONLY,
    
    /**
     * Key is disabled and cannot be used
     */
    DISABLED,
    
    /**
     * Key has been revoked due to security concerns
     */
    REVOKED,
    
    /**
     * Key has expired
     */
    EXPIRED
}