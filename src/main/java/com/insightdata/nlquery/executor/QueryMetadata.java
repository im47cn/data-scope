package com.insightdata.nlquery.executor;

import java.util.List;

import lombok.Data;

/**
 * 查询元数据信息
 * 包含查询结果的元数据信息，如列名、列类型、是否可为空等
 */
@Data
public class QueryMetadata {

    /**
     * 列名称列表
     */
    private List<ColumnMetadata> columns;

    /**
     * 总记录数
     * 在部分数据库中，可能无法获取到准确的总记录数
     */
    private Long totalCount;

    /**
     * 是否还有更多数据
     */
    private Boolean hasMore;

    /**
     * 列元数据信息
     */
    @Data
    public static class ColumnMetadata {
        /**
         * 列名
         */
        private String name;
        
        /**
         * 列标签，用于展示
         */
        private String label;
        
        /**
         * 数据类型
         */
        private String dataType;
        
        /**
         * 是否可为空
         */
        private Boolean nullable;
        
        /**
         * 列长度
         */
        private Integer length;
        
        /**
         * 精度（对于数值类型）
         */
        private Integer precision;
        
        /**
         * 小数位数（对于数值类型）
         */
        private Integer scale;
        
        /**
         * 是否为主键
         */
        private Boolean isPrimaryKey;
        
        /**
         * 列的显示宽度建议值
         */
        private Integer displayWidth;
        
        /**
         * 列的对齐方式
         * LEFT, RIGHT, CENTER
         */
        private String alignment;
        
        /**
         * 是否为敏感数据
         */
        private Boolean isSensitive;
    }
}