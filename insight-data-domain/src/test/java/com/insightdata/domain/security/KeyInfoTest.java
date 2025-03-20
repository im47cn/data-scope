package com.insightdata.domain.security;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class KeyInfoTest {

    @Test
    void builder_ShouldCreateKeyInfo_WithAllProperties() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        
        // When
        KeyInfo keyInfo = KeyInfo.builder()
                .id("test-key-1")
                .version(1)
                .purpose("CREDENTIAL_ENCRYPTION")
                .status(KeyStatus.ACTIVE)
                .keyContent("test-key-content")
                .createdAt(now)
                .updatedAt(now)
                .expiresAt(now.plusDays(30))
                .build();

        // Then
        assertEquals("test-key-1", keyInfo.getId());
        assertEquals(1, keyInfo.getVersion());
        assertEquals("CREDENTIAL_ENCRYPTION", keyInfo.getPurpose());
        assertEquals(KeyStatus.ACTIVE, keyInfo.getStatus());
        assertEquals("test-key-content", keyInfo.getKeyContent());
        assertEquals(now, keyInfo.getCreatedAt());
        assertEquals(now, keyInfo.getUpdatedAt());
        assertEquals(now.plusDays(30), keyInfo.getExpiresAt());
    }

    @Test
    void equals_ShouldReturnTrue_WhenSameContent() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        KeyInfo keyInfo1 = KeyInfo.builder()
                .id("test-key-1")
                .version(1)
                .status(KeyStatus.ACTIVE)
                .createdAt(now)
                .build();

        KeyInfo keyInfo2 = KeyInfo.builder()
                .id("test-key-1")
                .version(1)
                .status(KeyStatus.ACTIVE)
                .createdAt(now)
                .build();

        // When/Then
        assertEquals(keyInfo1, keyInfo2);
        assertEquals(keyInfo1.hashCode(), keyInfo2.hashCode());
    }

    @Test
    void equals_ShouldReturnFalse_WhenDifferentContent() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        KeyInfo keyInfo1 = KeyInfo.builder()
                .id("test-key-1")
                .version(1)
                .status(KeyStatus.ACTIVE)
                .createdAt(now)
                .build();

        KeyInfo keyInfo2 = KeyInfo.builder()
                .id("test-key-2")
                .version(1)
                .status(KeyStatus.ACTIVE)
                .createdAt(now)
                .build();

        // When/Then
        assertNotEquals(keyInfo1, keyInfo2);
        assertNotEquals(keyInfo1.hashCode(), keyInfo2.hashCode());
    }
}
