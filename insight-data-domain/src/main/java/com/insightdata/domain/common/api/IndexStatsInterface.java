package com.insightdata.domain.common.api;

/**
 * 索引统计信息接口
 * 定义索引统计信息的基本操作
 */
public interface IndexStatsInterface {
    /**
     * 获取索引行数
     */
    Long getRowCount();
    
    /**
     * 获取索引页数
     */
    Long getPageCount();
    
    /**
     * 获取索引分布统计
     */
    Double getDistinctRatio();
    
    /**
     * 获取索引碎片率
     */
    Double getFragmentationRatio();
    
    /**
     * 获取最后更新时间
     */
    String getLastUpdateTime();
}