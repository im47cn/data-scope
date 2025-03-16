package com.insightdata.domain.model.query;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏的查询
 * 用户保存的查询，包括查询条件、配置和权限设置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedQuery {
    
    /**
     * 收藏查询ID
     */
    private Long id;
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 查询描述
     */
    private String description;
    
    /**
     * 创建者ID
     */
    private Long creatorId;
    
    /**
     * 创建者名称
     */
    private String creatorName;
    
    /**
     * 所有者ID
     */
    private Long ownerId;
    
    /**
     * 所有者名称
     */
    private String ownerName;
    
    /**
     * 查询界面配置ID
     */
    private Long interfaceConfigId;
    
    /**
     * 查询界面配置名称
     */
    private String interfaceName;
    
    /**
     * 查询界面配置版本
     */
    private String interfaceVersion;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 数据源名称
     */
    private String dataSourceName;
    
    /**
     * 自然语言查询
     */
    private String naturalLanguageQuery;
    
    /**
     * 生成的SQL语句
     */
    private String generatedSql;
    
    /**
     * 查询条件参数
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 查询条件参数
     * 兼容旧版的queryParams
     */
    @Builder.Default
    private Map<String, Object> queryParams = new HashMap<>();
    
    /**
     * SQL语句
     */
    private String sqlStatement;
    
    /**
     * 是否公开
     * 公开的查询可以被其他用户发现和使用
     */
    private boolean isPublic;
    
    /**
     * 共享用户IDs
     * 指定哪些用户可以使用此查询
     */
    @Builder.Default
    private List<Long> sharedUserIds = new ArrayList<>();
    
    /**
     * 共享部门IDs
     * 指定哪些部门可以使用此查询
     */
    @Builder.Default
    private List<Long> sharedDepartmentIds = new ArrayList<>();
    
    /**
     * 权限码
     * 访问此查询需要的权限
     */
    @Builder.Default
    private List<String> permissionCodes = new ArrayList<>();
    
    /**
     * 标签
     */
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    /**
     * 分类
     */
    private String category;
    
    /**
     * 查询状态
     * 例如: ACTIVE, ARCHIVED, DEPRECATED
     */
    private String status;
    
    /**
     * 使用次数
     */
    private Integer usageCount;
    
    /**
     * 最后使用时间
     */
    private LocalDateTime lastUsedAt;
    
    /**
     * 查询结果快照
     * 可以选择性保存最后一次查询结果以提高查看速度
     */
    private String resultSnapshot;
    
    /**
     * 快照创建时间
     */
    private LocalDateTime snapshotCreatedAt;
    
    /**
     * 排序序号
     */
    private Integer sortOrder;
    
    /**
     * 是否固定
     * 固定的查询会显示在列表顶部
     */
    private boolean pinned;
    
    /**
     * 查询URL
     * 可以直接访问的URL
     */
    private String queryUrl;
    
    /**
     * 是否启用通知
     */
    private boolean enableNotification;
    
    /**
     * 通知配置
     */
    @Builder.Default
    private Map<String, Object> notificationConfig = new HashMap<>();
    
    /**
     * 计划任务配置
     * 定时执行此查询的配置
     */
    @Builder.Default
    private Map<String, Object> scheduledJobConfig = new HashMap<>();
    
    /**
     * 自定义结果处理配置
     */
    @Builder.Default
    private Map<String, Object> resultProcessingConfig = new HashMap<>();
    
    /**
     * 自定义视图配置
     */
    @Builder.Default
    private Map<String, Object> customViewConfig = new HashMap<>();
    
    /**
     * 视图类型
     * 例如：table(表格), chart(图表), card(卡片)等
     */
    private String viewType;
    
    /**
     * 版本号
     */
    private String version;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 备注
     */
    private String remarks;
    
    /**
     * 扩展属性
     */
    @Builder.Default
    private Map<String, Object> extensionProperties = new HashMap<>();
    
    /**
     * 为兼容旧版代码，设置自然语言查询
     */
    public void setNaturalLanguageQuery(String query) {
        this.naturalLanguageQuery = query;
    }
    
    /**
     * 为兼容旧版代码，获取生成的SQL
     */
    public String getGeneratedSql() {
        return this.generatedSql;
    }
    
    /**
     * 为兼容旧版代码，设置生成的SQL
     */
    public void setGeneratedSql(String sql) {
        this.generatedSql = sql;
    }
    
    /**
     * 为兼容旧版代码，设置查询参数
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
    
    /**
     * 为兼容旧版代码，设置是否公开
     */
    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
