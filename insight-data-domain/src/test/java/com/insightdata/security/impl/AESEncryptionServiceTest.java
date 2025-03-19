package com.insightdata.security.impl;

import com.insightdata.security.EncryptionException;
import com.insightdata.security.KeyInfo;
import com.insightdata.security.KeyManagementService;
import com.insightdata.security.KeyStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AESEncryptionServiceTest {

    private static final String TEST_PURPOSE = "test-purpose";
    private static final String PLAIN_TEXT = "Hello, World!";

    @Mock
    private KeyManagementService keyManagementService;

    private AESEncryptionService encryptionService;
    private KeyInfo testKey;

    @BeforeEach
    void setUp() {
        encryptionService = new AESEncryptionService(keyManagementService);
        testKey = KeyInfo.builder()
                .id("test-key-id")
                .version(1)
                .keyContent("VGhpcyBpcyBhIHRlc3Qga2V5IGZvciBlbmNyeXB0aW9uIHRlc3Rpbmc=") // 32 bytes base64 encoded
                .purpose(TEST_PURPOSE)
                .status(KeyStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMonths(3))
                .build();
    }

    @Test
    void encryptAndDecrypt_Success() throws EncryptionException {
        when(keyManagementService.retrieveCurrentKey(TEST_PURPOSE)).thenReturn(Optional.of(testKey));
        when(keyManagementService.retrieveKeyById(String.valueOf(testKey.getVersion()))).thenReturn(Optional.of(testKey));

        String encrypted = encryptionService.encrypt(PLAIN_TEXT, TEST_PURPOSE);
        assertNotNull(encrypted);
        assertNotEquals(PLAIN_TEXT, encrypted);

        String decrypted = encryptionService.decrypt(encrypted, TEST_PURPOSE);
        assertEquals(PLAIN_TEXT, decrypted);

        verify(keyManagementService).retrieveCurrentKey(TEST_PURPOSE);
        verify(keyManagementService).retrieveKeyById(String.valueOf(testKey.getVersion()));
    }

    @Test
    void encrypt_NoValidKey_ThrowsException() {
        when(keyManagementService.retrieveCurrentKey(TEST_PURPOSE)).thenReturn(Optional.empty());

        assertThrows(EncryptionException.class, () ->
                encryptionService.encrypt(PLAIN_TEXT, TEST_PURPOSE));

        verify(keyManagementService).retrieveCurrentKey(TEST_PURPOSE);
    }

    @Test
    void decrypt_NoValidKey_ThrowsException() {
        when(keyManagementService.retrieveCurrentKey(TEST_PURPOSE)).thenReturn(Optional.of(testKey));
        String encrypted = encryptionService.encrypt(PLAIN_TEXT, TEST_PURPOSE);

        when(keyManagementService.retrieveKeyById(anyString())).thenReturn(Optional.empty());

        assertThrows(EncryptionException.class, () ->
                encryptionService.decrypt(encrypted, TEST_PURPOSE));

        verify(keyManagementService).retrieveKeyById(anyString());
    }

    @Test
    void validate_ValidKey_ReturnsTrue() {
        when(keyManagementService.retrieveCurrentKey(TEST_PURPOSE)).thenReturn(Optional.of(testKey));
        String encrypted = encryptionService.encrypt(PLAIN_TEXT, TEST_PURPOSE);

        when(keyManagementService.retrieveKeyById(String.valueOf(testKey.getVersion()))).thenReturn(Optional.of(testKey));

        assertTrue(encryptionService.validate(encrypted));
    }

    @Test
    void validate_InvalidKey_ReturnsFalse() {
        when(keyManagementService.retrieveCurrentKey(TEST_PURPOSE)).thenReturn(Optional.of(testKey));
        String encrypted = encryptionService.encrypt(PLAIN_TEXT, TEST_PURPOSE);

        when(keyManagementService.retrieveKeyById(anyString())).thenReturn(Optional.empty());

        assertFalse(encryptionService.validate(encrypted));
    }

    @Test
    void reencrypt_Success() throws EncryptionException {
        when(keyManagementService.retrieveCurrentKey(TEST_PURPOSE)).thenReturn(Optional.of(testKey));
        when(keyManagementService.retrieveKeyById(String.valueOf(testKey.getVersion()))).thenReturn(Optional.of(testKey));

        String encrypted = encryptionService.encrypt(PLAIN_TEXT, TEST_PURPOSE);
        String reencrypted = encryptionService.reencrypt(encrypted, TEST_PURPOSE);

        assertNotNull(reencrypted);
        assertNotEquals(encrypted, reencrypted);

        String decrypted = encryptionService.decrypt(reencrypted, TEST_PURPOSE);
        assertEquals(PLAIN_TEXT, decrypted);
    }
}