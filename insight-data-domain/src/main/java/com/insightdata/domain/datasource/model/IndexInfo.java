package com.insightdata.domain.datasource.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 索引信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndexInfo {
    /**
     * 索引名称
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
     * 索引类型(BTREE/HASH等)
     */
    private String type;

    /**
     * 索引列名列表
     */
    private String[] columnNames;

    /**
     * 是否唯一索引
     */
    private boolean unique;

    /**
     * 是否主键索引
     */
    private boolean primaryKey;

    /**
     * 是否聚集索引
     */
    private boolean clustered;

    /**
     * 过滤条件
     */
    private String filterCondition;

    /**
     * 索引大小(字节)
     */
    private Long indexSize;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

    /**
     * 索引注释
     */
    private String comment;

    /**
     * 是否启用
     */
    private boolean enabled;

    /**
     * 索引统计信息
     */
    private IndexStats stats;

    /**
     * 索引统计信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndexStats {
        /**
         * 索引行数
         */
        private Long rowCount;

        /**
         * 索引页数
         */
        private Long pageCount;

        /**
         * 索引分布统计
         */
        private Double distinctRatio;

        /**
         * 索引碎片率
         */
        private Double fragmentationRatio;

        /**
         * 最后更新时间
         */
        private String lastUpdateTime;
    }
}