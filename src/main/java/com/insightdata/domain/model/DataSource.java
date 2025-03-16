package com.insightdata.domain.model;

import java.time.LocalDateTime;
import java.util.Map;

import com.insightdata.domain.model.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 数据源
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataSource extends BaseEntity {
    
    /**
     * 数据源类型
     */
    public enum DataSourceType {
        /**
         * MySQL数据库
         */
        MYSQL("MySQL"),
        
        /**
         * DB2数据库
         */
        DB2("DB2"),
        
        /**
         * Oracle数据库
         */
        ORACLE("Oracle"),
        
        /**
         * PostgreSQL数据库
         */
        POSTGRESQL("PostgreSQL"),
        
        /**
         * SQL Server数据库
         */
        SQLSERVER("SQL Server");
        
        private final String displayName;
        
        DataSourceType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
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
     * 驱动类名
     */
    private String driverClassName;
    
    /**
     * JDBC URL
     */
    private String jdbcUrl;
    
    /**
     * 连接属性
     */
    private Map<String, String> connectionProperties;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 是否启用
     */
    private boolean active;
    
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;
    
    /**
     * 获取完整的JDBC URL
     */
    public String getFullJdbcUrl() {
        if (jdbcUrl != null && !jdbcUrl.isEmpty()) {
            return jdbcUrl;
        }
        
        switch (type) {
            case MYSQL:
                return String.format("jdbc:mysql://%s:%d/%s", host, port, databaseName);
            case DB2:
                return String.format("jdbc:db2://%s:%d/%s", host, port, databaseName);
            case ORACLE:
                return String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, databaseName);
            case POSTGRESQL:
                return String.format("jdbc:postgresql://%s:%d/%s", host, port, databaseName);
            case SQLSERVER:
                return String.format("jdbc:sqlserver://%s:%d;databaseName=%s", host, port, databaseName);
            default:
                throw new IllegalStateException("Unsupported database type: " + type);
        }
    }
    
    /**
     * 获取默认驱动类名
     */
    public String getDefaultDriverClassName() {
        switch (type) {
            case MYSQL:
                return "com.mysql.cj.jdbc.Driver";
            case DB2:
                return "com.ibm.db2.jcc.DB2Driver";
            case ORACLE:
                return "oracle.jdbc.OracleDriver";
            case POSTGRESQL:
                return "org.postgresql.Driver";
            case SQLSERVER:
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            default:
                throw new IllegalStateException("Unsupported database type: " + type);
        }
    }
}