package com.domain.model.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * 保存的查询实体
 */
@Data
@Builder
public class SavedQuery {
    
    /**
     * 主键ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 查询描述
     */
    private String description;
    
    /**
     * SQL语句
     */
    private String sql;
    
    /**
     * 参数定义
     */
    private Map<String, Object> parameterDefinitions;
    
    /**
     * 默认参数值
     */
    private Map<String, Object> defaultParameters;
    
    /**
     * 文件夹路径
     */
    private String folderPath;
    
    /**
     * 是否共享
     */
    private Boolean isShared;
    
    /**
     * 显示顺序
     */
    private Integer displayOrder;
    
    /**
     * 标签
     */
    private List<String> tags;
    
    /**
     * 乐观锁版本号
     */
    private Long nonce;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 最后执行时间
     */
    private LocalDateTime lastExecutedAt;
    
    /**
     * 执行次数
     */
    private Long executionCount;
    
    /**
     * 创建者
     */
    private String createdBy;
    
    /**
     * 更新者
     */
    private String updatedBy;
}
