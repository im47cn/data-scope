package com.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import com.common.enums.DataSourceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源领域模型
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
     * 密码（未加密）
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
    private Set<String> tags;
    
    /**
     * 获取数据库驱动类名
     * @return 数据库驱动类名
     */
    public String getDriverClassName() {
        if (type == null) {
            return null;
        }
        
        switch (type) {
            case MYSQL:
                return "com.mysql.cj.jdbc.Driver";
            case DB2:
                return "com.ibm.db2.jcc.DB2Driver";
            default:
                throw new IllegalArgumentException("不支持的数据源类型: " + type);
        }
    }
    
    /**
     * 获取JDBC URL
     * @return JDBC连接URL
     */
    public String getJdbcUrl() {
        if (type == null || host == null || port == null || databaseName == null) {
            return null;
        }
        
        switch (type) {
            case MYSQL:
                return String.format("jdbc:mysql://%s:%d/%s", host, port, databaseName);
            case DB2:
                return String.format("jdbc:db2://%s:%d/%s", host, port, databaseName);
            default:
                throw new IllegalArgumentException("不支持的数据源类型: " + type);
        }
    }
}