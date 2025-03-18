import { mount } from '@vue/test-utils';
import UtilService from '../../../../insight-data-main/src/main/resources/static/js/services/util-service';

jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/util-service');

describe('ConditionBuilder', () => {
    let wrapper;
    const mockTables = [
        {
            id: 'table1',
            name: 'users',
            columns: [
                { name: 'id', type: 'INTEGER', primaryKey: true },
                { name: 'username', type: 'VARCHAR' },
                { name: 'age', type: 'INTEGER' },
                { name: 'created_at', type: 'TIMESTAMP' }
            ]
        },
        {
            id: 'table2',
            name: 'orders',
            columns: [
                { name: 'id', type: 'INTEGER', primaryKey: true },
                { name: 'user_id', type: 'INTEGER' },
                { name: 'total', type: 'DECIMAL' }
            ]
        }
    ];

    const defaultConditions = {
        logic: 'AND',
        conditions: []
    };

    beforeEach(() => {
        jest.clearAllMocks();
        
        UtilService.formatDateTime.mockImplementation(date => date);

        wrapper = mount(ConditionBuilder, {
            propsData: {
                value: defaultConditions,
                tables: mockTables
            },
            stubs: [
                'a-select',
                'a-input',
                'a-input-number',
                'a-date-picker',
                'a-switch',
                'a-radio-group',
                'a-radio-button',
                'a-button'
            ]
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    describe('Component initialization', () => {
        it('should initialize with empty conditions', () => {
            expect(wrapper.vm.value.conditions).toHaveLength(0);
            expect(wrapper.vm.logic).toBe('AND');
        });

        it('should compute available columns from tables', () => {
            expect(wrapper.vm.availableColumns).toHaveLength(7); // Total columns from both tables
            expect(wrapper.vm.availableColumns[0].id).toBe('table1.id');
        });
    });

    describe('Condition operations', () => {
        it('should add new condition', async () => {
            await wrapper.vm.addCondition();
            expect(wrapper.emitted().input[0][0].conditions).toHaveLength(1);
            
            const newCondition = wrapper.emitted().input[0][0].conditions[0];
            expect(newCondition).toEqual({
                tableId: '',
                column: '',
                operator: '=',
                value: null,
                valueType: 'string'
            });
        });

        it('should add condition group', async () => {
            await wrapper.vm.addGroup();
            expect(wrapper.emitted().input[0][0].conditions).toHaveLength(1);
            
            const newGroup = wrapper.emitted().input[0][0].conditions[0];
            expect(newGroup).toEqual({
                logic: 'AND',
                conditions: []
            });
        });

        it('should remove condition', async () => {
            // Add condition first
            await wrapper.vm.addCondition();
            expect(wrapper.emitted().input[0][0].conditions).toHaveLength(1);
            
            // Remove condition
            await wrapper.vm.removeItem(0);
            expect(wrapper.emitted().input[1][0].conditions).toHaveLength(0);
        });
    });

    describe('Condition updates', () => {
        it('should update condition when column changes', async () => {
            await wrapper.vm.addCondition();
            await wrapper.vm.updateCondition(0, {
                tableId: 'table1',
                column: 'age'
            });

            const updatedCondition = wrapper.emitted().input[1][0].conditions[0];
            expect(updatedCondition.valueType).toBe('number');
            expect(updatedCondition.value).toBe(0);
        });

        it('should update operator', async () => {
            await wrapper.vm.addCondition();
            await wrapper.vm.updateCondition(0, { operator: 'IN' });

            const updatedCondition = wrapper.emitted().input[1][0].conditions[0];
            expect(updatedCondition.operator).toBe('IN');
        });

        it('should handle different value types', () => {
            const testCases = [
                { type: 'INTEGER', expectedType: 'number', expectedDefault: 0 },
                { type: 'VARCHAR', expectedType: 'string', expectedDefault: '' },
                { type: 'TIMESTAMP', expectedType: 'date', expectedDefault: null },
                { type: 'BOOLEAN', expectedType: 'boolean', expectedDefault: false }
            ];

            testCases.forEach(({ type, expectedType, expectedDefault }) => {
                const columnType = wrapper.vm.getColumnType({ type });
                expect(columnType).toBe(expectedType);
                
                const defaultValue = wrapper.vm.getDefaultValue(expectedType);
                expect(defaultValue).toEqual(expectedDefault);
            });
        });
    });

    describe('Operator handling', () => {
        it('should filter operators by column type', () => {
            const numericColumn = { type: 'INTEGER' };
            const stringColumn = { type: 'VARCHAR' };
            const dateColumn = { type: 'TIMESTAMP' };

            const numericOps = wrapper.vm.getAvailableOperators(numericColumn);
            expect(numericOps.some(op => op.value === '>')).toBe(true);
            expect(numericOps.some(op => op.value === 'LIKE')).toBe(false);

            const stringOps = wrapper.vm.getAvailableOperators(stringColumn);
            expect(stringOps.some(op => op.value === 'LIKE')).toBe(true);
            expect(stringOps.some(op => op.value === '>')).toBe(false);

            const dateOps = wrapper.vm.getAvailableOperators(dateColumn);
            expect(dateOps.some(op => op.value === '>')).toBe(true);
            expect(dateOps.some(op => op.value === 'LIKE')).toBe(false);
        });

        it('should determine correct value editor type', () => {
            const tests = [
                { operator: '=', expected: 'single' },
                { operator: 'IN', expected: 'list' },
                { operator: 'BETWEEN', expected: 'range' },
                { operator: 'IS NULL', expected: 'none' }
            ];

            tests.forEach(({ operator, expected }) => {
                expect(wrapper.vm.getOperatorValueType(operator)).toBe(expected);
            });
        });
    });

    describe('Value formatting', () => {
        it('should format different value types correctly', () => {
            const tests = [
                {
                    condition: { value: '2025-03-17', valueType: 'date' },
                    expected: '2025-03-17'
                },
                {
                    condition: { value: true, valueType: 'boolean' },
                    expected: '是'
                },
                {
                    condition: { value: [1, 2, 3], valueType: 'list' },
                    expected: '1, 2, 3'
                },
                {
                    condition: { value: 123, valueType: 'number' },
                    expected: '123'
                }
            ];

            tests.forEach(({ condition, expected }) => {
                expect(wrapper.vm.formatValue(condition)).toBe(expected);
            });
        });
    });

    describe('Complex condition groups', () => {
        it('should handle nested condition groups', async () => {
            // Add outer group
            await wrapper.vm.addGroup();
            
            // Get outer group reference
            const outerGroup = wrapper.emitted().input[0][0].conditions[0];
            
            // Add condition to outer group
            await wrapper.vm.updateGroup(0, {
                ...outerGroup,
                conditions: [{
                    tableId: 'table1',
                    column: 'age',
                    operator: '>',
                    value: 18,
                    valueType: 'number'
                }]
            });

            // Add inner group
            await wrapper.vm.updateGroup(0, {
                ...wrapper.emitted().input[1][0].conditions[0],
                conditions: [
                    ...wrapper.emitted().input[1][0].conditions[0].conditions,
                    {
                        logic: 'OR',
                        conditions: []
                    }
                ]
            });

            const finalConfig = wrapper.emitted().input[2][0];
            expect(finalConfig.conditions[0].conditions).toHaveLength(2);
            expect(finalConfig.conditions[0].conditions[1].logic).toBe('OR');
        });
    });
});