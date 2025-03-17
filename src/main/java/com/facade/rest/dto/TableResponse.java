package com.facade.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据表响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableResponse {
    
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
    private String schemaName;
    
    /**
     * 表名
     */
    private String name;
    
    /**
     * 表类型
     */
    private String type;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 预估行数
     */
    private Long estimatedRowCount;
    
    /**
     * 数据大小(字节)
     */
    private Long dataSize;
    
    /**
     * 索引大小(字节)
     */
    private Long indexSize;
    
    /**
     * 列列表
     */
    @Builder.Default
    private List<ColumnResponse> columns = new ArrayList<>();
    
    /**
     * 索引列表
     */
    @Builder.Default
    private List<IndexResponse> indexes = new ArrayList<>();
    
    /**
     * 外键列表
     */
    @Builder.Default
    private List<ForeignKeyResponse> foreignKeys = new ArrayList<>();
    
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
     * 是否系统表
     */
    private boolean systemTable;
    
    /**
     * 是否临时表
     */
    private boolean temporaryTable;
    
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
     * 添加列
     */
    public TableResponse addColumn(ColumnResponse column) {
        columns.add(column);
        return this;
    }
    
    /**
     * 添加索引
     */
    public TableResponse addIndex(IndexResponse index) {
        indexes.add(index);
        return this;
    }
    
    /**
     * 添加外键
     */
    public TableResponse addForeignKey(ForeignKeyResponse foreignKey) {
        foreignKeys.add(foreignKey);
        return this;
    }
    
    /**
     * 获取列名列表
     */
    public List<String> getColumnNames() {
        List<String> names = new ArrayList<>();
        for (ColumnResponse column : columns) {
            names.add(column.getName());
        }
        return names;
    }
    
    /**
     * 获取指定列
     */
    public ColumnResponse getColumn(String columnName) {
        for (ColumnResponse column : columns) {
            if (column.getName().equalsIgnoreCase(columnName)) {
                return column;
            }
        }
        return null;
    }
    
    /**
     * 获取主键列表
     */
    public List<ColumnResponse> getPrimaryKeyColumns() {
        List<ColumnResponse> pkColumns = new ArrayList<>();
        for (ColumnResponse column : columns) {
            if (column.isPrimaryKey()) {
                pkColumns.add(column);
            }
        }
        return pkColumns;
    }
}
