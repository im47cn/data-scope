package com.insightdata.domain.querybuilder.engine;

/**
 * 查询执行状态枚举
 */
public enum ExecutionStatus {

    /**
     * 等待执行
     */
    PENDING,

    /**
     * 正在执行
     */
    RUNNING,

    /**
     * 执行成功
     */
    SUCCESS,

    /**
     * 执行失败
     */
    FAILED,

    /**
     * 已取消
     */
    CANCELLED,

    /**
     * 部分成功
     */
    PARTIAL_SUCCESS
}