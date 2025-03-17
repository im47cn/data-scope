package com.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 模式信息实体类
 * 表示数据库的模式/架构
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SchemaInfo {

    /**
     * 模式ID
     */
    private String id;

    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 模式名称
     */
    private String name;
    
    /**
     * 模式的目录
     */
    private String catalog;
    
    /**
     * 模式的拥有者
     */
    private String owner;
    
    /**
     * 模式的默认字符集
     */
    private String defaultCharacterSetName;
    
    /**
     * 模式的默认排序规则
     */
    private String defaultCollationName;
    
    /**
     * 是否为默认模式
     */
    private boolean defaultSchema;
    
    /**
     * 模式的备注/描述
     */
    private String description;
    
    /**
     * 此模式下的表列表
     */
    @Builder.Default
    private List<TableInfo> tables = new ArrayList<>();
    
    /**
     * 此模式下的视图列表
     */
    @Builder.Default
    private List<ViewInfo> views = new ArrayList<>();
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 添加表
     */
    public void addTable(TableInfo table) {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        tables.add(table);
    }
    
    /**
     * 添加视图
     */
    public void addView(ViewInfo view) {
        if (views == null) {
            views = new ArrayList<>();
        }
        views.add(view);
    }
    
    /**
     * 根据名称查找表
     */
    public Optional<TableInfo> findTableByName(String tableName) {
        if (tables == null) {
            return Optional.empty();
        }
        return tables.stream()
                .filter(table -> table.getName().equalsIgnoreCase(tableName))
                .findFirst();
    }
    
    /**
     * 根据名称查找视图
     */
    public Optional<ViewInfo> findViewByName(String viewName) {
        if (views == null) {
            return Optional.empty();
        }
        return views.stream()
                .filter(view -> view.getName().equalsIgnoreCase(viewName))
                .findFirst();
    }
    
    /**
     * 获取所有表名
     */
    public List<String> getTableNames() {
        if (tables == null) {
            return new ArrayList<>();
        }
        return tables.stream()
                .map(TableInfo::getName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有视图名
     */
    public List<String> getViewNames() {
        if (views == null) {
            return new ArrayList<>();
        }
        return views.stream()
                .map(ViewInfo::getName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有对象（表和视图）名称
     */
    public List<String> getAllObjectNames() {
        List<String> names = new ArrayList<>();
        
        if (tables != null) {
            names.addAll(getTableNames());
        }
        
        if (views != null) {
            names.addAll(getViewNames());
        }
        
        return names;
    }
    
    /**
     * 获取表或视图的完全限定名
     * 例如：schema_name.table_name
     */
    public String getQualifiedName(String objectName) {
        return name + "." + objectName;
    }
    
    /**
     * 检查模式是否包含指定的表
     */
    public boolean containsTable(String tableName) {
        return findTableByName(tableName).isPresent();
    }
    
    /**
     * 检查模式是否包含指定的视图
     */
    public boolean containsView(String viewName) {
        return findViewByName(viewName).isPresent();
    }
    
    /**
     * 获取模式中表和视图的总数
     */
    public int getTotalObjectCount() {
        int count = 0;
        
        if (tables != null) {
            count += tables.size();
        }
        
        if (views != null) {
            count += views.size();
        }
        
        return count;
    }
}