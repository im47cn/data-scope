package com.insightdata.nlquery.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排序要求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortRequirement {
    
    /**
     * 排序方向
     */
    public enum SortDirection {
        /**
         * 升序
         */
        ASC("升序"),
        
        /**
         * 降序
         */
        DESC("降序");
        
        private final String displayName;
        
        SortDirection(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * 排序字段
     */
    private String field;
    
    /**
     * 排序方向
     */
    private SortDirection direction;
    
    /**
     * 排序优先级(数字越小优先级越高)
     */
    private int priority;
    
    /**
     * 是否忽略大小写
     */
    private boolean ignoreCase;
    
    /**
     * 是否允许null值
     */
    private boolean nullsFirst;
    
    /**
     * 创建一个升序排序
     */
    public static SortRequirement asc(String field) {
        return SortRequirement.builder()
                .field(field)
                .direction(SortDirection.ASC)
                .priority(0)
                .build();
    }
    
    /**
     * 创建一个降序排序
     */
    public static SortRequirement desc(String field) {
        return SortRequirement.builder()
                .field(field)
                .direction(SortDirection.DESC)
                .priority(0)
                .build();
    }
    
    /**
     * 创建一个带优先级的升序排序
     */
    public static SortRequirement asc(String field, int priority) {
        return SortRequirement.builder()
                .field(field)
                .direction(SortDirection.ASC)
                .priority(priority)
                .build();
    }
    
    /**
     * 创建一个带优先级的降序排序
     */
    public static SortRequirement desc(String field, int priority) {
        return SortRequirement.builder()
                .field(field)
                .direction(SortDirection.DESC)
                .priority(priority)
                .build();
    }
    
    /**
     * 获取排序表达式
     */
    public String toSortExpression() {
        StringBuilder sb = new StringBuilder();
        
        // 添加字段名
        sb.append(field);
        
        // 添加排序方向
        sb.append(" ").append(direction.name());
        
        // 添加null值处理
        if (nullsFirst) {
            sb.append(" NULLS FIRST");
        } else {
            sb.append(" NULLS LAST");
        }
        
        return sb.toString();
    }
}
