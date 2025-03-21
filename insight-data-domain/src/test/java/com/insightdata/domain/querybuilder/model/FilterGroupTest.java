package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FilterGroup 的单元测试类
 */
class FilterGroupTest {

    private QueryModelContract mockModel;
    private Filter mockFilter1;
    private Filter mockFilter2;

    @BeforeEach
    void setUp() {
        mockModel = Mockito.mock(QueryModelContract.class);
        
        mockFilter1 = Mockito.mock(Filter.class);
        Mockito.when(mockFilter1.getExpression()).thenReturn("field1 = 'value1'");
        Mockito.when(mockFilter1.isValid()).thenReturn(true);
        
        mockFilter2 = Mockito.mock(Filter.class);
        Mockito.when(mockFilter2.getExpression()).thenReturn("field2 = 'value2'");
        Mockito.when(mockFilter2.isValid()).thenReturn(true);
    }

    @Test
    void shouldCreateEmptyFilterGroup() {
        FilterGroup group = new FilterGroup();
        
        assertNotNull(group.getFilters());
        assertNotNull(group.getGroups());
        assertEquals("AND", group.getOperator());
        assertTrue(group.getFilters().isEmpty());
        assertTrue(group.getGroups().isEmpty());
    }

    @Test
    void shouldCreateFilterGroupWithOperator() {
        FilterGroup group = new FilterGroup("OR");
        assertEquals("OR", group.getOperator());

        FilterGroup groupWithNull = new FilterGroup(null);
        assertEquals("AND", groupWithNull.getOperator());
    }

    @Test
    void shouldHandleFilters() {
        FilterGroup group = new FilterGroup();
        
        group.addFilter(mockFilter1);
        group.addFilter(mockFilter2);
        group.addFilter(null); // 应该忽略 null

        assertEquals(2, group.getFilters().size());
    }

    @Test
    void shouldHandleNestedGroups() {
        FilterGroup parentGroup = new FilterGroup("AND");
        FilterGroup childGroup = new FilterGroup("OR");
        
        childGroup.addFilter(mockFilter1);
        childGroup.addFilter(mockFilter2);
        
        parentGroup.addGroup(childGroup);
        parentGroup.addGroup(null); // 应该忽略 null

        assertEquals(1, parentGroup.getGroups().size());
        assertEquals(2, parentGroup.getGroups().get(0).getFilters().size());
    }

    @Test
    void shouldGenerateExpression() {
        FilterGroup group = new FilterGroup("AND");
        group.addFilter(mockFilter1);
        group.addFilter(mockFilter2);
        
        String expected = "field1 = 'value1' AND field2 = 'value2'";
        assertEquals(expected, group.getExpression());
    }

    @Test
    void shouldGenerateNestedExpression() {
        FilterGroup parentGroup = new FilterGroup("OR");
        FilterGroup childGroup = new FilterGroup("AND");
        
        childGroup.addFilter(mockFilter1);
        childGroup.addFilter(mockFilter2);
        
        parentGroup.addFilter(mockFilter1);
        parentGroup.addGroup(childGroup);
        
        String expected = "field1 = 'value1' OR (field1 = 'value1' AND field2 = 'value2')";
        assertEquals(expected, parentGroup.getExpression());
    }

    @Test
    void shouldCopyFilterGroup() {
        FilterGroup original = new FilterGroup("AND");
        original.setQueryModel(mockModel);
        original.addFilter(mockFilter1);
        
        FilterGroup nested = new FilterGroup("OR");
        nested.addFilter(mockFilter2);
        original.addGroup(nested);

        FilterGroup copy = original.copy();

        // 验证基本属性
        assertEquals(original.getOperator(), copy.getOperator());
        assertEquals(original.getFilters().size(), copy.getFilters().size());
        assertEquals(original.getGroups().size(), copy.getGroups().size());
        
        // 验证浅复制
        assertSame(original.getQueryModel(), copy.getQueryModel());
        
        // 验证深度复制
        assertNotSame(original.getFilters().get(0), copy.getFilters().get(0));
        assertNotSame(original.getGroups().get(0), copy.getGroups().get(0));
    }

    @Test
    void shouldValidateFilterGroup() {
        FilterGroup validGroup = new FilterGroup("AND");
        validGroup.addFilter(mockFilter1);
        assertTrue(validGroup.isValid());

        FilterGroup emptyGroup = new FilterGroup("AND");
        assertFalse(emptyGroup.isValid());

        FilterGroup invalidOperatorGroup = new FilterGroup("INVALID");
        invalidOperatorGroup.addFilter(mockFilter1);
        assertFalse(invalidOperatorGroup.isValid());

        // 测试无效的过滤条件
        Filter invalidFilter = Mockito.mock(Filter.class);
        Mockito.when(invalidFilter.isValid()).thenReturn(false);
        FilterGroup groupWithInvalidFilter = new FilterGroup("AND");
        groupWithInvalidFilter.addFilter(invalidFilter);
        assertFalse(groupWithInvalidFilter.isValid());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        FilterGroup group1 = new FilterGroup("AND");
        group1.addFilter(mockFilter1);
        
        FilterGroup group2 = new FilterGroup("AND");
        group2.addFilter(mockFilter1);
        
        FilterGroup group3 = new FilterGroup("OR");
        group3.addFilter(mockFilter1);

        assertEquals(group1, group2);
        assertNotEquals(group1, group3);
        assertEquals(group1.hashCode(), group2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        FilterGroup group = new FilterGroup("AND");
        group.addFilter(mockFilter1);
        group.addFilter(mockFilter2);
        
        String expected = "field1 = 'value1' AND field2 = 'value2'";
        assertEquals(expected, group.toString());
    }
}