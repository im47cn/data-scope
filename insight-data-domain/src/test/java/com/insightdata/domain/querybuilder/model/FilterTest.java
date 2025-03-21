package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Filter 的单元测试类
 */
class FilterTest {

    private QueryModelContract mockModel;

    @BeforeEach
    void setUp() {
        mockModel = Mockito.mock(QueryModelContract.class);
    }

    @Test
    void shouldCreateEmptyFilter() {
        Filter filter = new Filter();
        
        assertNull(filter.getField());
        assertNull(filter.getOperator());
        assertNull(filter.getValue());
        assertNull(filter.getQueryModel());
    }

    @Test
    void shouldCreateFilterWithBasicInfo() {
        Filter filter = new Filter("name", "=", "John");
        
        assertEquals("name", filter.getField());
        assertEquals("=", filter.getOperator());
        assertEquals("John", filter.getValue());
        assertNull(filter.getQueryModel());
    }

    @Test
    void shouldCreateFilterWithFullInfo() {
        Filter filter = new Filter("name", "=", "John", mockModel);
        
        assertEquals("name", filter.getField());
        assertEquals("=", filter.getOperator());
        assertEquals("John", filter.getValue());
        assertSame(mockModel, filter.getQueryModel());
    }

    @Test
    void shouldGenerateExpressionForBasicOperators() {
        Filter filter1 = new Filter("age", "=", 25);
        assertEquals("age = 25", filter1.getExpression());

        Filter filter2 = new Filter("name", "LIKE", "John%");
        assertEquals("name LIKE 'John%'", filter2.getExpression());

        Filter filter3 = new Filter("active", "=", true);
        assertEquals("active = true", filter3.getExpression());
    }

    @Test
    void shouldGenerateExpressionForNullOperators() {
        Filter filter1 = new Filter("name", "IS NULL", null);
        assertEquals("name IS NULL", filter1.getExpression());

        Filter filter2 = new Filter("name", "IS NOT NULL", null);
        assertEquals("name IS NOT NULL", filter2.getExpression());
    }

    @Test
    void shouldGenerateExpressionForInOperator() {
        Filter filter = new Filter("id", "IN", Arrays.asList(1, 2, 3));
        assertEquals("id IN (1, 2, 3)", filter.getExpression());
    }

    @Test
    void shouldGenerateExpressionForBetweenOperator() {
        Filter filter = new Filter("age", "BETWEEN", new Object[]{20, 30});
        assertEquals("age BETWEEN 20 AND 30", filter.getExpression());
    }

    @Test
    void shouldHandleSpecialCharactersInValues() {
        Filter filter = new Filter("name", "=", "O'Connor");
        assertEquals("name = 'O''Connor'", filter.getExpression());
    }

    @Test
    void shouldCopyFilter() {
        Filter original = new Filter("name", "=", "John", mockModel);
        original.setDescription("Test filter");

        Filter copy = original.copy();

        assertEquals(original.getField(), copy.getField());
        assertEquals(original.getOperator(), copy.getOperator());
        assertEquals(original.getValue(), copy.getValue());
        assertEquals(original.getDescription(), copy.getDescription());
        assertSame(original.getQueryModel(), copy.getQueryModel()); // 浅复制
    }

    @Test
    void shouldValidateFilter() {
        Filter validFilter = new Filter("name", "=", "John");
        assertTrue(validFilter.isValid());

        Filter nullFieldFilter = new Filter(null, "=", "John");
        assertFalse(nullFieldFilter.isValid());

        Filter emptyFieldFilter = new Filter("", "=", "John");
        assertFalse(emptyFieldFilter.isValid());

        Filter nullOperatorFilter = new Filter("name", null, "John");
        assertFalse(nullOperatorFilter.isValid());

        Filter nullValueFilter = new Filter("name", "=", null);
        assertFalse(nullValueFilter.isValid());

        Filter isNullFilter = new Filter("name", "IS NULL", null);
        assertTrue(isNullFilter.isValid());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        Filter filter1 = new Filter("name", "=", "John");
        Filter filter2 = new Filter("name", "=", "John");
        Filter filter3 = new Filter("name", "=", "Jane");

        assertEquals(filter1, filter2);
        assertNotEquals(filter1, filter3);
        assertEquals(filter1.hashCode(), filter2.hashCode());
    }

    @Test
    void shouldHandleNullValuesInEqualsAndHashCode() {
        Filter filter1 = new Filter(null, null, null);
        Filter filter2 = new Filter(null, null, null);
        Filter filter3 = new Filter("name", null, null);

        assertEquals(filter1, filter2);
        assertNotEquals(filter1, filter3);
        assertEquals(filter1.hashCode(), filter2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        Filter filter = new Filter("name", "=", "John");
        assertEquals("name = 'John'", filter.toString());
    }

    @Test
    void shouldHandleInvalidExpressions() {
        Filter invalidInFilter = new Filter("id", "IN", "not a list");
        assertEquals("", invalidInFilter.getExpression());

        Filter invalidBetweenFilter = new Filter("age", "BETWEEN", new Object[]{20});
        assertEquals("", invalidBetweenFilter.getExpression());
    }
}