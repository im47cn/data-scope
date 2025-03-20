package com.insightdata.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyManagerImplTest {

    private KeyManagerImpl keyManager;

    @BeforeEach
    void setUp() {
        keyManager = new KeyManagerImpl();
    }

    @Test
    void testGenerateKey() {
        String key = keyManager.generateKey();
        assertNotNull(key);
        assertTrue(key.length() > 0);
    }

    @Test
    void testStoreAndRetrieveKey() {
        String keyId = "test-key-id";
        String key = keyManager.generateKey();
        
        keyManager.storeKey(keyId, key);
        String retrievedKey = keyManager.retrieveKey(keyId);
        
        assertEquals(key, retrievedKey);
    }

    @Test
    void testDeleteKey() {
        String keyId = "test-key-id";
        String key = keyManager.generateKey();
        
        keyManager.storeKey(keyId, key);
        keyManager.deleteKey(keyId);
        
        assertThrows(IllegalStateException.class, () -> keyManager.retrieveKey(keyId));
    }

    @Test
    void testRotateKey() {
        String keyId = "test-key-id";
        String originalKey = keyManager.generateKey();
        
        keyManager.storeKey(keyId, originalKey);
        String newKeyId = keyManager.rotateKey(keyId);
        String newKey = keyManager.retrieveKey(newKeyId);
        
        assertNotNull(newKey);
        assertNotEquals(originalKey, newKey);
    }

    @Test
    void testStoreKeyWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.storeKey(null, "key"));
    }

    @Test
    void testStoreKeyWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.storeKey("", "key"));
    }

    @Test
    void testStoreKeyWithNullKey() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.storeKey("id", null));
    }

    @Test
    void testStoreKeyWithEmptyKey() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.storeKey("id", ""));
    }

    @Test
    void testRetrieveKeyWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.retrieveKey(null));
    }

    @Test
    void testRetrieveKeyWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.retrieveKey(""));
    }

    @Test
    void testRetrieveNonExistentKey() {
        assertThrows(IllegalStateException.class, () -> keyManager.retrieveKey("non-existent-key"));
    }

    @Test
    void testDeleteKeyWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.deleteKey(null));
    }

    @Test
    void testDeleteKeyWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.deleteKey(""));
    }

    @Test
    void testDeleteNonExistentKey() {
        assertThrows(IllegalStateException.class, () -> keyManager.deleteKey("non-existent-key"));
    }

    @Test
    void testRotateKeyWithNullId() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.rotateKey(null));
    }

    @Test
    void testRotateKeyWithEmptyId() {
        assertThrows(IllegalArgumentException.class, () -> keyManager.rotateKey(""));
    }

    @Test
    void testRotateNonExistentKey() {
        assertThrows(IllegalStateException.class, () -> keyManager.rotateKey("non-existent-key"));
    }
}