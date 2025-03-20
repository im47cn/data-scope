package com.insightdata.domain.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyStatusTest {

    @Test
    void keyStatus_ShouldHaveCorrectValues() {
        // When/Then
        assertEquals(5, KeyStatus.values().length);
        assertArrayEquals(new KeyStatus[] {
            KeyStatus.ACTIVE,
            KeyStatus.DECRYPT_ONLY,
            KeyStatus.DISABLED,
            KeyStatus.EXPIRED,
            KeyStatus.REVOKED
        }, KeyStatus.values());
    }

    @Test
    void valueOf_ShouldReturnCorrectEnum() {
        // When/Then
        assertEquals(KeyStatus.ACTIVE, KeyStatus.valueOf("ACTIVE"));
        assertEquals(KeyStatus.DECRYPT_ONLY, KeyStatus.valueOf("DECRYPT_ONLY"));
        assertEquals(KeyStatus.DISABLED, KeyStatus.valueOf("DISABLED"));
        assertEquals(KeyStatus.EXPIRED, KeyStatus.valueOf("EXPIRED"));
        assertEquals(KeyStatus.REVOKED, KeyStatus.valueOf("REVOKED"));
    }

    @Test
    void valueOf_ShouldThrowException_ForInvalidValue() {
        // When/Then
        assertThrows(IllegalArgumentException.class, () -> 
            KeyStatus.valueOf("INVALID_STATUS")
        );
    }
}
