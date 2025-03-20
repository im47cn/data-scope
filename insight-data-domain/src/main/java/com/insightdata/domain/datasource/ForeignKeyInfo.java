package com.insightdata.domain.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}