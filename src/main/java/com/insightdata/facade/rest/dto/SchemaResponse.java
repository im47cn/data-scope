package com.insightdata.facade.rest.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库模式响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaResponse {
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 模式名称
     */
    private String name;
    
    /**
     * 描述
     */
    private String description;
    
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
     * 表列表
     */
    @Builder.Default
    private List<TableResponse> tables = new ArrayList<>();
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;
    
    /**
     * 是否系统模式
     */
    private boolean systemSchema;
    
    /**
     * 是否默认模式
     */
    private boolean defaultSchema;
    
    /**
     * 字符集
     */
    private String characterSet;
    
    /**
     * 排序规则
     */
    private String collation;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 添加表
     */
    public SchemaResponse addTable(TableResponse table) {
        tables.add(table);
        tableCount = tables.size();
        return this;
    }
    
    /**
     * 获取表名列表
     */
    public List<String> getTableNames() {
        List<String> names = new ArrayList<>();
        for (TableResponse table : tables) {
            names.add(table.getName());
        }
        return names;
    }
    
    /**
     * 获取指定表
     */
    public TableResponse getTable(String tableName) {
        for (TableResponse table : tables) {
            if (table.getName().equalsIgnoreCase(tableName)) {
                return table;
            }
        }
        return null;
    }
}
