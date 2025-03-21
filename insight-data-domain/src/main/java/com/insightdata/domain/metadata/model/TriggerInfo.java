package com.insightdata.domain.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 触发器信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TriggerInfo {
    /**
     * 触发器名称
     */
    private String name;

    /**
     * 所属模式
     */
    private String schema;

    /**
     * 所属表名
     */
    private String tableName;

    /**
     * 触发事件(INSERT/UPDATE/DELETE)
     */
    private String event;

    /**
     * 触发时机(BEFORE/AFTER/INSTEAD OF)
     */
    private String timing;

    /**
     * 触发器定义
     */
    private String definition;

    /**
     * 触发条件
     */
    private String condition;

    /**
     * 触发顺序
     */
    private Integer ordering;

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 是否为系统触发器
     */
    private boolean system;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 触发器注释
     */
    private String comment;

    /**
     * 触发器类型(ROW/STATEMENT)
     */
    private String orientation;

    /**
     * 触发器作用域(LOCAL/GLOBAL)
     */
    private String scope;

    /**
     * 引用的列
     */
    private String[] referencedColumns;

    /**
     * 触发器状态
     */
    private TriggerStatus status;

    /**
     * 触发器状态信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TriggerStatus {
        /**
         * 上次触发时间
         */
        private String lastFired;

        /**
         * 触发次数
         */
        private Long fireCount;

        /**
         * 平均执行时间(毫秒)
         */
        private Double avgExecutionTime;

        /**
         * 最大执行时间(毫秒)
         */
        private Long maxExecutionTime;

        /**
         * 最小执行时间(毫秒)
         */
        private Long minExecutionTime;

        /**
         * 错误次数
         */
        private Long errorCount;

        /**
         * 最后一次错误信息
         */
        private String lastError;

        /**
         * 最后一次错误时间
         */
        private String lastErrorTime;
    }
}