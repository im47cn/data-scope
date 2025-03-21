package com.insightdata.domain.querybuilder.engine;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 执行统计信息测试类
 */
public class ExecutionStatsTest {

    @Test
    public void testCalculateDuration() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusSeconds(5);

        ExecutionStats stats = ExecutionStats.builder()
                .startTime(start)
                .endTime(end)
                .build();

        assertEquals(5000, stats.calculateDuration());
    }

    @Test
    public void testCalculateDurationWithNullTimes() {
        ExecutionStats stats = ExecutionStats.builder()
                .duration(1000)
                .build();

        assertEquals(1000, stats.calculateDuration());
    }

    @Test
    public void testGetTotalWaitTime() {
        ExecutionStats stats = ExecutionStats.builder()
                .cpuTime(1000)
                .ioWaitTime(500)
                .build();

        assertEquals(1500, stats.getTotalWaitTime());
    }

    @Test
    public void testNeedsOptimizationTrue() {
        ExecutionStats stats = ExecutionStats.builder()
                .duration(2000)
                .rowsScanned(20000)
                .memoryUsed(200 * 1024 * 1024)
                .cacheHitRate(0.3)
                .indexUsed(false)
                .build();

        assertTrue(stats.needsOptimization());
    }

    @Test
    public void testNeedsOptimizationFalse() {
        ExecutionStats stats = ExecutionStats.builder()
                .duration(500)
                .rowsScanned(5000)
                .memoryUsed(50 * 1024 * 1024)
                .cacheHitRate(0.8)
                .indexUsed(true)
                .build();

        assertFalse(stats.needsOptimization());
    }

    @Test
    public void testGetPerformanceScorePerfect() {
        ExecutionStats stats = ExecutionStats.builder()
                .duration(100)
                .rowsScanned(1000)
                .cacheHitRate(0.9)
                .indexUsed(true)
                .memoryUsed(10 * 1024 * 1024)
                .build();

        assertEquals(100, stats.getPerformanceScore());
    }

    @Test
    public void testGetPerformanceScorePoor() {
        ExecutionStats stats = ExecutionStats.builder()
                .duration(6000)
                .rowsScanned(200000)
                .cacheHitRate(0.2)
                .indexUsed(false)
                .memoryUsed(600 * 1024 * 1024)
                .build();

        assertEquals(0, stats.getPerformanceScore());
    }

    @Test
    public void testGetSummary() {
        ExecutionStats stats = ExecutionStats.builder()
                .duration(1000)
                .rowsAffected(100)
                .rowsScanned(1000)
                .warnings(new String[]{"警告1", "警告2"})
                .build();

        String summary = stats.getSummary();
        assertTrue(summary.contains("执行时间: 1000ms"));
        assertTrue(summary.contains("影响行数: 100"));
        assertTrue(summary.contains("扫描行数: 1000"));
        assertTrue(summary.contains("警告: 警告1, 警告2"));
    }

    @Test
    public void testGetSummaryNoWarnings() {
        ExecutionStats stats = ExecutionStats.builder()
                .duration(1000)
                .rowsAffected(100)
                .rowsScanned(1000)
                .build();

        String summary = stats.getSummary();
        assertFalse(summary.contains("警告:"));
    }
}