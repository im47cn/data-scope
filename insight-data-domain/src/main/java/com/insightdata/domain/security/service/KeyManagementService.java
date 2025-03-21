package com.insightdata.domain.security.service;

import java.util.List;
import java.util.Optional;

import com.insightdata.domain.security.enums.KeyStatus;
import com.insightdata.domain.security.model.KeyInfo;

/**
 * Service interface for managing encryption keys
 */
public interface KeyManagementService {

    /**
     * Creates a new encryption key for the specified purpose
     *
     * @param purpose The purpose/usage of the key
     * @return The created key information
     */
    KeyInfo createKey(String purpose);

    /**
     * Retrieves a key by its ID
     *
     * @param keyId The unique identifier of the key
     * @return The key information if found
     */
    Optional<KeyInfo> retrieveKeyById(String keyId);

    /**
     * Gets the current active key for a specific purpose
     *
     * @param purpose The purpose/usage of the key
     * @return The current key information if found
     */
    Optional<KeyInfo> getCurrentKey(String purpose);

    /**
     * Lists all keys for a specific purpose
     *
     * @param purpose The purpose/usage of the keys
     * @return List of key information
     */
    List<KeyInfo> listKeys(String purpose);

    /**
     * Rotates the current key for a specific purpose
     *
     * @param purpose The purpose/usage of the key to rotate
     * @return The new key information
     */
    KeyInfo rotateKey(String purpose);

    /**
     * Updates the status of a key
     *
     * @param keyId The unique identifier of the key
     * @param status The new status
     */
    void updateKeyStatus(String keyId, KeyStatus status);

    /**
     * Gets the next version number for a key purpose
     *
     * @param purpose The purpose/usage of the key
     * @return The next version number
     */
    int getNextKeyVersion(String purpose);

    /**
     * Deletes a key by its ID
     *
     * @param keyId The unique identifier of the key
     */
    void deleteKey(String keyId);
}
