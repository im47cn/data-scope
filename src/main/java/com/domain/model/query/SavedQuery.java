package com.domain.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 保存的查询实体类
 * 用户保存的查询，可以方便地重复执行
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class SavedQuery {

    /**
     * 查询ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 查询描述
     */
    private String description;
    
    /**
     * 原始查询（可能是自然语言或SQL）
     */
    private String query;
    
    /**
     * 查询类型
     * SQL: 直接SQL查询
     * NL: 自然语言查询
     */
    private String queryType;
    
    /**
     * 执行的SQL
     */
    private String sql;
    
    /**
     * 查询参数
     */
    private String parameters;
    
    /**
     * 是否已共享
     */
    private boolean shared;
    
    /**
     * 是否已收藏
     */
    private boolean favorite;
    
    /**
     * 收藏排序
     */
    private Integer favoriteOrder;
    
    /**
     * 查询标签
     */
    private List<String> tags;
    
    /**
     * 查询结果字段
     */
    private List<String> resultColumns;
    
    /**
     * 执行次数
     */
    private Integer executionCount;
    
    /**
     * 最后执行时间
     */
    private LocalDateTime lastExecutedAt;
    
    /**
     * 平均执行时间（毫秒）
     */
    private Long averageExecutionTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 检查查询是否为自然语言查询
     */
    public boolean isNaturalLanguageQuery() {
        return "NL".equals(queryType);
    }
    
    /**
     * 检查查询是否为直接SQL查询
     */
    public boolean isSqlQuery() {
        return "SQL".equals(queryType);
    }
    
    /**
     * 更新平均执行时间
     */
    public void updateAverageExecutionTime(long newExecutionTime) {
        if (executionCount == null || executionCount == 0) {
            averageExecutionTime = newExecutionTime;
            executionCount = 1;
        } else {
            long totalTime = averageExecutionTime * executionCount;
            executionCount++;
            averageExecutionTime = (totalTime + newExecutionTime) / executionCount;
        }
    }
    
    /**
     * 获取查询的简要摘要
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(name);
        
        if (description != null && !description.isEmpty()) {
            sb.append(" - ").append(truncateIfNeeded(description, 30));
        }
        
        sb.append(" [");
        if (isNaturalLanguageQuery()) {
            sb.append("自然语言]");
        } else {
            sb.append("SQL]");
        }
        
        return sb.toString();
    }
    
    /**
     * 截断过长的字符串
     */
    private String truncateIfNeeded(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        
        if (str.length() <= maxLength) {
            return str;
        }
        
        return str.substring(0, maxLength - 3) + "...";
    }
}
