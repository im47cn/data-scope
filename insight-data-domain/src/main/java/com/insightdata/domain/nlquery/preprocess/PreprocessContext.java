package com.insightdata.domain.nlquery.preprocess;

import lombok.Builder;
import lombok.Data;

/**
 * 预处理上下文
 */
@Data
@Builder
public class PreprocessContext {

    /**
     * 数据源ID
     */
    private Long dataSourceId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 领域
     */
    private String domain;

    /**
     * 语言
     */
    private String language;

    /**
     * 时区
     */
    private String timezone;

    /**
     * 创建一个基本的预处理上下文
     */
    public static PreprocessContext basic(Long dataSourceId, Long userId) {
        return PreprocessContext.builder()
                .dataSourceId(dataSourceId)
                .userId(userId)
                .build();
    }

    /**
     * 创建一个带会话信息的预处理上下文
     */
    public static PreprocessContext withSession(Long dataSourceId, Long userId, String sessionId) {
        return PreprocessContext.builder()
                .dataSourceId(dataSourceId)
                .userId(userId)
                .sessionId(sessionId)
                .build();
    }

    /**
     * 创建一个完整的预处理上下文
     */
    public static PreprocessContext complete(Long dataSourceId, Long userId, String sessionId,
            String domain, String language, String timezone) {
        return PreprocessContext.builder()
                .dataSourceId(dataSourceId)
                .userId(userId)
                .sessionId(sessionId)
                .domain(domain)
                .language(language)
                .timezone(timezone)
                .build();
    }
}
