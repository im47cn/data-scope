package com.insightdata.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AESEncryptorTest {

    @Mock
    private KeyManager keyManager;

    private AESEncryptor encryptor;
    private static final String TEST_KEY_ID = "test-key-id";
    private static final String TEST_KEY = "VGhpc0lzQVRlc3RLZXlGb3JBRVNFbmNyeXB0aW9u";
    private static final String TEST_DATA = "Hello, World!";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encryptor = new AESEncryptor(keyManager);
    }

    @Test
    void testEncryptAndDecrypt() {
        when(keyManager.retrieveKey(TEST_KEY_ID)).thenReturn(TEST_KEY);
        
        String encrypted = encryptor.encrypt(TEST_DATA, TEST_KEY_ID);
        assertNotNull(encrypted);
        assertNotEquals(TEST_DATA, encrypted);
        
        String decrypted = encryptor.decrypt(encrypted, TEST_KEY_ID);
        assertEquals(TEST_DATA, decrypted);
        
        verify(keyManager, times(2)).retrieveKey(TEST_KEY_ID);
    }

    @Test
    void testCreateNewKey() {
        String newKey = "NewGeneratedKey";
        when(keyManager.generateKey()).thenReturn(newKey);
        
        String keyId = encryptor.createNewKey();
        assertNotNull(keyId);
        
        verify(keyManager).generateKey();
        verify(keyManager).storeKey(keyId, newKey);
    }

    @Test
    void testRotateKey() {
        String oldKeyId = "old-key-id";
        String newKeyId = "new-key-id";
        when(keyManager.rotateKey(oldKeyId)).thenReturn(newKeyId);
        
        String resultKeyId = encryptor.rotateKey(oldKeyId);
        assertEquals(newKeyId, resultKeyId);
        
        verify(keyManager).rotateKey(oldKeyId);
    }

    @Test
    void testEncryptionFailure() {
        when(keyManager.retrieveKey(TEST_KEY_ID)).thenReturn("InvalidKey");
        
        assertThrows(RuntimeException.class, () -> {
            encryptor.encrypt(TEST_DATA, TEST_KEY_ID);
        });
    }

    @Test
    void testDecryptionFailure() {
        when(keyManager.retrieveKey(TEST_KEY_ID)).thenReturn("InvalidKey");
        
        assertThrows(RuntimeException.class, () -> {
            encryptor.decrypt("InvalidEncryptedData", TEST_KEY_ID);
        });
    }
}