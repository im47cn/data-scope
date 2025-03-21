package com.insightdata.domain.querybuilder.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.insightdata.domain.querybuilder.api.QueryModelContract;

/**
 * FieldSelection 的单元测试类
 */
class FieldSelectionTest {

    private FieldReference mockField;
    private QueryModelContract mockModel;
    private TableReference mockTable;

    @BeforeEach
    void setUp() {
        mockTable = new TableReference("users", "u");
        mockField = new FieldReference(mockTable, "name");
        mockModel = Mockito.mock(QueryModelContract.class);
    }

    @Test
    void shouldCreateEmptyFieldSelection() {
        FieldSelection selection = new FieldSelection();
        
        assertNull(selection.getField());
        assertNull(selection.getAlias());
        assertNull(selection.getAggregateFunction());
        assertNull(selection.getQueryModel());
        assertFalse(selection.isCalculated());
    }

    @Test
    void shouldCreateFieldSelectionWithField() {
        FieldSelection selection = new FieldSelection(mockField);
        
        assertSame(mockField, selection.getField());
        assertNull(selection.getAlias());
        assertNull(selection.getAggregateFunction());
    }

    @Test
    void shouldCreateFieldSelectionWithFieldAndAlias() {
        FieldSelection selection = new FieldSelection(mockField, "user_name");
        
        assertSame(mockField, selection.getField());
        assertEquals("user_name", selection.getAlias());
        assertNull(selection.getAggregateFunction());
    }

    @Test
    void shouldCreateFieldSelectionWithFullInfo() {
        FieldSelection selection = new FieldSelection(mockField, "user_name", 
            "COUNT", mockModel);
        
        assertSame(mockField, selection.getField());
        assertEquals("user_name", selection.getAlias());
        assertEquals("COUNT", selection.getAggregateFunction());
        assertSame(mockModel, selection.getQueryModel());
    }

    @Test
    void shouldGenerateSimpleExpression() {
        FieldSelection selection = new FieldSelection(mockField);
        assertEquals("u.name", selection.getExpression());
    }

    @Test
    void shouldGenerateExpressionWithAlias() {
        FieldSelection selection = new FieldSelection(mockField, "user_name");
        assertEquals("u.name AS user_name", selection.getExpression());
    }

    @Test
    void shouldGenerateExpressionWithAggregate() {
        FieldSelection selection = new FieldSelection(mockField, "total_users", 
            "COUNT", mockModel);
        assertEquals("COUNT(u.name) AS total_users", selection.getExpression());
    }

    @Test
    void shouldGenerateCalculatedExpression() {
        FieldSelection selection = new FieldSelection();
        selection.setCalculated(true);
        selection.setExpression("CONCAT(first_name, ' ', last_name)");
        selection.setAlias("full_name");
        
        assertEquals("CONCAT(first_name, ' ', last_name) AS full_name", 
            selection.getExpression());
    }

    @Test
    void shouldCopyFieldSelection() {
        FieldSelection original = new FieldSelection(mockField, "user_name", 
            "COUNT", mockModel);
        original.setDescription("Test selection");
        original.setCalculated(true);
        original.setExpression("test_expression");

        FieldSelection copy = original.copy();

        // 验证深度复制
        assertNotSame(original.getField(), copy.getField());
        assertEquals(original.getField().getFieldName(), copy.getField().getFieldName());
        
        // 验证其他属性
        assertEquals(original.getAlias(), copy.getAlias());
        assertEquals(original.getAggregateFunction(), copy.getAggregateFunction());
        assertEquals(original.getDescription(), copy.getDescription());
        assertEquals(original.isCalculated(), copy.isCalculated());
        assertEquals(original.getExpression(), copy.getExpression());
        
        // 验证浅复制
        assertSame(original.getQueryModel(), copy.getQueryModel());
    }

    @Test
    void shouldValidateFieldSelection() {
        FieldSelection validSelection = new FieldSelection(mockField);
        assertTrue(validSelection.isValid());

        FieldSelection invalidSelection = new FieldSelection();
        assertFalse(invalidSelection.isValid());

        FieldSelection validCalculated = new FieldSelection();
        validCalculated.setCalculated(true);
        validCalculated.setExpression("CONCAT(a, b)");
        assertTrue(validCalculated.isValid());

        FieldSelection invalidCalculated = new FieldSelection();
        invalidCalculated.setCalculated(true);
        invalidCalculated.setExpression("");
        assertFalse(invalidCalculated.isValid());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        FieldSelection selection1 = new FieldSelection(mockField, "user_name", 
            "COUNT", mockModel);
        FieldSelection selection2 = new FieldSelection(mockField, "user_name", 
            "COUNT", mockModel);
        FieldSelection selection3 = new FieldSelection(mockField, "other_name", 
            "COUNT", mockModel);

        assertEquals(selection1, selection2);
        assertNotEquals(selection1, selection3);
        assertEquals(selection1.hashCode(), selection2.hashCode());
    }

    @Test
    void shouldHandleNullValuesInEqualsAndHashCode() {
        FieldSelection selection1 = new FieldSelection(null, null, null, null);
        FieldSelection selection2 = new FieldSelection(null, null, null, null);
        FieldSelection selection3 = new FieldSelection(mockField, null, null, null);

        assertEquals(selection1, selection2);
        assertNotEquals(selection1, selection3);
        assertEquals(selection1.hashCode(), selection2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        FieldSelection selection = new FieldSelection(mockField, "user_name", 
            "COUNT", mockModel);
        assertEquals("COUNT(u.name) AS user_name", selection.toString());
    }
}