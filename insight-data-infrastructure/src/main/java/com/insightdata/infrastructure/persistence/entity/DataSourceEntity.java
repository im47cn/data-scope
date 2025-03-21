package com.insightdata.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.Map;

import com.insightdata.domain.datasource.enums.DataSourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源实体类
 * 使用MyBatis映射
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceEntity {
    
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
     * 加密后的密码
     */
    private String encryptedPassword;
    
    /**
     * 加密盐值
     */
    private String encryptionSalt;
    
    /**
     * 连接属性
     */
    private Map<String, String> connectionProperties;
    
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;
    
    /**
     * 是否启用
     */
    private Boolean active;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}