package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库模式信息
 * 用于表示数据库的结构信息，包含表、列、索引等元数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaInfo {

    private Long id;
    
    /**
     * 数据库名称/模式名称
     */
    private String name;
    
    /**
     * 描述信息
     */
    private String description;
    
    /**
     * 所属数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 表信息列表
     */
    private List<TableInfo> tables;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 获取特定表的信息
     * 
     * @param tableName 表名
     * @return 表信息对象，如果不存在则返回null
     */
    public TableInfo getTable(String tableName) {
        if (tables == null || tableName == null) {
            return null;
        }
        
        return tables.stream()
                .filter(table -> tableName.equalsIgnoreCase(table.getName()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 检查指定表是否存在
     * 
     * @param tableName 表名
     * @return 如果表存在返回true，否则返回false
     */
    public boolean hasTable(String tableName) {
        return getTable(tableName) != null;
    }
    
    /**
     * 添加表信息
     * 
     * @param table 表信息对象
     */
    public void addTable(TableInfo table) {
        if (table != null) {
            tables.add(table);
        }
    }

}