package com.insightdata.facade.metadata;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.insightdata.facade.metadata.enums.DataSourceType;
import lombok.Data;

import java.util.Map;

/**
 * 数据源更新请求DTO
 */
@Data
public class DataSourceUpdateRequest {
    
    /**
     * 数据源名称
     */
    @NotBlank(message = "数据源名称不能为空")
    @Size(max = 100, message = "数据源名称长度不能超过100个字符")
    private String name;
    
    /**
     * 数据源类型
     */
    @NotNull(message = "数据源类型不能为空")
    private DataSourceType type;
    
    /**
     * 主机地址
     */
    @NotBlank(message = "主机地址不能为空")
    @Size(max = 255, message = "主机地址长度不能超过255个字符")
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
    @Size(max = 100, message = "数据库名称长度不能超过100个字符")
    private String databaseName;
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 100, message = "用户名长度不能超过100个字符")
    private String username;
    
    /**
     * 密码（可选，如果不提供则保留原密码）
     */
    private String password;
    
    /**
     * 连接属性
     */
    private Map<String, String> connectionProperties;
    
    /**
     * 是否激活
     */
    private boolean active = true;
    
    /**
     * 描述
     */
    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;
}