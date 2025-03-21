package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JoinCondition 的单元测试类
 */
class JoinConditionTest {

    private TableReference leftTable;
    private TableReference rightTable;
    private QueryModelContract mockModel;

    @BeforeEach
    void setUp() {
        leftTable = new TableReference("users", "u");
        rightTable = new TableReference("orders", "o");
        mockModel = Mockito.mock(QueryModelContract.class);
    }

    @Test
    void shouldCreateEmptyJoinCondition() {
        JoinCondition join = new JoinCondition();
        
        assertNull(join.getLeftTable());
        assertNull(join.getRightTable());
        assertEquals("INNER", join.getJoinType());
        assertNull(join.getCondition());
        assertNull(join.getQueryModel());
    }

    @Test
    void shouldCreateJoinConditionWithBasicInfo() {
        JoinCondition join = new JoinCondition(leftTable, rightTable, "u.id = o.user_id");
        
        assertSame(leftTable, join.getLeftTable());
        assertSame(rightTable, join.getRightTable());
        assertEquals("INNER", join.getJoinType());
        assertEquals("u.id = o.user_id", join.getCondition());
        assertNull(join.getQueryModel());
    }

    @Test
    void shouldCreateJoinConditionWithFullInfo() {
        JoinCondition join = new JoinCondition(leftTable, rightTable, "LEFT", 
            "u.id = o.user_id", mockModel);
        
        assertSame(leftTable, join.getLeftTable());
        assertSame(rightTable, join.getRightTable());
        assertEquals("LEFT", join.getJoinType());
        assertEquals("u.id = o.user_id", join.getCondition());
        assertSame(mockModel, join.getQueryModel());
    }

    @Test
    void shouldHandleNullJoinType() {
        JoinCondition join = new JoinCondition(leftTable, rightTable, null, 
            "u.id = o.user_id", mockModel);
        
        assertEquals("INNER", join.getJoinType());
    }

    @Test
    void shouldGenerateJoinStatement() {
        JoinCondition join = new JoinCondition(leftTable, rightTable, "LEFT", 
            "u.id = o.user_id", mockModel);
        
        String expected = "LEFT JOIN orders AS o ON u.id = o.user_id";
        assertEquals(expected, join.getJoinStatement());
    }

    @Test
    void shouldCopyJoinCondition() {
        JoinCondition original = new JoinCondition(leftTable, rightTable, "LEFT", 
            "u.id = o.user_id", mockModel);
        original.setDescription("Test join");

        JoinCondition copy = original.copy();

        // 验证深度复制
        assertNotSame(original.getLeftTable(), copy.getLeftTable());
        assertNotSame(original.getRightTable(), copy.getRightTable());
        assertEquals(original.getLeftTable().getTableName(), copy.getLeftTable().getTableName());
        assertEquals(original.getRightTable().getTableName(), copy.getRightTable().getTableName());
        
        // 验证其他属性
        assertEquals(original.getJoinType(), copy.getJoinType());
        assertEquals(original.getCondition(), copy.getCondition());
        assertEquals(original.getDescription(), copy.getDescription());
        
        // 验证浅复制
        assertSame(original.getQueryModel(), copy.getQueryModel());
    }

    @Test
    void shouldValidateJoinCondition() {
        JoinCondition validJoin = new JoinCondition(leftTable, rightTable, 
            "u.id = o.user_id");
        assertTrue(validJoin.isValid());

        JoinCondition invalidJoin1 = new JoinCondition(null, rightTable, 
            "u.id = o.user_id");
        assertFalse(invalidJoin1.isValid());

        JoinCondition invalidJoin2 = new JoinCondition(leftTable, null, 
            "u.id = o.user_id");
        assertFalse(invalidJoin2.isValid());

        JoinCondition invalidJoin3 = new JoinCondition(leftTable, rightTable, 
            null);
        assertFalse(invalidJoin3.isValid());

        JoinCondition invalidJoin4 = new JoinCondition(leftTable, rightTable, 
            "");
        assertFalse(invalidJoin4.isValid());

        JoinCondition invalidJoin5 = new JoinCondition(leftTable, rightTable, 
            " ");
        assertFalse(invalidJoin5.isValid());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        JoinCondition join1 = new JoinCondition(leftTable, rightTable, "LEFT", 
            "u.id = o.user_id", mockModel);
        JoinCondition join2 = new JoinCondition(leftTable, rightTable, "LEFT", 
            "u.id = o.user_id", mockModel);
        JoinCondition join3 = new JoinCondition(leftTable, rightTable, "RIGHT", 
            "u.id = o.user_id", mockModel);

        assertEquals(join1, join2);
        assertNotEquals(join1, join3);
        assertEquals(join1.hashCode(), join2.hashCode());
    }

    @Test
    void shouldHandleNullValuesInEqualsAndHashCode() {
        JoinCondition join1 = new JoinCondition(null, null, null, null, null);
        JoinCondition join2 = new JoinCondition(null, null, null, null, null);
        JoinCondition join3 = new JoinCondition(leftTable, null, null, null, null);

        assertEquals(join1, join2);
        assertNotEquals(join1, join3);
        assertEquals(join1.hashCode(), join2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        JoinCondition join = new JoinCondition(leftTable, rightTable, "LEFT", 
            "u.id = o.user_id", mockModel);
        
        String expected = "LEFT JOIN orders AS o ON u.id = o.user_id";
        assertEquals(expected, join.toString());
    }
}