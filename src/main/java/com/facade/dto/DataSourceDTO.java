package com.facade.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.common.enums.DataSourceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceDTO {
    
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
     * 数据库主机地址
     */
    private String host;
    
    /**
     * 数据库端口号
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
     * 密码（未加密，仅在创建/更新时使用）
     */
    private String password;
    
    /**
     * 连接属性
     */
    private Map<String, String> connectionProperties;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 描述信息
     */
    private String description;
    
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;
    
    /**
     * 最后连接时间
     */
    private LocalDateTime lastConnectedAt;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 标签
     */
    private String[] tags;
}