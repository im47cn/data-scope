package com.insightdata.nlquery.intent;

import com.insightdata.nlquery.preprocess.PreprocessedText;
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
    
    @Override
    public QueryIntent recognizeIntent(PreprocessedText preprocessedText, IntentRecognitionContext context) {
        try {
            String text = preprocessedText.getNormalizedText().toLowerCase();
            
            // 识别查询类型
            QueryType queryType = recognizeQueryType(text);
            QueryPurpose queryPurpose = recognizeQueryPurpose(text);
            TimeRange timeRange = recognizeTimeRange(text);
            List<SortRequirement> sortRequirements = recognizeSortRequirements(text);
            LimitRequirement limitRequirement = recognizeLimitRequirement(text);
            
            // 构建查询意图
            QueryIntent intent = new QueryIntent();
            intent.setQueryType(queryType);
            intent.setQueryPurpose(queryPurpose);
            intent.setTimeRange(timeRange);
            intent.setSortRequirements(sortRequirements);
            intent.setLimitRequirement(limitRequirement);
            intent.setConfidence(1.0);
            
            return intent;
        } catch (Exception e) {
            log.error("意图识别失败", e);
            QueryIntent intent = new QueryIntent();
            intent.setQueryType(QueryType.UNKNOWN);
            intent.setQueryPurpose(QueryPurpose.UNKNOWN);
            intent.setConfidence(0.0);
            return intent;
        }
    }
    
    private QueryType recognizeQueryType(String text) {
        if (text.contains("计数") || text.contains("多少") || text.contains("数量")) {
            return QueryType.COUNT;
        } else if (text.contains("求和") || text.contains("总和") || text.contains("合计")) {
            return QueryType.SUM;
        } else if (text.contains("平均") || text.contains("均值")) {
            return QueryType.AVG;
        } else if (text.contains("最大") || text.contains("最高") || text.contains("最多")) {
            return QueryType.MAX;
        } else if (text.contains("最小") || text.contains("最低") || text.contains("最少")) {
            return QueryType.MIN;
        } else if (text.contains("分组") || text.contains("按") || text.contains("group")) {
            return QueryType.GROUP;
        } else {
            return QueryType.SELECT;
        }
    }
    
    private QueryPurpose recognizeQueryPurpose(String text) {
        if (text.contains("统计") || text.contains("计算")) {
            return QueryPurpose.STATISTICAL_ANALYSIS;
        } else if (text.contains("趋势") || text.contains("变化")) {
            return QueryPurpose.TREND_ANALYSIS;
        } else if (text.contains("对比") || text.contains("比较")) {
            return QueryPurpose.COMPARISON_ANALYSIS;
        } else if (text.contains("分组") || text.contains("分类")) {
            return QueryPurpose.GROUP_ANALYSIS;
        } else if (text.contains("排名") || text.contains("前") || text.contains("后")) {
            return QueryPurpose.RANKING_ANALYSIS;
        } else if (text.contains("占比") || text.contains("百分比")) {
            return QueryPurpose.PROPORTION_ANALYSIS;
        } else if (text.contains("关联") || text.contains("相关")) {
            return QueryPurpose.CORRELATION_ANALYSIS;
        } else {
            return QueryPurpose.DATA_RETRIEVAL;
        }
    }
    
    private TimeRange recognizeTimeRange(String text) {
        Pattern pattern = Pattern.compile("(最近|过去|未来)?\\s*(\\d+)\\s*(年|月|周|天|小时|分钟)");
        Matcher matcher = pattern.matcher(text);
        
        if (matcher.find()) {
            int value = Integer.parseInt(matcher.group(2));
            String unit = matcher.group(3);
            
            TimeRange timeRange = new TimeRange();
            timeRange.setRangeType(TimeRange.TimeRangeType.RELATIVE);
            timeRange.setTimeType(TimeRange.TimeType.RELATIVE);
            timeRange.setTimeValue(value);
            
            switch (unit) {
                case "年":
                    timeRange.setTimeUnit(TimeRange.TimeUnit.YEAR);
                    break;
                case "月":
                    timeRange.setTimeUnit(TimeRange.TimeUnit.MONTH);
                    break;
                case "周":
                    timeRange.setTimeUnit(TimeRange.TimeUnit.WEEK);
                    break;
                case "天":
                    timeRange.setTimeUnit(TimeRange.TimeUnit.DAY);
                    break;
                case "小时":
                    timeRange.setTimeUnit(TimeRange.TimeUnit.HOUR);
                    break;
                case "分钟":
                    timeRange.setTimeUnit(TimeRange.TimeUnit.MINUTE);
                    break;
                default:
                    timeRange.setTimeUnit(TimeRange.TimeUnit.DAY);
            }
            
            return timeRange;
        }
        
        return null;
    }
    
    private List<SortRequirement> recognizeSortRequirements(String text) {
        List<SortRequirement> requirements = new ArrayList<>();
        
        if (text.contains("降序") || text.contains("从大到小")) {
            requirements.add(SortRequirement.desc(""));
        } else if (text.contains("升序") || text.contains("从小到大")) {
            requirements.add(SortRequirement.asc(""));
        }
        
        return requirements;
    }
    
    private LimitRequirement recognizeLimitRequirement(String text) {
        Pattern pattern = Pattern.compile("(前|后|top|bottom)\\s*(\\d+)");
        Matcher matcher = pattern.matcher(text.toLowerCase());
        
        if (matcher.find()) {
            int value = Integer.parseInt(matcher.group(2));
            String direction = matcher.group(1);
            
            LimitRequirement requirement = new LimitRequirement();
            requirement.setLimitValue(value);
            
            if (direction.equals("前") || direction.equals("top")) {
                requirement.setLimitType(LimitType.TOP_N);
            } else {
                requirement.setLimitType(LimitType.BOTTOM_N);
            }
            
            return requirement;
        }
        
        return null;
    }
}
