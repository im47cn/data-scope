package com.domain.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 查询历史实体类
 * 记录用户执行的查询历史
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class QueryHistory {

    /**
     * 查询历史ID
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
     * 执行时间（毫秒）
     */
    private Long duration;
    
    /**
     * 查询结果行数
     */
    private Long resultCount;
    
    /**
     * 查询是否成功
     */
    private boolean success;
    
    /**
     * 错误消息（如果失败）
     */
    private String errorMessage;
    
    /**
     * 查询结果字段
     */
    private List<String> resultColumns;
    
    /**
     * 执行查询的时间
     */
    private LocalDateTime executedAt;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 是否已保存
     */
    private boolean saved;
    
    /**
     * 关联的保存查询ID（如果已保存）
     */
    private Long savedQueryId;
    
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
     * 获取查询执行的简要摘要
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        
        if (isNaturalLanguageQuery()) {
            sb.append("自然语言: ").append(truncateIfNeeded(query, 50));
        } else {
            sb.append("SQL: ").append(truncateIfNeeded(sql, 50));
        }
        
        sb.append(" [");
        if (success) {
            sb.append(resultCount).append("行, ").append(duration).append("ms");
        } else {
            sb.append("失败: ").append(truncateIfNeeded(errorMessage, 30));
        }
        sb.append("]");
        
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
