import { mount } from '@vue/test-utils';
import DataSourceService from '../../../main/resources/static/js/services/datasource-service';
import UtilService from '../../../main/resources/static/js/services/util-service';

jest.mock('../../../main/resources/static/js/services/datasource-service');
jest.mock('../../../main/resources/static/js/services/util-service');

describe('DataSourceDetail', () => {
    let wrapper;
    const mockDataSource = {
        id: '1',
        name: 'Test DB',
        type: 'MYSQL',
        host: 'localhost',
        port: 3306,
        databaseName: 'testdb',
        username: 'test',
        active: true,
        tags: ['test', 'dev'],
        description: 'Test database',
        createdAt: '2025-03-17T10:00:00Z',
        updatedAt: '2025-03-17T11:00:00Z',
        lastSyncedAt: '2025-03-17T11:00:00Z',
        syncStatus: {
            status: 'COMPLETED',
            progress: 100,
            message: 'Sync completed successfully'
        }
    };

    const mockMetadataStats = {
        schemaCount: 2,
        tableCount: 10,
        viewCount: 3,
        columnCount: 50
    };

    const mockMetadataTree = [
        {
            name: 'public',
            tables: [
                { name: 'users', type: 'table' },
                { name: 'orders', type: 'table' }
            ]
        }
    ];

    const mockTableDetails = {
        name: 'users',
        columns: [
            {
                name: 'id',
                type: 'INTEGER',
                nullable: false,
                primaryKey: true
            },
            {
                name: 'username',
                type: 'VARCHAR',
                nullable: false,
                primaryKey: false
            }
        ],
        indexes: [
            {
                name: 'pk_users',
                type: 'BTREE',
                unique: true,
                columns: ['id']
            }
        ]
    };

    beforeEach(() => {
        jest.clearAllMocks();
        
        // Setup service mocks
        DataSourceService.getDataSource.mockResolvedValue({
            data: mockDataSource
        });
        
        DataSourceService.getMetadataStats.mockResolvedValue({
            data: mockMetadataStats
        });

        DataSourceService.getMetadataTree.mockResolvedValue({
            data: mockMetadataTree
        });

        DataSourceService.getTableDetails.mockResolvedValue({
            data: mockTableDetails
        });
        
        // Mount component
        wrapper = mount(DataSourceDetail, {
            propsData: {
                dataSourceId: '1'
            },
            stubs: [
                'a-tabs',
                'a-tab-pane',
                'a-descriptions',
                'a-descriptions-item',
                'a-tag',
                'a-statistic',
                'a-tree',
                'a-table',
                'a-spin',
                'a-card',
                'sync-status-badge'
            ],
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
        if (wrapper.vm.pollingTimer) {
            clearInterval(wrapper.vm.pollingTimer);
        }
    });

    describe('Component initialization', () => {
        it('should load data source details on mount', async () => {
            await wrapper.vm.$nextTick();
            expect(DataSourceService.getDataSource).toHaveBeenCalledWith('1');
            expect(DataSourceService.getMetadataStats).toHaveBeenCalledWith('1');
            expect(wrapper.vm.dataSource).toEqual(mockDataSource);
            expect(wrapper.vm.metadataStats).toEqual(mockMetadataStats);
        });

        it('should handle loading errors gracefully', async () => {
            const error = new Error('Failed to load');
            DataSourceService.getDataSource.mockRejectedValueOnce(error);
            
            await wrapper.vm.$nextTick();
            expect(wrapper.vm.$message.error).toHaveBeenCalledWith('获取数据源详情失败');
        });
    });

    describe('Metadata browsing', () => {
        it('should load metadata tree when switching to metadata tab', async () => {
            await wrapper.vm.handleTabChange('metadata');
            expect(DataSourceService.getMetadataTree).toHaveBeenCalledWith('1');
            expect(wrapper.vm.metadataTree).toHaveLength(1);
        });

        it('should transform metadata tree correctly', () => {
            const transformed = wrapper.vm.transformMetadataTree(mockMetadataTree);
            expect(transformed[0].children).toHaveLength(2);
            expect(transformed[0].children[0].isLeaf).toBe(true);
        });

        it('should load table details when selecting a table', async () => {
            const node = {
                type: 'table',
                schema: 'public',
                title: 'users'
            };
            
            await wrapper.vm.handleTreeSelect(['table-public-users'], { node });
            expect(DataSourceService.getTableDetails).toHaveBeenCalledWith('1', 'public', 'users');
            expect(wrapper.vm.tableDetails).toEqual(mockTableDetails);
        });
    });

    describe('Sync monitoring', () => {
        it('should start polling when sync is in progress', async () => {
            jest.useFakeTimers();
            
            const syncingDataSource = {
                ...mockDataSource,
                syncStatus: {
                    status: 'RUNNING',
                    progress: 50,
                    message: 'Syncing metadata'
                }
            };
            
            DataSourceService.getDataSource.mockResolvedValueOnce({
                data: syncingDataSource
            });
            
            wrapper = mount(DataSourceDetail, {
                propsData: { dataSourceId: '1' },
                stubs: ['a-tabs', 'sync-status-badge']
            });
            
            await wrapper.vm.$nextTick();
            expect(wrapper.vm.isSyncing).toBe(true);
            
            jest.advanceTimersByTime(3000);
            expect(DataSourceService.getDataSource).toHaveBeenCalledTimes(2);
            
            jest.useRealTimers();
        });

        it('should handle sync initiation', async () => {
            DataSourceService.syncMetadata.mockResolvedValue({});
            
            await wrapper.vm.handleSync();
            expect(DataSourceService.syncMetadata).toHaveBeenCalledWith('1');
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('同步任务已启动');
        });

        it('should prevent multiple sync initiations', async () => {
            wrapper.setData({
                dataSource: {
                    ...mockDataSource,
                    syncStatus: {
                        status: 'RUNNING'
                    }
                }
            });
            
            await wrapper.vm.handleSync();
            expect(DataSourceService.syncMetadata).not.toHaveBeenCalled();
            expect(wrapper.vm.$message.warning).toHaveBeenCalledWith('同步任务正在进行中');
        });
    });

    describe('Navigation', () => {
        it('should navigate to edit page', () => {
            wrapper.vm.handleEdit();
            expect(wrapper.vm.$router.push).toHaveBeenCalledWith('/datasource/edit/1');
        });

        it('should handle tab changes', async () => {
            await wrapper.vm.handleTabChange('syncHistory');
            expect(wrapper.vm.activeTab).toBe('syncHistory');
        });
    });

    describe('Cleanup', () => {
        it('should clear polling timer on destroy', () => {
            const clearIntervalSpy = jest.spyOn(window, 'clearInterval');
            wrapper.vm.pollingTimer = setInterval(() => {}, 1000);
            wrapper.destroy();
            expect(clearIntervalSpy).toHaveBeenCalled();
        });
    });
});