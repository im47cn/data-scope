import { mount } from '@vue/test-utils';
import DataSourceService from '../../../../insight-data-main/src/main/resources/static/js/services/datasource-service';
import UtilService from '../../../../insight-data-main/src/main/resources/static/js/services/util-service';

// Mock the services
jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/datasource-service');
jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/util-service');

describe('DataSourceList', () => {
    let wrapper;
    const mockDataSources = [
        {
            id: '1',
            name: 'Test DB',
            type: 'MYSQL',
            host: 'localhost',
            port: 3306,
            databaseName: 'testdb',
            active: true,
            syncStatus: { status: 'COMPLETED' },
            updatedAt: '2025-03-17T12:00:00Z'
        }
    ];

    beforeEach(() => {
        // Reset all mocks
        jest.clearAllMocks();

        // Setup service mocks
        DataSourceService.getDataSources.mockResolvedValue({
            data: {
                content: mockDataSources,
                pageable: {
                    totalElements: 1
                }
            }
        });
        DataSourceService.getSupportedTypes.mockResolvedValue({
            data: ['MYSQL', 'POSTGRESQL']
        });

        // Mount component
        wrapper = mount(DataSourceList, {
            stubs: ['a-table', 'a-input-search', 'a-select', 'a-button', 'a-icon', 'sync-status-badge'],
            mocks: {
                $router: {
                    push: jest.fn()
                },
                $message: {
                    success: jest.fn(),
                    error: jest.fn(),
                    warning: jest.fn()
                }
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    describe('Initial load', () => {
        it('should load data sources on mount', async () => {
            await wrapper.vm.$nextTick();
            expect(DataSourceService.getDataSources).toHaveBeenCalled();
            expect(DataSourceService.getSupportedTypes).toHaveBeenCalled();
            expect(wrapper.vm.dataSources).toEqual(mockDataSources);
        });

        it('should handle load error gracefully', async () => {
            const error = new Error('Failed to load');
            DataSourceService.getDataSources.mockRejectedValueOnce(error);
            
            await wrapper.vm.$nextTick();
            expect(wrapper.vm.$message.error).toHaveBeenCalledWith('获取数据源列表失败');
        });
    });

    describe('Search and filter', () => {
        it('should update search query and fetch data', async () => {
            const searchQuery = 'test';
            wrapper.setData({ searchQuery });
            await wrapper.vm.handleSearch();
            
            expect(DataSourceService.getDataSources).toHaveBeenCalledWith(
                expect.objectContaining({
                    search: searchQuery
                })
            );
        });

        it('should update type filter and fetch data', async () => {
            const typeFilter = 'MYSQL';
            wrapper.setData({ typeFilter });
            await wrapper.vm.handleSearch();
            
            expect(DataSourceService.getDataSources).toHaveBeenCalledWith(
                expect.objectContaining({
                    type: typeFilter
                })
            );
        });
    });

    describe('CRUD operations', () => {
        it('should navigate to create form when clicking add button', () => {
            wrapper.vm.handleAdd();
            expect(wrapper.vm.$router.push).toHaveBeenCalledWith('/datasource/add');
        });

        it('should navigate to edit form with correct id', () => {
            const dataSource = { id: '1' };
            wrapper.vm.handleEdit(dataSource);
            expect(wrapper.vm.$router.push).toHaveBeenCalledWith('/datasource/edit/1');
        });

        it('should delete data source with confirmation', async () => {
            DataSourceService.deleteDataSource.mockResolvedValueOnce({});
            const dataSource = { id: '1', name: 'Test DB' };
            
            // Simulate confirmation
            wrapper.vm.$confirm = ({ onOk }) => onOk();
            
            await wrapper.vm.handleDelete(dataSource);
            
            expect(DataSourceService.deleteDataSource).toHaveBeenCalledWith('1');
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('删除成功');
            expect(DataSourceService.getDataSources).toHaveBeenCalled();
        });

        it('should handle delete error gracefully', async () => {
            const error = new Error('Delete failed');
            DataSourceService.deleteDataSource.mockRejectedValueOnce(error);
            const dataSource = { id: '1', name: 'Test DB' };
            
            // Simulate confirmation
            wrapper.vm.$confirm = ({ onOk }) => onOk();
            
            await wrapper.vm.handleDelete(dataSource);
            
            expect(wrapper.vm.$message.error).toHaveBeenCalledWith('删除数据源失败');
        });
    });

    describe('Sync operations', () => {
        it('should start sync when clicking sync button', async () => {
            DataSourceService.syncMetadata.mockResolvedValueOnce({});
            const dataSource = { id: '1', name: 'Test DB' };
            
            // Simulate confirmation
            wrapper.vm.$confirm = ({ onOk }) => onOk();
            
            await wrapper.vm.handleSync(dataSource);
            
            expect(DataSourceService.syncMetadata).toHaveBeenCalledWith('1');
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('同步任务已启动');
            expect(DataSourceService.getDataSources).toHaveBeenCalled();
        });

        it('should prevent sync when already syncing', async () => {
            const dataSource = {
                id: '1',
                name: 'Test DB',
                syncStatus: { status: 'RUNNING' }
            };
            
            await wrapper.vm.handleSync(dataSource);
            
            expect(wrapper.vm.$message.warning).toHaveBeenCalledWith('该数据源正在同步中');
            expect(DataSourceService.syncMetadata).not.toHaveBeenCalled();
        });
    });

    describe('Table operations', () => {
        it('should handle table change events', async () => {
            const pagination = { current: 2, pageSize: 20 };
            const filters = { type: ['MYSQL'] };
            const sorter = { field: 'name', order: 'ascend' };
            
            await wrapper.vm.handleTableChange(pagination, filters, sorter);
            
            expect(DataSourceService.getDataSources).toHaveBeenCalledWith(
                expect.objectContaining({
                    page: 1,
                    size: 20,
                    sort: 'name,asc',
                    type: 'MYSQL'
                })
            );
        });
    });
});