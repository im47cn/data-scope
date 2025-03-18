import { mount } from '@vue/test-utils';
import DataSourceService from '../../../../insight-data-main/src/main/resources/static/js/services/datasource-service';
import QueryService from '../../../../insight-data-main/src/main/resources/static/js/services/query-service';
import UtilService from '../../../../insight-data-main/src/main/resources/static/js/services/util-service';

jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/datasource-service');
jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/query-service');
jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/util-service');

describe('QueryWorkspace', () => {
    let wrapper;
    const mockSchemas = [
        { name: 'public' },
        { name: 'test' }
    ];
    
    const mockTables = [
        { 
            name: 'users',
            columns: [
                { name: 'id', type: 'INTEGER', primaryKey: true },
                { name: 'username', type: 'VARCHAR' }
            ]
        },
        {
            name: 'orders',
            columns: [
                { name: 'id', type: 'INTEGER', primaryKey: true },
                { name: 'user_id', type: 'INTEGER' },
                { name: 'total', type: 'DECIMAL' }
            ]
        }
    ];

    const mockRelationships = [
        {
            leftColumn: 'id',
            rightColumn: 'user_id',
            confidence: 0.9
        }
    ];

    beforeEach(() => {
        jest.clearAllMocks();
        
        // Setup service mocks
        DataSourceService.getSchemas.mockResolvedValue({
            data: mockSchemas
        });
        
        DataSourceService.getTables.mockResolvedValue({
            data: mockTables
        });
        
        DataSourceService.getTableDetails.mockImplementation((_, __, tableName) => {
            const table = mockTables.find(t => t.name === tableName);
            return Promise.resolve({ data: table });
        });
        
        QueryService.inferTableRelationship.mockResolvedValue({
            data: { relationships: mockRelationships }
        });
        
        QueryService.generateSql.mockResolvedValue({
            data: {
                sql: 'SELECT * FROM users',
                valid: true,
                errors: [],
                warnings: []
            }
        });

        UtilService.generateUniqueId.mockImplementation(() => Math.random().toString(36).substr(2, 9));
        UtilService.debounce.mockImplementation(fn => fn);

        // Mount component
        wrapper = mount(QueryWorkspace, {
            propsData: {
                dataSourceId: '1'
            },
            stubs: [
                'a-select',
                'a-input-search',
                'a-list',
                'a-list-item',
                'a-icon',
                'a-button',
                'a-checkbox',
                'a-spin'
            ],
            mocks: {
                $message: {
                    success: jest.fn(),
                    error: jest.fn(),
                    warning: jest.fn()
                },
                $notification: {
                    info: jest.fn(),
                    error: jest.fn()
                }
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    describe('Initialization', () => {
        it('should load schemas on mount', async () => {
            await wrapper.vm.$nextTick();
            expect(DataSourceService.getSchemas).toHaveBeenCalledWith('1');
            expect(wrapper.vm.schemas).toEqual(mockSchemas);
            expect(wrapper.vm.selectedSchema).toBe('public');
        });

        it('should load tables when schema is selected', async () => {
            await wrapper.vm.loadTables('public');
            expect(DataSourceService.getTables).toHaveBeenCalledWith('1', 'public');
            expect(wrapper.vm.tables).toEqual(mockTables);
        });

        it('should load saved query if provided', async () => {
            const savedQuery = {
                tables: [
                    {
                        id: '1',
                        name: 'users',
                        columns: []
                    }
                ],
                joins: [],
                conditions: { logic: 'AND', conditions: [] }
            };

            wrapper = mount(QueryWorkspace, {
                propsData: {
                    dataSourceId: '1',
                    initialQuery: savedQuery
                }
            });

            await wrapper.vm.$nextTick();
            expect(wrapper.vm.queryConfig).toEqual(savedQuery);
        });
    });

    describe('Table operations', () => {
        it('should add table to query', async () => {
            const table = mockTables[0];
            await wrapper.vm.addTable(table);

            expect(DataSourceService.getTableDetails).toHaveBeenCalledWith('1', 'public', 'users');
            expect(wrapper.vm.queryConfig.tables).toHaveLength(1);
            expect(wrapper.vm.queryConfig.tables[0].name).toBe('users');
        });

        it('should prevent adding duplicate tables', async () => {
            const table = mockTables[0];
            await wrapper.vm.addTable(table);
            await wrapper.vm.addTable(table);

            expect(wrapper.vm.queryConfig.tables).toHaveLength(1);
            expect(wrapper.vm.$message.warning).toHaveBeenCalled();
        });

        it('should remove table and related items', async () => {
            // Add two tables first
            await wrapper.vm.addTable(mockTables[0]);
            await wrapper.vm.addTable(mockTables[1]);
            
            const firstTableId = wrapper.vm.queryConfig.tables[0].id;
            
            // Add a join between them
            wrapper.vm.queryConfig.joins.push({
                id: '1',
                leftTableId: firstTableId,
                rightTableId: wrapper.vm.queryConfig.tables[1].id,
                leftColumn: 'id',
                rightColumn: 'user_id'
            });

            // Remove first table
            wrapper.vm.removeTable(firstTableId);

            expect(wrapper.vm.queryConfig.tables).toHaveLength(1);
            expect(wrapper.vm.queryConfig.joins).toHaveLength(0);
        });
    });

    describe('Relationship inference', () => {
        it('should infer relationships when adding second table', async () => {
            // Add first table
            await wrapper.vm.addTable(mockTables[0]);
            
            // Add second table
            await wrapper.vm.addTable(mockTables[1]);

            expect(QueryService.inferTableRelationship).toHaveBeenCalled();
            expect(wrapper.vm.queryConfig.joins).toHaveLength(1);
            expect(wrapper.vm.$notification.info).toHaveBeenCalled();
        });

        it('should not infer relationships for first table', async () => {
            await wrapper.vm.addTable(mockTables[0]);
            expect(QueryService.inferTableRelationship).not.toHaveBeenCalled();
            expect(wrapper.vm.queryConfig.joins).toHaveLength(0);
        });
    });

    describe('SQL generation', () => {
        it('should generate SQL when query config changes', async () => {
            await wrapper.vm.addTable(mockTables[0]);
            
            expect(QueryService.generateSql).toHaveBeenCalled();
            expect(wrapper.vm.preview.sql).toBe('SELECT * FROM users');
            expect(wrapper.vm.preview.valid).toBe(true);
        });

        it('should handle SQL generation errors', async () => {
            QueryService.generateSql.mockRejectedValueOnce(new Error('SQL generation failed'));
            
            await wrapper.vm.addTable(mockTables[0]);
            
            expect(wrapper.vm.preview.valid).toBe(false);
            expect(wrapper.vm.preview.errors).toHaveLength(1);
        });
    });

    describe('Workspace interactions', () => {
        it('should update workspace layout when adding/removing tables', async () => {
            const updateLayoutSpy = jest.spyOn(wrapper.vm, 'updateWorkspaceLayout');
            
            await wrapper.vm.addTable(mockTables[0]);
            expect(updateLayoutSpy).toHaveBeenCalled();
            
            wrapper.vm.removeTable(wrapper.vm.queryConfig.tables[0].id);
            expect(updateLayoutSpy).toHaveBeenCalledTimes(2);
        });

        it('should handle workspace resizing', () => {
            const handleResizeSpy = jest.spyOn(wrapper.vm, 'handleResize');
            
            // Trigger resize event
            global.dispatchEvent(new Event('resize'));
            
            expect(handleResizeSpy).toHaveBeenCalled();
        });
    });

    describe('Query config management', () => {
        it('should return complete query config', async () => {
            await wrapper.vm.addTable(mockTables[0]);
            
            const config = wrapper.vm.getQueryConfig();
            
            expect(config).toEqual({
                dataSourceId: '1',
                schema: 'public',
                tables: wrapper.vm.queryConfig.tables,
                joins: [],
                conditions: { logic: 'AND', conditions: [] },
                groupBy: [],
                orderBy: [],
                limit: 100
            });
        });
    });
});