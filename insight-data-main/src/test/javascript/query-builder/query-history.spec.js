import { mount } from '@vue/test-utils';
import QueryService from '../../../../insight-data-main/src/main/resources/static/js/services/query-service';
import UtilService from '../../../../insight-data-main/src/main/resources/static/js/services/util-service';

jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/query-service');
jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/util-service');

describe('QueryHistory', () => {
    let wrapper;
    const mockDataSourceId = 'test-ds-1';
    const mockHistoryRecords = [
        {
            id: '1',
            sql: 'SELECT * FROM users',
            executedAt: '2025-03-17T10:00:00Z',
            executionTime: 0.5,
            affectedRows: 10,
            status: 'SUCCESS'
        },
        {
            id: '2',
            sql: 'INSERT INTO users(name) VALUES("test")',
            executedAt: '2025-03-17T11:00:00Z',
            executionTime: 0.3,
            affectedRows: 1,
            status: 'SUCCESS'
        },
        {
            id: '3',
            sql: 'SELECT * FROM invalid_table',
            executedAt: '2025-03-17T12:00:00Z',
            executionTime: 0.1,
            affectedRows: 0,
            status: 'FAILED',
            error: 'Table not found'
        }
    ];

    beforeEach(() => {
        jest.clearAllMocks();
        
        // Mock service methods
        QueryService.getQueryHistory.mockResolvedValue({
            data: {
                content: mockHistoryRecords,
                total: mockHistoryRecords.length
            }
        });

        UtilService.formatDateTime.mockImplementation(date => date);
        UtilService.copyToClipboard.mockResolvedValue();
        UtilService.debounce.mockImplementation(fn => fn);

        wrapper = mount(QueryHistory, {
            propsData: {
                dataSourceId: mockDataSourceId
            },
            stubs: [
                'a-table',
                'a-input-search',
                'a-range-picker',
                'a-button',
                'a-icon',
                'a-space',
                'a-tag',
                'a-tooltip',
                'a-drawer',
                'a-descriptions',
                'a-descriptions-item'
            ],
            mocks: {
                $message: {
                    success: jest.fn(),
                    error: jest.fn(),
                    warning: jest.fn()
                },
                $confirm: jest.fn(({ onOk }) => onOk())
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    describe('Initial loading', () => {
        it('should load history records on mount', async () => {
            expect(QueryService.getQueryHistory).toHaveBeenCalledWith(
                expect.objectContaining({
                    dataSourceId: mockDataSourceId
                })
            );
            expect(wrapper.vm.historyRecords).toEqual(mockHistoryRecords);
        });

        it('should handle loading error', async () => {
            QueryService.getQueryHistory.mockRejectedValueOnce(new Error('Load failed'));
            
            await wrapper.vm.loadHistory();
            
            expect(wrapper.vm.$message.error).toHaveBeenCalledWith('加载查询历史失败');
            expect(wrapper.vm.loading).toBe(false);
        });
    });

    describe('Search and filtering', () => {
        it('should handle search text changes', async () => {
            wrapper.setData({ searchText: 'SELECT' });
            await wrapper.vm.handleSearch();
            
            expect(QueryService.getQueryHistory).toHaveBeenCalledWith(
                expect.objectContaining({
                    search: 'SELECT'
                })
            );
        });

        it('should handle date range changes', async () => {
            const dateRange = ['2025-03-17', '2025-03-18'];
            await wrapper.vm.handleDateRangeChange(dateRange);
            
            expect(QueryService.getQueryHistory).toHaveBeenCalledWith(
                expect.objectContaining({
                    startDate: dateRange[0],
                    endDate: dateRange[1]
                })
            );
        });

        it('should handle status filter', async () => {
            const filters = { status: ['SUCCESS'] };
            await wrapper.vm.handleTableChange(wrapper.vm.pagination, filters, null);
            
            expect(wrapper.vm.filters.status).toEqual(['SUCCESS']);
            expect(QueryService.getQueryHistory).toHaveBeenCalled();
        });
    });

    describe('Record operations', () => {
        it('should show record details', async () => {
            await wrapper.vm.handleViewDetails(mockHistoryRecords[0]);
            expect(wrapper.vm.selectedRecord).toEqual(mockHistoryRecords[0]);
            expect(wrapper.emitted().select[0]).toEqual([mockHistoryRecords[0]]);
        });

        it('should emit reuse event', async () => {
            await wrapper.vm.handleReuseQuery(mockHistoryRecords[0]);
            expect(wrapper.emitted().reuse[0]).toEqual([mockHistoryRecords[0]]);
        });

        it('should copy SQL to clipboard', async () => {
            const sql = 'SELECT * FROM users';
            await wrapper.vm.handleCopySQL(sql);
            
            expect(UtilService.copyToClipboard).toHaveBeenCalledWith(sql);
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('SQL已复制到剪贴板');
        });

        it('should handle copy failure', async () => {
            UtilService.copyToClipboard.mockRejectedValueOnce(new Error());
            
            await wrapper.vm.handleCopySQL('SELECT * FROM users');
            expect(wrapper.vm.$message.error).toHaveBeenCalledWith('复制失败，请手动复制');
        });

        it('should save query', async () => {
            const record = mockHistoryRecords[0];
            await wrapper.vm.handleSaveQuery(record);
            
            expect(QueryService.saveQuery).toHaveBeenCalledWith(
                expect.objectContaining({
                    sql: record.sql,
                    dataSourceId: mockDataSourceId
                })
            );
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('保存成功');
        });

        it('should delete record', async () => {
            const record = mockHistoryRecords[0];
            await wrapper.vm.handleDelete(record);
            
            expect(QueryService.deleteQueryHistory).toHaveBeenCalledWith(record.id);
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('删除成功');
            expect(QueryService.getQueryHistory).toHaveBeenCalled();
        });

        it('should clear selected record after deletion', async () => {
            const record = mockHistoryRecords[0];
            wrapper.setData({ selectedRecord: record });
            
            await wrapper.vm.handleDelete(record);
            
            expect(wrapper.vm.selectedRecord).toBeNull();
        });
    });

    describe('Pagination', () => {
        it('should handle page changes', async () => {
            const pagination = { current: 2, pageSize: 20 };
            await wrapper.vm.handleTableChange(pagination, {}, null);
            
            expect(wrapper.vm.pagination.current).toBe(2);
            expect(wrapper.vm.pagination.pageSize).toBe(20);
            expect(QueryService.getQueryHistory).toHaveBeenCalledWith(
                expect.objectContaining({
                    page: 1,
                    size: 20
                })
            );
        });
    });

    describe('Visual behaviors', () => {
        it('should show error message in details drawer', async () => {
            const failedRecord = mockHistoryRecords[2];
            await wrapper.vm.handleViewDetails(failedRecord);
            
            const drawer = wrapper.find('.ant-drawer');
            expect(drawer.exists()).toBe(true);
            expect(drawer.html()).toContain(failedRecord.error);
        });

        it('should format execution time', () => {
            const cell = wrapper.vm.$options.components['a-table'].scopedSlots.executionTime({
                executionTime: 1.23456
            });
            expect(cell).toBe('1.23秒');
        });

        it('should show status tags with correct colors', () => {
            const successTag = wrapper.vm.$options.components['a-table'].scopedSlots.status('SUCCESS');
            const failedTag = wrapper.vm.$options.components['a-table'].scopedSlots.status('FAILED');
            
            expect(successTag.props.color).toBe('success');
            expect(failedTag.props.color).toBe('error');
        });
    });
});