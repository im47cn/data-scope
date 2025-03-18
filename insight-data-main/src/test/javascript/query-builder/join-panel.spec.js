import { mount } from '@vue/test-utils';
import QueryService from '../../../../insight-data-main/src/main/resources/static/js/services/query-service';
import UtilService from '../../../../insight-data-main/src/main/resources/static/js/services/util-service';

jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/query-service');
jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/util-service');

describe('JoinPanel', () => {
    let wrapper;
    const mockTables = [
        {
            id: 'table1',
            name: 'users',
            columns: [
                { name: 'id', type: 'INTEGER' },
                { name: 'username', type: 'VARCHAR' }
            ]
        },
        {
            id: 'table2',
            name: 'orders',
            columns: [
                { name: 'id', type: 'INTEGER' },
                { name: 'user_id', type: 'INTEGER' },
                { name: 'total', type: 'DECIMAL' }
            ]
        }
    ];

    const mockJoins = [
        {
            id: 'join1',
            leftTableId: 'table1',
            leftColumn: 'id',
            rightTableId: 'table2',
            rightColumn: 'user_id',
            type: 'INNER'
        }
    ];

    beforeEach(() => {
        jest.clearAllMocks();
        
        UtilService.generateUniqueId.mockImplementation(() => 'mock-id');
        
        QueryService.inferTableRelationship.mockResolvedValue({
            data: {
                relationships: [
                    {
                        leftColumn: 'id',
                        rightColumn: 'user_id',
                        confidence: 0.9
                    }
                ]
            }
        });

        wrapper = mount(JoinPanel, {
            propsData: {
                value: [...mockJoins],
                tables: mockTables
            },
            stubs: [
                'a-list',
                'a-list-item',
                'a-button',
                'a-modal',
                'a-form',
                'a-form-item',
                'a-select',
                'a-radio-group',
                'a-radio-button',
                'a-tag',
                'a-icon'
            ],
            mocks: {
                $message: {
                    error: jest.fn(),
                    success: jest.fn()
                },
                $notification: {
                    info: jest.fn()
                },
                $confirm: jest.fn(({ onOk }) => onOk())
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    describe('Component initialization', () => {
        it('should initialize with provided joins', () => {
            expect(wrapper.vm.value).toEqual(mockJoins);
        });

        it('should compute available columns correctly', () => {
            expect(wrapper.vm.availableColumns).toHaveLength(5);
            expect(wrapper.vm.availableColumns[0].id).toBe('table1.id');
        });
    });

    describe('Join operations', () => {
        it('should add new join', async () => {
            await wrapper.vm.addJoin();
            
            expect(wrapper.emitted().input[0][0]).toHaveLength(2);
            expect(wrapper.emitted().input[0][0][1]).toEqual({
                id: 'mock-id',
                leftTableId: '',
                leftColumn: '',
                rightTableId: '',
                rightColumn: '',
                type: 'INNER'
            });
        });

        it('should remove join with confirmation', async () => {
            await wrapper.vm.removeJoin('join1');
            
            expect(wrapper.emitted().input[0][0]).toHaveLength(0);
        });

        it('should edit join', async () => {
            await wrapper.vm.editJoin(mockJoins[0]);
            expect(wrapper.vm.editingJoin).toEqual(mockJoins[0]);
        });

        it('should save valid join', async () => {
            const validJoin = {
                id: 'join2',
                leftTableId: 'table1',
                leftColumn: 'id',
                rightTableId: 'table2',
                rightColumn: 'user_id',
                type: 'INNER'
            };

            wrapper.vm.editingJoin = validJoin;
            await wrapper.vm.saveJoin();
            
            expect(wrapper.emitted().input).toBeTruthy();
            expect(wrapper.vm.editingJoin).toBeNull();
        });

        it('should not save invalid join', async () => {
            const invalidJoin = {
                id: 'join2',
                leftTableId: '',
                leftColumn: '',
                rightTableId: '',
                rightColumn: '',
                type: 'INNER'
            };

            wrapper.vm.editingJoin = invalidJoin;
            await wrapper.vm.saveJoin();
            
            expect(wrapper.emitted().input).toBeFalsy();
            expect(wrapper.vm.$message.error).toHaveBeenCalled();
        });
    });

    describe('Table relationship inference', () => {
        it('should load suggestions when tables selected', async () => {
            wrapper.vm.editingJoin = {
                id: 'new-join',
                leftTableId: 'table1',
                leftColumn: '',
                rightTableId: 'table2',
                rightColumn: '',
                type: 'INNER'
            };

            await wrapper.vm.loadSuggestions();
            
            expect(QueryService.inferTableRelationship).toHaveBeenCalled();
            expect(wrapper.vm.suggestions).toHaveLength(1);
            expect(wrapper.vm.editingJoin.leftColumn).toBe('id');
            expect(wrapper.vm.editingJoin.rightColumn).toBe('user_id');
        });

        it('should handle table change and reload suggestions', async () => {
            wrapper.vm.editingJoin = {
                id: 'new-join',
                leftTableId: '',
                leftColumn: '',
                rightTableId: '',
                rightColumn: '',
                type: 'INNER'
            };

            await wrapper.vm.handleLeftTableChange('table1');
            
            expect(wrapper.vm.editingJoin.leftTableId).toBe('table1');
            expect(wrapper.vm.editingJoin.leftColumn).toBe('');
            expect(QueryService.inferTableRelationship).toHaveBeenCalled();
        });
    });

    describe('Validation', () => {
        it('should validate join configuration', () => {
            const validJoin = {
                leftTableId: 'table1',
                leftColumn: 'id',
                rightTableId: 'table2',
                rightColumn: 'user_id'
            };
            expect(wrapper.vm.validateJoin(validJoin)).toBe(true);

            const invalidJoin = {
                leftTableId: '',
                leftColumn: '',
                rightTableId: '',
                rightColumn: ''
            };
            expect(wrapper.vm.validateJoin(invalidJoin)).toBe(false);
        });

        it('should prevent self-joins', () => {
            const selfJoin = {
                leftTableId: 'table1',
                leftColumn: 'id',
                rightTableId: 'table1',
                rightColumn: 'id'
            };
            expect(wrapper.vm.validateJoin(selfJoin)).toBe(false);
            expect(wrapper.vm.$message.error).toHaveBeenCalled();
        });
    });

    describe('UI interactions', () => {
        it('should show join type icons correctly', () => {
            expect(wrapper.vm.getJoinTypeIcon('INNER')).toBe('connection');
            expect(wrapper.vm.getJoinTypeIcon('LEFT')).toBe('align-left');
            expect(wrapper.vm.getJoinTypeIcon('RIGHT')).toBe('align-right');
            expect(wrapper.vm.getJoinTypeIcon('FULL')).toBe('block');
            expect(wrapper.vm.getJoinTypeIcon('INVALID')).toBe('question');
        });

        it('should get correct columns for table', () => {
            const columns = wrapper.vm.getTableColumns('table1');
            expect(columns).toHaveLength(2);
            expect(columns[0].name).toBe('id');
        });
    });
});