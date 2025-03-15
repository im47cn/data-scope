package com.insightdata.infrastructure.persistence.entity;

import com.insightdata.common.enums.DataSourceType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 数据源实体类
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data_source")
public class DataSourceEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DataSourceType type;
    
    @Column(name = "host", nullable = false)
    private String host;
    
    @Column(name = "port", nullable = false)
    private Integer port;
    
    @Column(name = "database_name", nullable = false)
    private String databaseName;
    
    @Column(name = "username", nullable = false)
    private String username;
    
    @Column(name = "encrypted_password", nullable = false)
    private String encryptedPassword;
    
    @Column(name = "encryption_salt", nullable = false)
    private String encryptionSalt;
    
    @Column(name = "connection_properties", columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> connectionProperties;
    
    @Column(name = "last_sync_time")
    private LocalDateTime lastSyncTime;
    
    @Column(name = "active", nullable = false)
    private Boolean active;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}