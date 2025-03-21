package com.insightdata.domain.security;

import com.insightdata.domain.security.service.KeyManagementService;
import com.insightdata.domain.security.service.impl.KeyManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class KeyManagementServiceTest {

    private KeyManagementService keyManagementService;

    @BeforeEach
    void setUp() {
        keyManagementService = new KeyManagementServiceImpl();
        ReflectionTestUtils.setField(keyManagementService, "masterKey", "test-master-key-123");
        keyManagementService.init();
    }

    @Test
    void getKey_ShouldReturnSameKey_ForSameKeyId() {
        // Given
        String keyId = "test-key-1";

        // When
        SecretKey key1 = keyManagementService.getKey(keyId);
        SecretKey key2 = keyManagementService.getKey(keyId);

        // Then
        assertNotNull(key1);
        assertNotNull(key2);
        assertEquals(key1, key2);
    }

    @Test
    void getKey_ShouldReturnDifferentKeys_ForDifferentKeyIds() {
        // Given
        String keyId1 = "test-key-1";
        String keyId2 = "test-key-2";

        // When
        SecretKey key1 = keyManagementService.getKey(keyId1);
        SecretKey key2 = keyManagementService.getKey(keyId2);

        // Then
        assertNotNull(key1);
        assertNotNull(key2);
        assertNotEquals(key1, key2);
    }

    @Test
    void rotateKey_ShouldGenerateNewKey() {
        // Given
        String keyId = "test-key-1";
        SecretKey originalKey = keyManagementService.getKey(keyId);

        // When
        keyManagementService.rotateKey(keyId);
        SecretKey newKey = keyManagementService.getKey(keyId);

        // Then
        assertNotNull(originalKey);
        assertNotNull(newKey);
        assertNotEquals(originalKey, newKey);
    }

    @Test
    void getCurrentKeyId_ShouldReturnValidKeyId() {
        // When
        String keyId = keyManagementService.getCurrentKeyId();

        // Then
        assertNotNull(keyId);
        assertFalse(keyId.isEmpty());
    }
}
