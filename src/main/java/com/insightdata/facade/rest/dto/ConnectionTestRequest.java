package com.insightdata.facade.rest.dto;

import java.util.Map;

import com.insightdata.domain.model.DataSource.DataSourceType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 连接测试请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionTestRequest {
    
    /**
     * 数据源类型
     */
    @NotNull(message = "数据源类型不能为空")
    private DataSourceType type;
    
    /**
     * 主机地址
     */
    @NotBlank(message = "主机地址不能为空")
    private String host;
    
    /**
     * 端口号
     */
    @NotNull(message = "端口号不能为空")
    private Integer port;
    
    /**
     * 数据库名称
     */
    @NotBlank(message = "数据库名称不能为空")
    private String databaseName;
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;
    
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;
    
    /**
     * 连接属性
     */
    private Map<String, String> connectionProperties;
    
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
