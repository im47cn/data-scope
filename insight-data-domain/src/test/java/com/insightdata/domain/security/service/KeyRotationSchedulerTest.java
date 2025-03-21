package com.insightdata.domain.security.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class KeyRotationSchedulerTest {

    @Mock
    private KeyManagementService keyManagementService;

    private KeyRotationScheduler keyRotationScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        keyRotationScheduler = new KeyRotationScheduler(keyManagementService);
    }

    @Test
    void rotateKeys_ShouldRotateCurrentKey() {
        // Given
        String currentKeyId = "test-key-1";
        when(keyManagementService.getCurrentKeyId()).thenReturn(currentKeyId);

        // When
        keyRotationScheduler.rotateKeys();

        // Then
        verify(keyManagementService).getCurrentKeyId();
        verify(keyManagementService).rotateKey(currentKeyId);
    }

    @Test
    void rotateKeys_ShouldHandleException() {
        // Given
        String currentKeyId = "test-key-1";
        when(keyManagementService.getCurrentKeyId()).thenReturn(currentKeyId);
        doThrow(new RuntimeException("Test error"))
            .when(keyManagementService).rotateKey(currentKeyId);

        // When/Then
        try {
            keyRotationScheduler.rotateKeys();
        } catch (SecurityException e) {
            verify(keyManagementService).getCurrentKeyId();
            verify(keyManagementService).rotateKey(currentKeyId);
            return;
        }
        throw new AssertionError("Expected SecurityException was not thrown");
    }
}
