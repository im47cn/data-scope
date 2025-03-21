package com.insightdata.domain.querybuilder.model;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JoinDefinition 的单元测试类
 */
class JoinDefinitionTest {

    private TableReference primaryTable;
    private TableReference secondaryTable;
    private JoinCondition joinCondition;
    private QueryModelContract mockModel;

    @BeforeEach
    void setUp() {
        primaryTable = new TableReference("users", "u");
        secondaryTable = new TableReference("orders", "o");
        joinCondition = new JoinCondition(primaryTable, secondaryTable, "u.id = o.user_id");
        mockModel = Mockito.mock(QueryModelContract.class);
    }

    @Test
    void shouldCreateEmptyJoinDefinition() {
        JoinDefinition def = new JoinDefinition();
        
        assertNull(def.getPrimaryTable());
        assertNotNull(def.getJoins());
        assertTrue(def.getJoins().isEmpty());
        assertNull(def.getQueryModel());
        assertFalse(def.isForceInnerJoin());
    }

    @Test
    void shouldCreateJoinDefinitionWithPrimaryTable() {
        JoinDefinition def = new JoinDefinition(primaryTable);
        
        assertSame(primaryTable, def.getPrimaryTable());
        assertTrue(def.getJoins().isEmpty());
        assertFalse(def.isForceInnerJoin());
    }

    @Test
    void shouldCreateJoinDefinitionWithFullInfo() {
        JoinDefinition def = new JoinDefinition(primaryTable, mockModel, true);
        
        assertSame(primaryTable, def.getPrimaryTable());
        assertSame(mockModel, def.getQueryModel());
        assertTrue(def.isForceInnerJoin());
        assertTrue(def.getJoins().isEmpty());
    }

    @Test
    void shouldAddValidJoinCondition() {
        JoinDefinition def = new JoinDefinition(primaryTable);
        def.addJoin(joinCondition);
        
        assertEquals(1, def.getJoins().size());
        assertSame(joinCondition, def.getJoins().get(0));
    }

    @Test
    void shouldIgnoreInvalidJoinCondition() {
        JoinDefinition def = new JoinDefinition(primaryTable);
        def.addJoin(null);
        def.addJoin(new JoinCondition()); // 无效的连接条件
        
        assertTrue(def.getJoins().isEmpty());
    }

    @Test
    void shouldForceInnerJoin() {
        JoinDefinition def = new JoinDefinition(primaryTable, mockModel, true);
        JoinCondition leftJoin = new JoinCondition(primaryTable, secondaryTable, 
            "LEFT", "u.id = o.user_id", mockModel);
        
        def.addJoin(leftJoin);
        
        assertEquals("INNER", def.getJoins().get(0).getJoinType());
    }

    @Test
    void shouldGenerateJoinStatement() {
        JoinDefinition def = new JoinDefinition(primaryTable);
        def.addJoin(joinCondition);
        
        String expected = "users AS u\nINNER JOIN orders AS o ON u.id = o.user_id";
        assertEquals(expected, def.getJoinStatement());
    }

    @Test
    void shouldHandleInvalidJoinDefinition() {
        JoinDefinition def = new JoinDefinition();
        assertEquals("", def.getJoinStatement());

        def.setPrimaryTable(new TableReference()); // 无效的主表
        assertEquals("", def.getJoinStatement());
    }

    @Test
    void shouldGetAllTables() {
        JoinDefinition def = new JoinDefinition(primaryTable);
        def.addJoin(joinCondition);
        
        assertEquals(2, def.getAllTables().size());
        assertTrue(def.getAllTables().contains(primaryTable));
        assertTrue(def.getAllTables().contains(secondaryTable));
    }

    @Test
    void shouldCopyJoinDefinition() {
        JoinDefinition original = new JoinDefinition(primaryTable, mockModel, true);
        original.addJoin(joinCondition);
        original.setDescription("Test join definition");

        JoinDefinition copy = original.copy();

        // 验证深度复制
        assertNotSame(original.getPrimaryTable(), copy.getPrimaryTable());
        assertEquals(original.getPrimaryTable().getTableName(), 
            copy.getPrimaryTable().getTableName());
        
        assertNotSame(original.getJoins().get(0), copy.getJoins().get(0));
        assertEquals(original.getJoins().get(0).getCondition(), 
            copy.getJoins().get(0).getCondition());
        
        // 验证其他属性
        assertEquals(original.isForceInnerJoin(), copy.isForceInnerJoin());
        assertEquals(original.getDescription(), copy.getDescription());
        
        // 验证浅复制
        assertSame(original.getQueryModel(), copy.getQueryModel());
    }

    @Test
    void shouldValidateJoinDefinition() {
        JoinDefinition validDef = new JoinDefinition(primaryTable);
        assertTrue(validDef.isValid());

        JoinDefinition invalidDef1 = new JoinDefinition(null);
        assertFalse(invalidDef1.isValid());

        JoinDefinition invalidDef2 = new JoinDefinition(new TableReference());
        assertFalse(invalidDef2.isValid());
    }

    @Test
    void shouldDetectCircularJoins() {
        JoinDefinition def = new JoinDefinition(primaryTable);
        def.addJoin(joinCondition);
        assertFalse(def.hasCircularJoins());

        // 创建循环连接
        TableReference thirdTable = new TableReference("users", "u2");
        JoinCondition circularJoin = new JoinCondition(secondaryTable, thirdTable, 
            "o.id = u2.order_id");
        def.addJoin(circularJoin);
        
        assertTrue(def.hasCircularJoins());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        JoinDefinition def1 = new JoinDefinition(primaryTable, mockModel, true);
        def1.addJoin(joinCondition);
        
        JoinDefinition def2 = new JoinDefinition(primaryTable, mockModel, true);
        def2.addJoin(joinCondition);
        
        JoinDefinition def3 = new JoinDefinition(primaryTable, mockModel, false);
        def3.addJoin(joinCondition);

        assertEquals(def1, def2);
        assertNotEquals(def1, def3);
        assertEquals(def1.hashCode(), def2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        JoinDefinition def = new JoinDefinition(primaryTable);
        def.addJoin(joinCondition);
        
        String expected = "users AS u\nINNER JOIN orders AS o ON u.id = o.user_id";
        assertEquals(expected, def.toString());
    }
}