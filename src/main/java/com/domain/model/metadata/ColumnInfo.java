package com.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 列信息模型
 * 用于表示数据库表中列的结构信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {
    
    /**
     * 列名
     */
    private String name;
    
    /**
     * 列描述
     */
    private String description;
    
    /**
     * 数据类型
     */
    private String dataType;

    private String columnType;

    /**
     * 字段长度
     */
    private Integer length;
    
    /**
     * 精度（用于数值类型）
     */
    private Integer precision;
    
    /**
     * 小数位数（用于数值类型）
     */
    private Integer scale;
    
    /**
     * 是否允许为空
     */
    private Boolean nullable;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 是否是主键
     */
    private Boolean primaryKey;
    
    /**
     * 是否是外键
     */
    private Boolean foreignKey;
    
    /**
     * 是否是唯一键
     */
    private Boolean unique;
    
    /**
     * 是否是自增列
     */
    private Boolean autoIncrement;
    
    /**
     * 序号位置
     */
    private Integer ordinalPosition;
    
    /**
     * 所属表名
     */
    private String tableName;
    
    /**
     * 所属模式（Schema）名称
     */
    private String schemaName;
    
    /**
     * 所属数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 列统计信息
     */
    private ColumnStatistics statistics;
    
    /**
     * 判断是否是主键
     * 
     * @return 如果是主键返回true，否则返回false
     */
    public boolean isPrimaryKey() {
        return primaryKey != null && primaryKey;
    }
    
    /**
     * 判断是否是外键
     * 
     * @return 如果是外键返回true，否则返回false
     */
    public boolean isForeignKey() {
        return foreignKey != null && foreignKey;
    }
    
    /**
     * 判断是否允许为空
     * 
     * @return 如果允许为空返回true，否则返回false
     */
    public boolean isNullable() {
        return nullable != null && nullable;
    }
    
    /**
     * 判断是否是唯一键
     * 
     * @return 如果是唯一键返回true，否则返回false
     */
    public boolean isUnique() {
        return unique != null && unique;
    }
    
    /**
     * 判断是否是自增列
     * 
     * @return 如果是自增列返回true，否则返回false
     */
    public boolean isAutoIncrement() {
        return autoIncrement != null && autoIncrement;
    }
    
    /**
     * 获取完整的列标识
     * 
     * @return 格式为"schema.table.column"的完整标识
     */
    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (schemaName != null && !schemaName.isEmpty()) {
            sb.append(schemaName).append(".");
        }
        if (tableName != null && !tableName.isEmpty()) {
            sb.append(tableName).append(".");
        }
        if (name != null) {
            sb.append(name);
        }
        return sb.toString();
    }
    
    /**
     * 获取列的完整类型描述
     * 
     * @return 完整的类型描述，包括长度、精度和小数位数
     */
    public String getFullDataType() {
        StringBuilder sb = new StringBuilder(dataType == null ? "" : dataType);
        
        if ("CHAR".equalsIgnoreCase(dataType) || 
            "VARCHAR".equalsIgnoreCase(dataType) || 
            "NVARCHAR".equalsIgnoreCase(dataType) ||
            "BINARY".equalsIgnoreCase(dataType) ||
            "VARBINARY".equalsIgnoreCase(dataType)) {
            if (length != null) {
                sb.append("(").append(length).append(")");
            }
        } else if ("DECIMAL".equalsIgnoreCase(dataType) || 
                   "NUMERIC".equalsIgnoreCase(dataType)) {
            if (precision != null) {
                sb.append("(").append(precision);
                if (scale != null) {
                    sb.append(",").append(scale);
                }
                sb.append(")");
            }
        }
        
        return sb.toString();
    }
    
    /**
     * 列统计信息
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColumnStatistics {
        /**
         * 唯一值数量
         */
        private Long distinctCount;
        
        /**
         * 空值数量
         */
        private Long nullCount;
        
        /**
         * 最大值
         */
        private String maxValue;
        
        /**
         * 最小值
         */
        private String minValue;
        
        /**
         * 平均值
         */
        private Double avgValue;
        
        /**
         * 中位数
         */
        private Double medianValue;
        
        /**
         * 频率最高的值
         */
        private String mostFrequentValue;
        
        /**
         * 频率最高的值出现次数
         */
        private Long mostFrequentValueCount;
    }
}