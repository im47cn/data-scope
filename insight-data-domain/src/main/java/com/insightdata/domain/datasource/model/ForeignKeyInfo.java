package com.insightdata.domain.datasource.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 外键信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKeyInfo {
    /**
     * 外键名称
     */
    private String name;

    /**
     * 所属模式
     */
    private String schema;

    /**
     * 源表名
     */
    private String sourceTable;

    /**
     * 源列名列表
     */
    private String[] sourceColumns;

    /**
     * 目标表名
     */
    private String targetTable;

    /**
     * 目标列名列表
     */
    private String[] targetColumns;

    /**
     * 更新规则(NO ACTION/CASCADE/SET NULL/SET DEFAULT)
     */
    private String updateRule;

    /**
     * 删除规则(NO ACTION/CASCADE/SET NULL/SET DEFAULT)
     */
    private String deleteRule;

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 是否延迟检查
     */
    private boolean deferrable;

    /**
     * 是否初始延迟
     */
    private boolean initiallyDeferred;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 外键注释
     */
    private String comment;

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