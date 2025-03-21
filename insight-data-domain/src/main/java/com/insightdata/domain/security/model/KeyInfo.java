package com.insightdata.domain.security.model;

import java.time.LocalDateTime;
import java.util.Map;

import com.insightdata.domain.security.enums.KeyStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KeyInfo {
    private String id;
    private int version;
    private String purpose;
    private KeyStatus status;
    private String keyContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expiresAt;
    private Map<String, String> metadata;
}