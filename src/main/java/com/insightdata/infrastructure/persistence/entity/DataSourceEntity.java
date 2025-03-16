package com.insightdata.infrastructure.persistence.entity;

import com.insightdata.common.enums.DataSourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 数据源实体类
 * 使用MyBatis映射
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceEntity {
    
    private Long id;
    
    private String name;
    
    private DataSourceType type;
    
    private String host;
    
    private Integer port;
    
    private String databaseName;
    
    private String username;
    
    private String encryptedPassword;
    
    private String encryptionSalt;
    
    private Map<String, String> connectionProperties;
    
    private LocalDateTime lastSyncTime;
    
    private Boolean active;
    
    private String description;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}