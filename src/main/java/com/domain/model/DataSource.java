package com.domain.model;

import java.time.LocalDateTime;
import java.util.Map;

import com.common.enums.DataSourceType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Boolean enabled;

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
    
    /**
     * 最后连接时间
     */
    private LocalDateTime lastConnectedAt;
    
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncedAt;
    
    /**
     * 标签
     */
    private String[] tags;

    /**
     * 获取JDBC URL
     */
    public String getJdbcUrl() {
        switch (type) {
            case MYSQL:
                return "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
            case DB2:
                return "jdbc:db2://" + host + ":" + port + "/" + databaseName;
            case ORACLE:
                return "jdbc:oracle:thin:@" + host + ":" + port + ":" + databaseName;
            case POSTGRESQL:
                return "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;
            case SQLSERVER:
                return "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + databaseName;
            default:
                throw new IllegalArgumentException("不支持的数据源类型: " + type);
        }
    }

    /**
     * 获取驱动类名
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
                throw new IllegalArgumentException("不支持的数据源类型: " + type);
        }
    }

    /**
     * 判断数据源是否需要进行元数据同步
     *
     * @return 如果需要同步返回true，否则返回false
     */
    public boolean needSync() {
        return lastSyncTime == null ||
                LocalDateTime.now().minusDays(1).isAfter(lastSyncTime);
    }

}