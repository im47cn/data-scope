package com.insightdata.domain.nlquery.intent;

import com.insightdata.domain.nlquery.preprocess.PreprocessedText;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于规则的意图识别器
 */
@Slf4j
@Component
public class RuleBasedIntentRecognizer implements IntentRecognizer {

    // 查询类型相关的正则表达式
    private static final Pattern SELECT_PATTERN = Pattern.compile("查询|获取|显示|列出|查看|select|get|show|list|display", Pattern.CASE_INSENSITIVE);
    private static final Pattern COUNT_PATTERN = Pattern.compile("计数|统计|数量|多少|count|how many", Pattern.CASE_INSENSITIVE);
    private static final Pattern SUM_PATTERN = Pattern.compile("求和|总和|总计|计算.*总|sum|total", Pattern.CASE_INSENSITIVE);
    private static final Pattern AVG_PATTERN = Pattern.compile("平均|均值|平均值|average|avg|mean", Pattern.CASE_INSENSITIVE);
    private static final Pattern MAX_PATTERN = Pattern.compile("最大|最高|最多|maximum|max|highest", Pattern.CASE_INSENSITIVE);
    private static final Pattern MIN_PATTERN = Pattern.compile("最小|最低|最少|minimum|min|lowest", Pattern.CASE_INSENSITIVE);
    private static final Pattern GROUP_PATTERN = Pattern.compile("分组|按.*分|按.*统计|group by|grouped by", Pattern.CASE_INSENSITIVE);

    // 查询目的相关的正则表达式
    private static final Pattern STATISTICAL_PATTERN = Pattern.compile("统计|分析|汇总|statistics|analysis|summary", Pattern.CASE_INSENSITIVE);
    private static final Pattern TREND_PATTERN = Pattern.compile("趋势|变化|走势|trend|change|movement", Pattern.CASE_INSENSITIVE);
    private static final Pattern COMPARATIVE_PATTERN = Pattern.compile("比较|对比|相比|compare|comparison|versus|vs", Pattern.CASE_INSENSITIVE);
    private static final Pattern ANOMALY_PATTERN = Pattern.compile("异常|不正常|异动|anomaly|abnormal|unusual", Pattern.CASE_INSENSITIVE);

    // 时间范围相关的正则表达式
    private static final Pattern RECENT_PATTERN = Pattern.compile("最近|近期|recent|lately", Pattern.CASE_INSENSITIVE);
    private static final Pattern PAST_PATTERN = Pattern.compile("过去|之前|past|previous", Pattern.CASE_INSENSITIVE);
    private static final Pattern FUTURE_PATTERN = Pattern.compile("未来|将来|之后|future|coming|next", Pattern.CASE_INSENSITIVE);
    private static final Pattern DAY_PATTERN = Pattern.compile("天|日|day", Pattern.CASE_INSENSITIVE);
    private static final Pattern WEEK_PATTERN = Pattern.compile("周|星期|week", Pattern.CASE_INSENSITIVE);
    private static final Pattern MONTH_PATTERN = Pattern.compile("月|month", Pattern.CASE_INSENSITIVE);
    private static final Pattern QUARTER_PATTERN = Pattern.compile("季度|季|quarter", Pattern.CASE_INSENSITIVE);
    private static final Pattern YEAR_PATTERN = Pattern.compile("年|year", Pattern.CASE_INSENSITIVE);

    // 排序相关的正则表达式
    private static final Pattern SORT_PATTERN = Pattern.compile("排序|按.*排|sort|order by", Pattern.CASE_INSENSITIVE);
    private static final Pattern SORT_PATTERN_GROUP = Pattern.compile("按\\s*([^排]+?)(?:排序|排列|升序|降序|$)|by\\s+([^\\s]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern ASC_PATTERN = Pattern.compile("升序|从小到大|从低到高|asc|ascending", Pattern.CASE_INSENSITIVE);
    private static final Pattern DESC_PATTERN = Pattern.compile("降序|从大到小|从高到低|desc|descending", Pattern.CASE_INSENSITIVE);

    // 限制相关的正则表达式
    private static final Pattern TOP_PATTERN = Pattern.compile("前\\d+|top\\s*\\d+|limit\\s*\\d+", Pattern.CASE_INSENSITIVE);
    private static final Pattern PAGE_PATTERN = Pattern.compile("分页|第\\d+页|page", Pattern.CASE_INSENSITIVE);

    @Override
    public QueryIntent recognizeIntent(PreprocessedText preprocessedText) {
        return recognizeIntent(preprocessedText, new IntentRecognitionContext());
    }

    @Override
    public QueryIntent recognizeIntent(PreprocessedText preprocessedText, IntentRecognitionContext context) {
        String normalizedText = preprocessedText.getNormalizedText();
        List<String> tokens = preprocessedText.getTokens();

        // 构建查询意图
        QueryIntent.QueryIntentBuilder builder = QueryIntent.builder();

        // 识别查询类型
        builder.queryType(recognizeQueryType(normalizedText, tokens));

        // 识别查询目的
        builder.queryPurpose(recognizeQueryPurpose(normalizedText, tokens));

        // 识别时间范围
        builder.timeRange(recognizeTimeRange(normalizedText, tokens));

        // 识别排序要求
        builder.sortRequirements(recognizeSortRequirements(normalizedText, tokens));

        // 识别限制条件
        builder.limitRequirement(recognizeLimitRequirement(normalizedText, tokens));

        // 设置置信度
        builder.confidence(0.8);

        return builder.build();
    }

    /**
     * 识别查询类型
     */
    private QueryType recognizeQueryType(String normalizedText, List<String> tokens) {
        // 按优先级顺序检查
        if (AVG_PATTERN.matcher(tokens.toString()).find()) {
            return QueryType.AVG;
        } else if (SUM_PATTERN.matcher(tokens.toString()).find()) {
            return QueryType.SUM;
        } else if (MAX_PATTERN.matcher(tokens.toString()).find()) {
            return QueryType.MAX;
        } else if (MIN_PATTERN.matcher(tokens.toString()).find()) {
            return QueryType.MIN;
        } else if (GROUP_PATTERN.matcher(tokens.toString()).find()) {
            return QueryType.GROUP;
        } else if (COUNT_PATTERN.matcher(tokens.toString()).find()) {
            return QueryType.COUNT;
        } else if (SELECT_PATTERN.matcher(tokens.toString()).find()) {
            return QueryType.SELECT;
        } else {
            return QueryType.UNKNOWN;
        }
    }

    /**
     * 识别查询目的
     */
    private QueryPurpose recognizeQueryPurpose(String normalizedText, List<String> tokens) {
        if (STATISTICAL_PATTERN.matcher(normalizedText).find()) {
            return QueryPurpose.STATISTICAL_ANALYSIS;
        } else if (TREND_PATTERN.matcher(normalizedText).find()) {
            return QueryPurpose.TREND_ANALYSIS;
        } else if (COMPARATIVE_PATTERN.matcher(normalizedText).find()) {
            return QueryPurpose.COMPARISON;
        } else if (ANOMALY_PATTERN.matcher(normalizedText).find()) {
            return QueryPurpose.ANOMALY_DETECTION;
        } else {
            return QueryPurpose.DATA_RETRIEVAL;
        }
    }

    /**
     * 识别时间范围
     */
    private TimeRange recognizeTimeRange(String normalizedText, List<String> tokens) {
        TimeRange.TimeRangeBuilder builder = TimeRange.builder();

        // 默认为未指定
        builder.timeType(TimeRange.TimeType.UNSPECIFIED);

        // 识别时间单位
        if (DAY_PATTERN.matcher(normalizedText).find()) {
            builder.timeUnit(TimeRange.TimeUnit.DAY);
        } else if (WEEK_PATTERN.matcher(normalizedText).find()) {
            builder.timeUnit(TimeRange.TimeUnit.WEEK);
        } else if (MONTH_PATTERN.matcher(normalizedText).find()) {
            builder.timeUnit(TimeRange.TimeUnit.MONTH);
        } else if (QUARTER_PATTERN.matcher(normalizedText).find()) {
            builder.timeUnit(TimeRange.TimeUnit.QUARTER);
        } else if (YEAR_PATTERN.matcher(normalizedText).find()) {
            builder.timeUnit(TimeRange.TimeUnit.YEAR);
        }

        // 识别相对时间
        if (RECENT_PATTERN.matcher(normalizedText).find() || PAST_PATTERN.matcher(normalizedText).find()) {
            builder.timeType(TimeRange.TimeType.RELATIVE);

            // 尝试提取数字
            int timeValue = extractTimeValue(normalizedText);
            builder.timeValue(timeValue > 0 ? timeValue : 30); // 默认30
        }

        return builder.build();
    }

    /**
     * 提取时间值
     */
    private int extractTimeValue(String text) {
        // 简单实现：查找数字
        java.util.regex.Matcher matcher = Pattern.compile("\\d+").matcher(text);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return -1;
    }

    /**
     * 识别排序要求
     */
    private List<SortRequirement> recognizeSortRequirements(String normalizedText, List<String> tokens) {
        List<SortRequirement> requirements = new ArrayList<>();

        if (SORT_PATTERN.matcher(normalizedText).find()) {
            SortRequirement.SortDirection direction = SortRequirement.SortDirection.ASC;

            if (DESC_PATTERN.matcher(normalizedText).find()) {
                direction = SortRequirement.SortDirection.DESC;
            } else if (ASC_PATTERN.matcher(normalizedText).find()) {
                direction = SortRequirement.SortDirection.ASC;
            }

            // 尝试提取排序字段
            String field = extractSortField(normalizedText, tokens);

            if (field != null) {
                requirements.add(SortRequirement.builder()
                        .field(field)
                        .direction(direction)
                        .priority(1)
                        .build());
            }
        }

        return requirements;
    }

    /**
     * 提取排序字段
     */
    private String extractSortField(String normalizedText, List<String> tokens) {
        // 改进的实现：查找"按"或"by"后面的词，但排除"排序"、"排列"等词
        Matcher matcher = SORT_PATTERN_GROUP.matcher(normalizedText);
        if (matcher.find()) {
            String field = matcher.group(1);
            if (field == null) {
                field = matcher.group(2);
            }
            return field != null ? field.trim() : null;
        }
        return null;
    }

    /**
     * 识别限制条件
     */
    private LimitRequirement recognizeLimitRequirement(String normalizedText, List<String> tokens) {
        LimitRequirement.LimitRequirementBuilder builder = LimitRequirement.builder();

        // 默认为无限制
        builder.limitType(LimitType.NONE);

        // 识别TOP N
        java.util.regex.Matcher topMatcher = Pattern.compile("前(\\d+)|top\\s*(\\d+)|limit\\s*(\\d+)", Pattern.CASE_INSENSITIVE).matcher(normalizedText);
        if (topMatcher.find()) {
            builder.limitType(LimitType.TOP_N);

            String value = topMatcher.group(1);
            if (value == null) {
                value = topMatcher.group(2);
            }
            if (value == null) {
                value = topMatcher.group(3);
            }

            builder.limitValue(value != null ? Integer.parseInt(value) : 10); // 默认10
        }

        // 识别分页
        java.util.regex.Matcher pageMatcher = Pattern.compile("第(\\d+)页|page\\s*(\\d+)", Pattern.CASE_INSENSITIVE).matcher(normalizedText);
        if (pageMatcher.find()) {
            builder.limitType(LimitType.PAGINATION);

            String value = pageMatcher.group(1);
            if (value == null) {
                value = pageMatcher.group(2);
            }

            int page = value != null ? Integer.parseInt(value) : 1; // 默认第1页
            builder.offset((page - 1) * 10); // 默认每页10条
            builder.limitValue(10); // 默认每页10条
        }

        return builder.build();
    }
}
