package com.facade.rest.dto;

import com.common.enums.DataSourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 数据源响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceResponse {
    
    /**
     * ID
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
    private Boolean active;
    
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
    
    /**
     * 表数量
     */
    private int tableCount;
    
    /**
     * 视图数量
     */
    private int viewCount;
    
    /**
     * 存储过程数量
     */
    private int procedureCount;
    
    /**
     * 函数数量
     */
    private int functionCount;
    
    /**
     * 触发器数量
     */
    private int triggerCount;
    
    /**
     * 序列数量
     */
    private int sequenceCount;
    
    /**
     * 总数据大小(字节)
     */
    private Long totalDataSize;
    
    /**
     * 总索引大小(字节)
     */
    private Long totalIndexSize;
    
    /**
     * 总行数
     */
    private Long totalRowCount;
    
    /**
     * 获取完整的JDBC URL
     */
    public String getJdbcUrl() {
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
    public String getDriverClassName() {
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
