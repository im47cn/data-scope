package com.insightdata.security.impl;

import com.insightdata.security.KeyInfo;
import com.insightdata.security.KeyManagementException;
import com.insightdata.security.KeyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DefaultKeyManagementServiceTest {

    private static final String TEST_PURPOSE = "test-purpose";
    
    private DefaultKeyManagementService keyManagementService;

    @BeforeEach
    void setUp() {
        keyManagementService = new DefaultKeyManagementService();
    }

    @Test
    void createKey_Success() {
        KeyInfo keyInfo = keyManagementService.createKey(TEST_PURPOSE);
        
        assertNotNull(keyInfo);
        assertNotNull(keyInfo.getId());
        assertEquals(1, keyInfo.getVersion());
        assertEquals(TEST_PURPOSE, keyInfo.getPurpose());
        assertEquals(KeyStatus.ACTIVE, keyInfo.getStatus());
        assertNotNull(keyInfo.getKeyContent());
        assertNotNull(keyInfo.getCreatedAt());
        assertNotNull(keyInfo.getUpdatedAt());
        assertNotNull(keyInfo.getExpiresAt());
    }

    @Test
    void retrieveKeyById_ExistingKey_ReturnsKey() {
        KeyInfo createdKey = keyManagementService.createKey(TEST_PURPOSE);
        Optional<KeyInfo> retrievedKey = keyManagementService.retrieveKeyById(createdKey.getId());
        
        assertTrue(retrievedKey.isPresent());
        assertEquals(createdKey.getId(), retrievedKey.get().getId());
    }

    @Test
    void retrieveKeyById_NonExistingKey_ReturnsEmpty() {
        Optional<KeyInfo> retrievedKey = keyManagementService.retrieveKeyById("non-existing-id");
        assertFalse(retrievedKey.isPresent());
    }

    @Test
    void retrieveCurrentKey_ExistingPurpose_ReturnsKey() {
        KeyInfo createdKey = keyManagementService.createKey(TEST_PURPOSE);
        Optional<KeyInfo> currentKey = keyManagementService.retrieveCurrentKey(TEST_PURPOSE);
        
        assertTrue(currentKey.isPresent());
        assertEquals(createdKey.getId(), currentKey.get().getId());
    }

    @Test
    void retrieveCurrentKey_NonExistingPurpose_ReturnsEmpty() {
        Optional<KeyInfo> currentKey = keyManagementService.retrieveCurrentKey("non-existing-purpose");
        assertFalse(currentKey.isPresent());
    }

    @Test
    void listKeys_ReturnsKeysInVersionOrder() {
        KeyInfo key1 = keyManagementService.createKey(TEST_PURPOSE);
        KeyInfo key2 = keyManagementService.rotateKey(TEST_PURPOSE);
        
        List<KeyInfo> keys = keyManagementService.listKeys(TEST_PURPOSE);
        
        assertEquals(2, keys.size());
        assertEquals(key2.getId(), keys.get(0).getId());
        assertEquals(key1.getId(), keys.get(1).getId());
    }

    @Test
    void rotateKey_CreatesNewKeyAndDeactivatesOld() {
        KeyInfo oldKey = keyManagementService.createKey(TEST_PURPOSE);
        KeyInfo newKey = keyManagementService.rotateKey(TEST_PURPOSE);
        
        assertNotEquals(oldKey.getId(), newKey.getId());
        assertEquals(oldKey.getVersion() + 1, newKey.getVersion());
        
        Optional<KeyInfo> retrievedOldKey = keyManagementService.retrieveKeyById(oldKey.getId());
        assertTrue(retrievedOldKey.isPresent());
        assertEquals(KeyStatus.DECRYPT_ONLY, retrievedOldKey.get().getStatus());
    }

    @Test
    void disableKey_Success() {
        KeyInfo key = keyManagementService.createKey(TEST_PURPOSE);
        keyManagementService.disableKey(key.getId());
        
        Optional<KeyInfo> retrievedKey = keyManagementService.retrieveKeyById(key.getId());
        assertTrue(retrievedKey.isPresent());
        assertEquals(KeyStatus.DISABLED, retrievedKey.get().getStatus());
    }

    @Test
    void enableKey_Success() {
        KeyInfo key = keyManagementService.createKey(TEST_PURPOSE);
        keyManagementService.disableKey(key.getId());
        keyManagementService.enableKey(key.getId());
        
        Optional<KeyInfo> retrievedKey = keyManagementService.retrieveKeyById(key.getId());
        assertTrue(retrievedKey.isPresent());
        assertEquals(KeyStatus.ACTIVE, retrievedKey.get().getStatus());
    }

    @Test
    void deleteKey_Success() {
        KeyInfo key = keyManagementService.createKey(TEST_PURPOSE);
        keyManagementService.deleteKey(key.getId());
        
        Optional<KeyInfo> retrievedKey = keyManagementService.retrieveKeyById(key.getId());
        assertFalse(retrievedKey.isPresent());
    }

    @Test
    void updateKeyStatus_NonExistingKey_ThrowsException() {
        assertThrows(KeyManagementException.class, () ->
                keyManagementService.updateKeyStatus("non-existing-id", KeyStatus.ACTIVE));
    }
}