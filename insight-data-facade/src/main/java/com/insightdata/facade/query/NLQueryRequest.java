package com.insightdata.facade.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 自然语言查询请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryRequest {

    /**
     * 数据源ID
     */
    @NotNull(message = "数据源ID不能为空")
    private String dataSourceId;

    /**
     * 自然语言查询
     */
    @NotBlank(message = "查询语句不能为空")
    private String query;

    /**
     * 查询参数
     */
    private Map<String, Object> parameters;

    /**
     * 最大返回行数
     */
    private Integer maxRows;

    /**
     * 超时时间（毫秒）
     */
    private Long timeout;
}