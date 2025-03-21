package com.insightdata.domain.datasource.model;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSource {
    /**
     * 数据源ID
     */
    private String id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据源类型
     */
    private DataSourceType type;

    /**
     * 主机地址
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;

    /**
     * 数据库名称
     */
    private String databaseName;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密后的密码
     */
    private String encryptedPassword;

    /**
     * 加密盐值
     */
    private String encryptionSalt;

    /**
     * 密钥ID
     */
    private String keyId;
    private String driverClassName;
    private String jdbcUrl;

    /**
     * 连接属性
     */
    private Map<String, String> connectionProperties;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 描述
     */
    private String description;

    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;
    private Integer maxConnections;
    private Integer minConnections;
    private Integer connectionTimeout;
    private Integer idleTimeout;
    private Integer maxLifetime;
    private Boolean autoCommit;
    private String validationQuery;
    private Integer validationTimeout;
    private String schema;
    private Map<String, Object> extendedProperties;
}