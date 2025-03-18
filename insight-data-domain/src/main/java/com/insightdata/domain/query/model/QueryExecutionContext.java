package com.insightdata.domain.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询执行上下文
 * 封装查询执行的上下文信息，包括参数、数据源、SQL等
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryExecutionContext {
    
    private static final Logger log = LoggerFactory.getLogger(QueryExecutionContext.class);
    
    /**
     * 上下文ID
     */
    private String contextId;
    
    /**
     * 关联的查询ID
     * 可能是SavedQuery的ID或QueryHistory的ID
     */
    private Long queryId;
    
    /**
     * 用户ID
     * 执行查询的用户
     */
    private Long userId;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 数据源名称
     */
    private String dataSourceName;
    
    /**
     * 数据源类型
     */
    private String dataSourceType;
    
    /**
     * 查询参数
     * 原始的查询参数
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 处理后的查询参数
     * 经过转换和验证后的查询参数
     */
    @Builder.Default
    private Map<String, Object> processedParameters = new HashMap<>();
    
    /**
     * 原始SQL
     * 从保存的查询或NL转换获得的原始SQL
     */
    private String originalSql;
    
    /**
     * 最终执行的SQL
     * 经过参数替换和优化后的SQL
     */
    private String executedSql;
    
    /**
     * 查询类型
     * SELECT, UPDATE, INSERT, DELETE
     */
    private String queryType;
    
    /**
     * 查询模式
     * NL_QUERY(自然语言查询), SAVED_QUERY(保存的查询), ADHOC(临时查询)
     */
    private String queryMode;
    
    /**
     * 最大返回记录数
     */
    private Integer maxRows;
    
    /**
     * 查询超时时间(秒)
     */
    private Integer timeoutSeconds;
    
    /**
     * 分页信息
     * 包含页码、每页大小等
     */
    @Builder.Default
    private Map<String, Object> pagination = new HashMap<>();
    
    /**
     * 排序信息
     * 包含排序字段、排序方向等
     */
    @Builder.Default
    private List<Map<String, Object>> sorting = new ArrayList<>();
    
    /**
     * 执行状态
     * PENDING, RUNNING, COMPLETED, FAILED, TIMEOUT
     */
    private String executionStatus;
    
    /**
     * 执行ID
     * 用于跟踪特定的执行实例
     */
    private String executionId;
    
    /**
     * 开始执行时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束执行时间
     */
    private LocalDateTime endTime;
    
    /**
     * 执行持续时间(毫秒)
     */
    private Long duration;
    
    /**
     * 影响的记录数
     * 对于非查询操作，表示影响的行数
     */
    private Integer affectedRows;
    
    /**
     * 返回的记录数
     * 对于查询操作，表示返回的行数
     */
    private Integer returnedRows;
    
    /**
     * 结果总数
     * 不考虑分页的总记录数
     */
    private Long totalRows;
    
    /**
     * 错误信息
     * 执行失败时的错误信息
     */
    private String errorMessage;
    
    /**
     * 错误代码
     * 执行失败时的错误代码
     */
    private String errorCode;
    
    /**
     * 错误堆栈
     * 执行失败时的详细错误堆栈
     */
    private String errorStack;
    
    /**
     * 执行结果
     * 查询执行的结果数据
     */
    private Object result;
    
    /**
     * 查询计划
     * 数据库返回的查询执行计划
     */
    private String queryPlan;
    
    /**
     * 是否缓存结果
     */
    private boolean cacheable;
    
    /**
     * 缓存键
     * 用于缓存查询结果的键
     */
    private String cacheKey;
    
    /**
     * 缓存过期时间(秒)
     */
    private Integer cacheTtl;
    
    /**
     * 是否命中缓存
     */
    private boolean cacheHit;
    
    /**
     * 是否记录查询历史
     */
    private boolean recordHistory;
    
    /**
     * 日志级别
     * 记录查询日志的级别：NONE, SIMPLE, DETAILED
     */
    private String logLevel;
    
    /**
     * 执行器ID
     * 执行查询的具体执行器标识
     */
    private String executorId;
    
    /**
     * 执行节点
     * 执行查询的服务器节点信息
     */
    private String executionNode;
    
    /**
     * 会话ID
     * 数据库会话ID
     */
    private String sessionId;
    
    /**
     * 事务ID
     * 数据库事务ID
     */
    private String transactionId;
    
    /**
     * 安全上下文
     * 包含执行权限、行级过滤等安全信息
     */
    @Builder.Default
    private Map<String, Object> securityContext = new HashMap<>();
    
    /**
     * 是否只读查询
     */
    private boolean readOnly;
    
    /**
     * 是否是导出操作
     */
    private boolean exportOperation;
    
    /**
     * 导出格式
     * CSV, EXCEL, PDF等
     */
    private String exportFormat;
    
    /**
     * 导出配置
     * 导出操作的具体配置
     */
    @Builder.Default
    private Map<String, Object> exportConfig = new HashMap<>();
    
    /**
     * 查询标签
     * 用于分类和标记查询
     */
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    /**
     * 上下文别名
     * 用户定义的友好名称
     */
    private String alias;
    
    /**
     * 接口配置ID
     * 关联的查询界面配置ID
     */
    private Long interfaceConfigId;
    
    /**
     * 是否使用SQL缓存
     * 是否使用数据库的查询缓存
     */
    private boolean useSqlCache;
    
    /**
     * 请求来源
     * API, WEB, MOBILE, BATCH等
     */
    private String requestSource;
    
    /**
     * 请求IP
     */
    private String requestIp;
    
    /**
     * 租户ID
     * 多租户环境下的租户标识
     */
    private String tenantId;
    
    /**
     * 自定义属性
     * 其他自定义属性
     */
    @Builder.Default
    private Map<String, Object> customProperties = new HashMap<>();
    
    /**
     * 查询上下文初始化时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 查询上下文更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 准备好执行参数
     * 根据参数定义和输入，处理并准备执行参数
     *
     * @param parameters 原始参数
     * @param conditionConfigs 条件配置
     * @return 处理后的参数
     */
    /**
     * 使用反射获取对象的字段值
     *
     * @param obj 对象
     * @param fieldName 字段名
     * @return 字段值
     */
    private Object getFieldValue(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            log.error("获取字段值失败: {}", fieldName, e);
            return null;
        }
    }
    
    /**
     * 检查Boolean字段值
     */
    private boolean getBooleanFieldValue(Object obj, String fieldName) {
        Object value = getFieldValue(obj, fieldName);
        return value != null && (Boolean) value;
    }

    public Map<String, Object> prepareExecutionParameters(Map<String, Object> parameters,
                                                        List<QueryConditionConfig> conditionConfigs) {
        // 实现参数处理逻辑
        Map<String, Object> processed = new HashMap<>();
        
        // 处理每个条件的参数
        if (conditionConfigs != null) {
            for (QueryConditionConfig config : conditionConfigs) {
                String paramName = (String) getFieldValue(config, "paramName");
                if (parameters.containsKey(paramName)) {
                    Object value = parameters.get(paramName);
                    
                    // 应用参数转换
                    String valueTransformer = (String) getFieldValue(config, "valueTransformer");
                    if (valueTransformer != null && !valueTransformer.trim().isEmpty()) {
                        // 这里可以使用策略模式调用具体的转换器
                        value = applyValueTransformer(value, valueTransformer);
                    }
                    
                    // 应用验证规则
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> validationRules =
                        (List<Map<String, Object>>) getFieldValue(config, "validationRules");
                    if (validationRules != null && !validationRules.isEmpty()) {
                        // 验证参数值
                        validateParameter(paramName, value, validationRules);
                    }
                    
                    processed.put(paramName, value);
                } else if (getBooleanFieldValue(config, "required")) {
                    // 必填参数缺失
                    throw new IllegalArgumentException("必填参数缺失: " + paramName);
                } else {
                    // 使用默认值
                    Object defaultValue = getFieldValue(config, "defaultValue");
                    if (defaultValue != null) {
                        processed.put(paramName, defaultValue);
                    }
                }
            }
        }
        
        // 处理分页参数
        if (parameters.containsKey("pageSize")) {
            Integer pageSize = Integer.valueOf(parameters.get("pageSize").toString());
            processed.put("pageSize", pageSize);
            
            this.pagination.put("pageSize", pageSize);
        }
        
        if (parameters.containsKey("pageNum")) {
            Integer pageNum = Integer.valueOf(parameters.get("pageNum").toString());
            processed.put("pageNum", pageNum);
            
            this.pagination.put("pageNum", pageNum);
        }
        
        // 处理排序参数
        if (parameters.containsKey("sortField") && parameters.containsKey("sortOrder")) {
            String sortField = parameters.get("sortField").toString();
            String sortOrder = parameters.get("sortOrder").toString();
            
            Map<String, Object> sortInfo = new HashMap<>();
            sortInfo.put("field", sortField);
            sortInfo.put("order", sortOrder);
            
            this.sorting.add(sortInfo);
            
            processed.put("sortField", sortField);
            processed.put("sortOrder", sortOrder);
        }
        
        this.processedParameters = processed;
        return processed;
    }
    
    /**
     * 应用参数值转换器
     * 
     * @param value 原始值
     * @param transformerName 转换器名称
     * @return 转换后的值
     */
    private Object applyValueTransformer(Object value, String transformerName) {
        // 此处应该实现调用具体转换器的逻辑
        // 可以使用工厂模式或策略模式实现不同的转换器
        return value; // 暂时直接返回原值
    }
    
    /**
     * 验证参数值
     *
     * @param paramName 参数名
     * @param value 参数值
     * @param rules 验证规则
     * @throws IllegalArgumentException 验证不通过时抛出异常
     */
    private void validateParameter(String paramName, Object value, List<Map<String, Object>> rules) {
        if (rules == null || rules.isEmpty()) {
            return; // 没有验证规则，直接返回
        }
        
        // 如果值为null，只检查required规则
        if (value == null) {
            for (Map<String, Object> rule : rules) {
                if (Boolean.TRUE.equals(rule.get("required"))) {
                    throw new IllegalArgumentException("参数 " + paramName + " 不能为空");
                }
            }
            return;
        }
        
        String stringValue = value.toString();
        
        for (Map<String, Object> rule : rules) {
            String ruleType = (String) rule.get("type");
            if (ruleType == null) {
                continue;
            }
            
            switch (ruleType) {
                case "required":
                    if (stringValue.trim().isEmpty()) {
                        throw new IllegalArgumentException("参数 " + paramName + " 不能为空");
                    }
                    break;
                    
                case "length":
                    Integer minLength = (Integer) rule.get("minLength");
                    Integer maxLength = (Integer) rule.get("maxLength");
                    
                    if (minLength != null && stringValue.length() < minLength) {
                        throw new IllegalArgumentException("参数 " + paramName + " 长度不能小于 " + minLength);
                    }
                    
                    if (maxLength != null && stringValue.length() > maxLength) {
                        throw new IllegalArgumentException("参数 " + paramName + " 长度不能大于 " + maxLength);
                    }
                    break;
                    
                case "range":
                    try {
                        double numValue = Double.parseDouble(stringValue);
                        Number minValue = (Number) rule.get("min");
                        Number maxValue = (Number) rule.get("max");
                        
                        if (minValue != null && numValue < minValue.doubleValue()) {
                            throw new IllegalArgumentException("参数 " + paramName + " 不能小于 " + minValue);
                        }
                        
                        if (maxValue != null && numValue > maxValue.doubleValue()) {
                            throw new IllegalArgumentException("参数 " + paramName + " 不能大于 " + maxValue);
                        }
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("参数 " + paramName + " 必须是有效的数字");
                    }
                    break;
                    
                case "regex":
                    String pattern = (String) rule.get("pattern");
                    if (pattern != null && !stringValue.matches(pattern)) {
                        String message = (String) rule.get("message");
                        if (message == null) {
                            message = "参数 " + paramName + " 格式不正确";
                        }
                        throw new IllegalArgumentException(message);
                    }
                    break;
                    
                case "enum":
                    @SuppressWarnings("unchecked")
                    List<String> allowedValues = (List<String>) rule.get("values");
                    if (allowedValues != null && !allowedValues.contains(stringValue)) {
                        throw new IllegalArgumentException("参数 " + paramName + " 的值必须是以下之一: " + String.join(", ", allowedValues));
                    }
                    break;
                    
                case "type":
                    String dataType = (String) rule.get("dataType");
                    if (dataType != null) {
                        boolean valid = validateDataType(stringValue, dataType);
                        if (!valid) {
                            throw new IllegalArgumentException("参数 " + paramName + " 必须是有效的 " + dataType + " 类型");
                        }
                    }
                    break;
                    
                case "custom":
                    // 自定义验证规则，根据规则名称调用相应的验证器
                    String validatorName = (String) rule.get("validator");
                    if (validatorName != null) {
                        // 这里可以通过策略模式或工厂模式调用具体的验证器
                        // 暂时直接返回，实际实现时需要替换
                    }
                    break;
                    
                default:
                    // 未知的验证规则类型，忽略
                    break;
            }
        }
    }
    
    /**
     * 验证数据类型
     *
     * @param value 字符串值
     * @param dataType 数据类型
     * @return 是否有效
     */
    private boolean validateDataType(String value, String dataType) {
        try {
            switch (dataType.toUpperCase()) {
                case "INT":
                case "INTEGER":
                    Integer.parseInt(value);
                    return true;
                    
                case "LONG":
                    Long.parseLong(value);
                    return true;
                    
                case "DOUBLE":
                case "FLOAT":
                    Double.parseDouble(value);
                    return true;
                    
                case "BOOLEAN":
                    return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
                    
                case "DATE":
                    // 简单日期格式验证，实际应用中可以使用更复杂的验证
                    return value.matches("\\d{4}-\\d{2}-\\d{2}");
                    
                case "DATETIME":
                    // 简单日期时间格式验证
                    return value.matches("\\d{4}-\\d{2}-\\d{2}(T|\\s)\\d{2}:\\d{2}(:\\d{2})?");
                    
                case "EMAIL":
                    return value.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
                    
                case "URL":
                    return value.matches("^(http|https)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(/.*)?$");
                    
                case "IP":
                    return value.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$");
                    
                default:
                    // 未知数据类型，认为验证通过
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取查询摘要信息
     * 
     * @return 查询摘要信息
     */
    public Map<String, Object> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("contextId", this.contextId);
        summary.put("dataSourceName", this.dataSourceName);
        summary.put("executionStatus", this.executionStatus);
        summary.put("startTime", this.startTime);
        summary.put("duration", this.duration);
        summary.put("returnedRows", this.returnedRows);
        summary.put("totalRows", this.totalRows);
        summary.put("queryMode", this.queryMode);
        
        if (this.executionStatus.equals("FAILED")) {
            summary.put("errorMessage", this.errorMessage);
        }
        
        return summary;
    }
    
    /**
     * 生成缓存键
     * 基于查询和参数生成唯一的缓存键
     * 
     * @return 缓存键
     */
    public String generateCacheKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.dataSourceId)
          .append(":")
          .append(this.originalSql)
          .append(":");
        
        // 将参数按照字母顺序排序并拼接
        List<String> sortedKeys = new ArrayList<>(this.processedParameters.keySet());
        java.util.Collections.sort(sortedKeys);
        
        for (String key : sortedKeys) {
            Object value = this.processedParameters.get(key);
            sb.append(key).append("=").append(value).append("&");
        }
        
        // 生成MD5哈希作为缓存键
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(sb.toString().getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            this.cacheKey = hexString.toString();
            return this.cacheKey;
        } catch (java.security.NoSuchAlgorithmException e) {
            // fallback到简单字符串如果MD5不可用
            this.cacheKey = sb.toString();
            return this.cacheKey;
        }
    }
    
    /**
     * 记录查询开始
     */
    public void markExecutionStart() {
        this.startTime = LocalDateTime.now();
        this.executionStatus = "RUNNING";
    }
    
    /**
     * 记录查询完成
     * 
     * @param result 查询结果
     * @param returnedRows 返回的行数
     * @param totalRows 总行数
     */
    public void markExecutionComplete(Object result, Integer returnedRows, Long totalRows) {
        this.endTime = LocalDateTime.now();
        this.duration = java.time.Duration.between(this.startTime, this.endTime).toMillis();
        this.executionStatus = "COMPLETED";
        this.result = result;
        this.returnedRows = returnedRows;
        this.totalRows = totalRows;
    }
    
    /**
     * 记录查询失败
     * 
     * @param errorCode 错误代码
     * @param errorMessage 错误信息
     * @param errorStack 错误堆栈
     */
    public void markExecutionFailed(String errorCode, String errorMessage, String errorStack) {
        this.endTime = LocalDateTime.now();
        this.duration = java.time.Duration.between(this.startTime, this.endTime).toMillis();
        this.executionStatus = "FAILED";
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorStack = errorStack;
    }
    
    /**
     * 记录查询超时
     */
    public void markExecutionTimeout() {
        this.endTime = LocalDateTime.now();
        this.duration = java.time.Duration.between(this.startTime, this.endTime).toMillis();
        this.executionStatus = "TIMEOUT";
        this.errorMessage = "查询超时，超过 " + this.timeoutSeconds + " 秒";
    }
}