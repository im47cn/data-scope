import { mount } from '@vue/test-utils';
import DataSourceService from '../../../main/resources/static/js/services/datasource-service';
import QueryService from '../../../main/resources/static/js/services/query-service';

jest.mock('../../../main/resources/static/js/services/datasource-service');
jest.mock('../../../main/resources/static/js/services/query-service');

describe('QueryBuilderPage', () => {
    let wrapper;
    const mockDataSource = {
        id: 'ds-1',
        name: 'Test Database'
    };
    const mockRoute = {
        params: {
            dataSourceId: 'ds-1'
        }
    };

    beforeEach(() => {
        jest.clearAllMocks();
        localStorage.clear();
        
        // Mock service responses
        DataSourceService.getDataSource.mockResolvedValue({
            data: mockDataSource
        });

        QueryService.executeQuery.mockResolvedValue({
            data: { queryId: 'query-1' }
        });

        wrapper = mount(QueryBuilderPage, {
            mocks: {
                $route: mockRoute,
                $message: {
                    success: jest.fn(),
                    error: jest.fn(),
                    warning: jest.fn()
                }
            },
            stubs: [
                'query-workspace',
                'query-history',
                'saved-queries',
                'parameter-input',
                'sql-preview',
                'results-preview',
                'query-plan',
                'a-tabs',
                'a-tab-pane',
                'a-collapse',
                'a-collapse-panel',
                'a-space',
                'a-button',
                'a-icon',
                'a-input-number',
                'a-tooltip'
            ]
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    describe('Initialization', () => {
        it('should load data source on mount', async () => {
            expect(DataSourceService.getDataSource).toHaveBeenCalledWith('ds-1');
            await wrapper.vm.$nextTick();
            expect(wrapper.vm.dataSource).toEqual(mockDataSource);
        });

        it('should restore last query from local storage', async () => {
            const lastQuery = {
                sql: 'SELECT * FROM users',
                parameters: { id: 1 },
                timestamp: Date.now()
            };
            localStorage.setItem('queryHistory', JSON.stringify([lastQuery]));
            
            wrapper = mount(QueryBuilderPage, {
                mocks: { $route: mockRoute }
            });

            await wrapper.vm.$nextTick();
            expect(wrapper.vm.queryConfig.sql).toBe(lastQuery.sql);
            expect(wrapper.vm.queryConfig.parameters).toEqual(lastQuery.parameters);
        });
    });

    describe('Query execution', () => {
        it('should execute query successfully', async () => {
            wrapper.setData({
                queryConfig: {
                    sql: 'SELECT * FROM users',
                    parameters: {},
                    pageSize: 100,
                    timeout: 30000
                }
            });

            await wrapper.vm.executeQuery();
            
            expect(QueryService.executeQuery).toHaveBeenCalledWith({
                dataSourceId: 'ds-1',
                sql: 'SELECT * FROM users',
                parameters: {},
                pageSize: 100,
                timeout: 30000
            });

            expect(wrapper.vm.queryId).toBe('query-1');
            expect(wrapper.vm.panels.planView).toBe(true);
        });

        it('should handle query errors', async () => {
            const error = new Error('Query failed');
            QueryService.executeQuery.mockRejectedValueOnce(error);

            await wrapper.vm.executeQuery();
            
            expect(wrapper.vm.executionStatus.error).toBeTruthy();
            expect(wrapper.vm.$message.error).toHaveBeenCalled();
        });

        it('should track execution time', async () => {
            jest.useFakeTimers();
            
            wrapper.setData({
                queryConfig: { sql: 'SELECT 1' }
            });

            const executionPromise = wrapper.vm.executeQuery();
            jest.advanceTimersByTime(1000);
            await executionPromise;

            expect(wrapper.vm.executionTime).toBeCloseTo(1, 1);
            
            jest.useRealTimers();
        });

        it('should cancel running query', async () => {
            wrapper.setData({
                queryId: 'query-1',
                executionStatus: { running: true }
            });

            await wrapper.vm.cancelQuery();
            
            expect(QueryService.cancelQuery).toHaveBeenCalledWith('query-1');
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('查询已取消');
        });
    });

    describe('Parameter handling', () => {
        it('should detect parameters in SQL', async () => {
            await wrapper.setData({
                queryConfig: {
                    sql: 'SELECT * FROM users WHERE id = :userId'
                }
            });

            expect(wrapper.vm.panels.parameters).toBe(true);
        });

        it('should validate parameters before execution', async () => {
            wrapper.setData({
                panels: { parameters: true },
                queryConfig: {
                    sql: 'SELECT * FROM users WHERE id = :userId'
                }
            });

            // Mock parameter input component as invalid
            wrapper.vm.$refs.parameterInput = {
                isComplete: false
            };

            expect(wrapper.vm.canExecute).toBe(false);
        });
    });

    describe('History management', () => {
        it('should save query to local history', async () => {
            const query = {
                sql: 'SELECT * FROM users',
                parameters: { id: 1 }
            };

            wrapper.setData({ queryConfig: query });
            await wrapper.vm.executeQuery();

            const history = JSON.parse(localStorage.getItem('queryHistory'));
            expect(history[0].sql).toBe(query.sql);
            expect(history[0].parameters).toEqual(query.parameters);
        });

        it('should limit history size', async () => {
            const queries = Array(60).fill().map((_, i) => ({
                sql: `SELECT ${i}`,
                timestamp: Date.now() - i
            }));

            localStorage.setItem('queryHistory', JSON.stringify(queries));
            
            wrapper.setData({
                queryConfig: { sql: 'NEW QUERY' }
            });
            await wrapper.vm.executeQuery();

            const history = JSON.parse(localStorage.getItem('queryHistory'));
            expect(history.length).toBe(50);
            expect(history[0].sql).toBe('NEW QUERY');
        });
    });

    describe('UI interactions', () => {
        it('should handle panel resizing', () => {
            const startHeight = 300;
            wrapper.setData({
                layout: {
                    previewHeight: startHeight
                }
            });

            wrapper.vm.startResizing({ clientY: 0 });
            wrapper.vm.handleResizing({ clientY: -50 });
            
            expect(wrapper.vm.layout.previewHeight).toBe(350);
        });

        it('should prevent preview height from becoming too small', () => {
            wrapper.setData({
                layout: {
                    previewHeight: 300
                }
            });

            wrapper.vm.startResizing({ clientY: 0 });
            wrapper.vm.handleResizing({ clientY: 200 });
            
            expect(wrapper.vm.layout.previewHeight).toBe(200);
        });

        it('should clean up resize listeners', () => {
            const removeEventListenerSpy = jest.spyOn(document, 'removeEventListener');
            
            wrapper.vm.startResizing({ clientY: 0 });
            wrapper.vm.stopResizing();
            
            expect(removeEventListenerSpy).toHaveBeenCalledTimes(2);
            expect(wrapper.vm.layout.resizing).toBe(false);
        });
    });
});