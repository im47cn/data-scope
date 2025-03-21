package com.insightdata.domain.querybuilder.engine;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 查询执行统计信息
 */
@Data
@Builder
public class ExecutionStats {

    /**
     * 执行开始时间
     */
    private LocalDateTime startTime;

    /**
     * 执行结束时间
     */
    private LocalDateTime endTime;

    /**
     * 执行耗时(毫秒)
     */
    private long duration;

    /**
     * 影响的行数
     */
    private long rowsAffected;

    /**
     * 扫描的行数
     */
    private long rowsScanned;

    /**
     * CPU使用时间(毫秒)
     */
    private long cpuTime;

    /**
     * IO等待时间(毫秒)
     */
    private long ioWaitTime;

    /**
     * 内存使用(字节)
     */
    private long memoryUsed;

    /**
     * 缓存命中率
     */
    private double cacheHitRate;

    /**
     * 是否使用了索引
     */
    private boolean indexUsed;

    /**
     * 使用的索引名称列表
     */
    private String[] indexNames;

    /**
     * 执行计划摘要
     */
    private String executionPlanSummary;

    /**
     * 警告信息
     */
    private String[] warnings;

    /**
     * 建议优化项
     */
    private String[] optimizationSuggestions;

    /**
     * 计算执行时长
     *
     * @return 执行时长(毫秒)
     */
    public long calculateDuration() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).toMillis();
        }
        return duration;
    }

    /**
     * 获取总等待时间
     *
     * @return 总等待时间(毫秒)
     */
    public long getTotalWaitTime() {
        return cpuTime + ioWaitTime;
    }

    /**
     * 判断是否需要优化
     *
     * @return true 如果需要优化，false 否则
     */
    public boolean needsOptimization() {
        return duration > 1000 || // 执行时间超过1秒
                rowsScanned > 10000 || // 扫描行数过多
                memoryUsed > 100 * 1024 * 1024 || // 内存使用超过100MB
                cacheHitRate < 0.5 || // 缓存命中率低于50%
                !indexUsed; // 未使用索引
    }

    /**
     * 获取性能评分
     *
     * @return 0-100的评分
     */
    public int getPerformanceScore() {
        int score = 100;

        // 执行时间评分(占比30%)
        if (duration > 5000) score -= 30;
        else if (duration > 1000) score -= 15;

        // 扫描行数评分(占比20%)
        if (rowsScanned > 100000) score -= 20;
        else if (rowsScanned > 10000) score -= 10;

        // 缓存命中率评分(占比20%)
        if (cacheHitRate < 0.3) score -= 20;
        else if (cacheHitRate < 0.7) score -= 10;

        // 索引使用评分(占比20%)
        if (!indexUsed) score -= 20;

        // 内存使用评分(占比10%)
        if (memoryUsed > 500 * 1024 * 1024) score -= 10;
        else if (memoryUsed > 100 * 1024 * 1024) score -= 5;

        return Math.max(0, score);
    }

    /**
     * 获取执行状态摘要
     *
     * @return 执行状态摘要
     */
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("执行时间: ").append(duration).append("ms, ");
        summary.append("影响行数: ").append(rowsAffected).append(", ");
        summary.append("扫描行数: ").append(rowsScanned).append(", ");
        summary.append("性能评分: ").append(getPerformanceScore());

        if (warnings != null && warnings.length > 0) {
            summary.append("\n警告: ").append(String.join(", ", warnings));
        }

        return summary.toString();
    }
}