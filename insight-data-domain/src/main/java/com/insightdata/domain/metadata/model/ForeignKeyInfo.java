package com.insightdata.domain.metadata.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ForeignKeyInfo {
    // Placeholder: Add fields as needed
    private String name;
    private List<ForeignKeyColumnInfo> columns; // Changed to List<ForeignKeyColumnInfo>
    private String sourceTableName;
    private String targetTableName;

    public List<ForeignKeyColumnInfo> getColumns(){
        return this.columns;
    }

    public String getTargetTableName() {
        return targetTableName;
    }
    
    /**
     * 获取源列名列表
     * @return 源列名列表
     */
    public List<String> getSourceColumnNames() {
        return columns.stream()
                .map(ForeignKeyColumnInfo::getSourceColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取目标列名列表
     * @return 目标列名列表
     */
    public List<String> getTargetColumnNames() {
        return columns.stream()
                .map(ForeignKeyColumnInfo::getTargetColumnName)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取源列名字符串（逗号分隔）
     * @return 源列名字符串
     */
    public String getSourceColumnNamesAsString() {
        return String.join(",", getSourceColumnNames());
    }
    
    /**
     * 获取目标列名字符串（逗号分隔）
     * @return 目标列名字符串
     */
    public String getTargetColumnNamesAsString() {
        return String.join(",", getTargetColumnNames());
    }
}