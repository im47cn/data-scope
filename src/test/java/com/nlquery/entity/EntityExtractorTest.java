package com.nlquery.entity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nlquery.preprocess.DefaultTextPreprocessor;
import com.nlquery.preprocess.EntityTag;
import com.nlquery.preprocess.EntityType;
import com.nlquery.preprocess.PreprocessedText;
import com.nlquery.preprocess.TextPreprocessor;

/**
 * 实体提取器测试类
 */
public class EntityExtractorTest {
    
    private EntityExtractor entityExtractor;
    private TextPreprocessor textPreprocessor;
    
    @BeforeEach
    public void setUp() {
        entityExtractor = new RuleBasedEntityExtractor();
        textPreprocessor = new DefaultTextPreprocessor();
    }
    
    @Test
    public void testExtractTableEntity() {
        String query = "查询用户表中的所有数据";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.TABLE && e.getText().contains("用户")));
    }
    
    @Test
    public void testExtractColumnEntity() {
        String query = "查询用户的姓名和邮箱";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.COLUMN && e.getText().contains("姓名")));
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.COLUMN && e.getText().contains("邮箱")));
    }
    
    @Test
    public void testExtractValueEntity() {
        String query = "查询价格大于100的产品";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.VALUE && e.getText().contains("100")));
    }
    
    @Test
    public void testExtractFunctionEntity() {
        String query = "计算订单的总金额";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.FUNCTION && e.getText().contains("计算")));
    }
    
    @Test
    public void testExtractOperatorEntity() {
        String query = "查询价格大于100且小于200的产品";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.OPERATOR && e.getText().contains("大于")));
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.OPERATOR && e.getText().contains("小于")));
    }
    
    @Test
    public void testExtractConditionEntity() {
        String query = "查询条件为状态等于'已完成'的订单";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.CONDITION && e.getText().contains("条件")));
    }
    
    @Test
    public void testExtractOrderEntity() {
        String query = "查询用户按注册时间排序";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.ORDER && e.getText().contains("排序")));
    }
    
    @Test
    public void testExtractLimitEntity() {
        String query = "查询前10条订单记录";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.LIMIT && e.getText().contains("前")));
    }
    
    @Test
    public void testExtractGroupEntity() {
        String query = "按部门分组统计员工数量";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.GROUP && e.getText().contains("分组")));
    }
    
    @Test
    public void testExtractDateEntity() {
        String query = "查询2023-01-01到2023-12-31之间的订单";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.DATE && e.getText().contains("2023-01-01")));
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.DATE && e.getText().contains("2023-12-31")));
    }
    
    @Test
    public void testExtractNumberEntity() {
        String query = "查询销售额超过10000的员工";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.NUMBER && e.getText().contains("10000")));
    }
    
    @Test
    public void testExtractStringEntity() {
        String query = "查询名称包含'测试'的产品";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.STRING && e.getText().contains("'测试'")));
    }
    
    @Test
    public void testExtractBooleanEntity() {
        String query = "查询状态为true的任务";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.BOOLEAN && e.getText().contains("true")));
    }
    
    @Test
    public void testComplexQuery() {
        String query = "查询销售部门中2023年销售额大于10000的员工，按销售额降序排列，只显示前5名";
        PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
        List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText);
        
        assertFalse(entities.isEmpty());
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.TABLE && e.getText().contains("部门")));
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.COLUMN && e.getText().contains("销售额")));
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.NUMBER && e.getText().contains("10000")));
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.ORDER && e.getText().contains("降序")));
        assertTrue(entities.stream().anyMatch(e -> e.getType() == EntityType.LIMIT && e.getText().contains("前")));
    }
}