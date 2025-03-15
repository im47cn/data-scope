package com.insightdata.nlquery.intent;

import com.insightdata.nlquery.preprocess.DefaultTextPreprocessor;
import com.insightdata.nlquery.preprocess.PreprocessedText;
import com.insightdata.nlquery.preprocess.TextPreprocessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 意图识别器测试类
 */
public class IntentRecognizerTest {
    
    private IntentRecognizer intentRecognizer;
    private TextPreprocessor textPreprocessor;
    
    @BeforeEach
    public void setUp() {
        intentRecognizer = new RuleBasedIntentRecognizer();
        textPreprocessor = new DefaultTextPreprocessor();
    }
    
    @Test
    public void testSelectQuery() {
        String query = "查询所有用户的姓名和邮箱";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertEquals(QueryType.SELECT, intent.getQueryType());
        assertEquals(QueryPurpose.DATA_RETRIEVAL, intent.getQueryPurpose());
    }
    
    @Test
    public void testCountQuery() {
        String query = "统计最近30天内注册的用户数量";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertEquals(QueryType.COUNT, intent.getQueryType());
        assertEquals(QueryPurpose.STATISTICAL_ANALYSIS, intent.getQueryPurpose());
        assertNotNull(intent.getTimeRange());
        assertEquals(TimeRange.TimeType.RELATIVE, intent.getTimeRange().getTimeType());
        assertEquals(TimeRange.TimeUnit.DAY, intent.getTimeRange().getTimeUnit());
        assertEquals(30, intent.getTimeRange().getTimeValue());
    }
    
    @Test
    public void testSumQuery() {
        String query = "计算上个月销售总额";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertEquals(QueryType.SUM, intent.getQueryType());
        assertNotNull(intent.getTimeRange());
        assertEquals(TimeRange.TimeUnit.MONTH, intent.getTimeRange().getTimeUnit());
    }
    
    @Test
    public void testAvgQuery() {
        String query = "查询员工的平均薪资";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertEquals(QueryType.AVG, intent.getQueryType());
    }
    
    @Test
    public void testMaxQuery() {
        String query = "查询最高销售额";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertEquals(QueryType.MAX, intent.getQueryType());
    }
    
    @Test
    public void testMinQuery() {
        String query = "查询最低库存";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertEquals(QueryType.MIN, intent.getQueryType());
    }
    
    @Test
    public void testGroupQuery() {
        String query = "按部门统计员工数量";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertEquals(QueryType.GROUP, intent.getQueryType());
    }
    
    @Test
    public void testSortRequirement() {
        String query = "查询所有用户按注册时间降序排列";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertNotNull(intent.getSortRequirements());
        assertFalse(intent.getSortRequirements().isEmpty());
        assertEquals(SortRequirement.SortDirection.DESC, intent.getSortRequirements().get(0).getDirection());
        assertEquals("注册时间", intent.getSortRequirements().get(0).getField());
    }
    
    @Test
    public void testLimitRequirement() {
        String query = "查询销售额前10的产品";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertNotNull(intent.getLimitRequirement());
        assertEquals(LimitRequirement.LimitType.TOP_N, intent.getLimitRequirement().getLimitType());
        assertEquals(10, intent.getLimitRequirement().getLimitValue());
    }
    
    @Test
    public void testPaginationRequirement() {
        String query = "查询第2页的用户列表";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertNotNull(intent.getLimitRequirement());
        assertEquals(LimitRequirement.LimitType.PAGINATION, intent.getLimitRequirement().getLimitType());
        assertEquals(10, intent.getLimitRequirement().getLimitValue());
        assertEquals(10, intent.getLimitRequirement().getOffset());
    }
    
    @Test
    public void testComplexQuery() {
        String query = "统计最近6个月各部门的平均销售额，按销售额降序排列，只显示前5名";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        QueryIntent intent = intentRecognizer.recognizeIntent(preprocessedText);
        
        assertEquals(QueryType.AVG, intent.getQueryType());
        assertEquals(QueryPurpose.STATISTICAL_ANALYSIS, intent.getQueryPurpose());
        
        assertNotNull(intent.getTimeRange());
        assertEquals(TimeRange.TimeType.RELATIVE, intent.getTimeRange().getTimeType());
        assertEquals(TimeRange.TimeUnit.MONTH, intent.getTimeRange().getTimeUnit());
        assertEquals(6, intent.getTimeRange().getTimeValue());
        
        assertNotNull(intent.getSortRequirements());
        assertFalse(intent.getSortRequirements().isEmpty());
        assertEquals(SortRequirement.SortDirection.DESC, intent.getSortRequirements().get(0).getDirection());
        
        assertNotNull(intent.getLimitRequirement());
        assertEquals(LimitRequirement.LimitType.TOP_N, intent.getLimitRequirement().getLimitType());
        assertEquals(5, intent.getLimitRequirement().getLimitValue());
    }
}
