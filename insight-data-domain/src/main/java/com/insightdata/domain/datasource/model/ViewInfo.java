package com.insightdata.domain.datasource.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 视图信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ViewInfo {
    /**
     * 视图名称
     */
    private String name;

    /**
     * 所属模式
     */
    private String schema;

    /**
     * 视图定义
     */
    private String definition;

    /**
     * 检查选项(NONE/LOCAL/CASCADED)
     */
    private String checkOption;

    /**
     * 是否可更新
     */
    private boolean updatable;

    /**
     * 是否可插入
     */
    private boolean insertable;

    /**
     * 是否可删除
     */
    private boolean deletable;

    /**
     * 是否合并更新
     */
    private boolean mergeUpdatable;

    /**
     * 是否强制使用索引
     */
    private boolean forceIndex;

    /**
     * 视图算法(UNDEFINED/MERGE/TEMPTABLE)
     */
    private String algorithm;

    /**
     * 安全性(DEFINER/INVOKER)
     */
    private String security;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 视图注释
     */
    private String comment;

    /**
     * 依赖的表
     */
    private String[] dependentTables;

    /**
     * 视图列信息
     */
    private ViewColumn[] columns;

    /**
     * 视图列信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ViewColumn {
        /**
         * 列名
         */
        private String name;

        /**
         * 列类型
         */
        private String type;

        /**
         * 是否可为空
         */
        private boolean nullable;

        /**
         * 列注释
         */
        private String comment;

        /**
         * 是否可更新
         */
        private boolean updatable;

        /**
         * 是否为表达式
         */
        private boolean expression;

        /**
         * 源表名
         */
        private String sourceTable;

        /**
         * 源列名
         */
        private String sourceColumn;
    }
}