import { mount } from '@vue/test-utils';
import QueryService from '../../../main/resources/static/js/services/query-service';
import UtilService from '../../../main/resources/static/js/services/util-service';

jest.mock('../../../main/resources/static/js/services/query-service');
jest.mock('../../../main/resources/static/js/services/util-service');

describe('ResultsPreview', () => {
    let wrapper;
    const mockQueryId = 'test-query-123';
    const mockResults = {
        columns: [
            { name: 'id', type: 'INTEGER' },
            { name: 'name', type: 'VARCHAR' },
            { name: 'created_at', type: 'TIMESTAMP' }
        ],
        rows: [
            { id: 1, name: 'Test 1', created_at: '2025-03-17T10:00:00Z' },
            { id: 2, name: 'Test 2', created_at: '2025-03-17T11:00:00Z' }
        ],
        total: 2,
        executionTime: 0.5,
        affectedRows: 0
    };

    beforeEach(() => {
        jest.clearAllMocks();
        
        // Mock service methods
        QueryService.getQueryStatus.mockResolvedValue({
            data: { status: 'COMPLETED' }
        });
        
        QueryService.getQueryResults.mockResolvedValue({
            data: mockResults
        });

        UtilService.formatDateTime.mockImplementation(date => date);
        UtilService.copyToClipboard.mockResolvedValue();
        UtilService.downloadFile.mockImplementation();

        wrapper = mount(ResultsPreview, {
            propsData: {
                queryId: mockQueryId,
                autoLoad: true
            },
            stubs: [
                'a-table',
                'a-button',
                'a-space',
                'a-dropdown',
                'a-menu',
                'a-menu-item',
                'a-switch',
                'a-tooltip',
                'a-icon',
                'a-statistic',
                'a-alert',
                'a-modal',
                'a-progress'
            ],
            mocks: {
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

    describe('Initial loading', () => {
        it('should load results on mount when autoLoad is true', async () => {
            expect(QueryService.getQueryStatus).toHaveBeenCalledWith(mockQueryId);
            expect(QueryService.getQueryResults).toHaveBeenCalled();
            expect(wrapper.vm.dataSource).toHaveLength(2);
        });

        it('should not load results when autoLoad is false', async () => {
            wrapper = mount(ResultsPreview, {
                propsData: {
                    queryId: mockQueryId,
                    autoLoad: false
                }
            });

            expect(QueryService.getQueryStatus).not.toHaveBeenCalled();
        });

        it('should handle loading error', async () => {
            QueryService.getQueryStatus.mockRejectedValueOnce(new Error('Load failed'));
            
            await wrapper.vm.loadResults();
            
            expect(wrapper.vm.error).toBeTruthy();
            expect(wrapper.vm.loading).toBe(false);
        });
    });

    describe('Query status polling', () => {
        it('should start polling for running queries', async () => {
            QueryService.getQueryStatus.mockResolvedValueOnce({
                data: { status: 'RUNNING' }
            });

            await wrapper.vm.loadResults();
            
            expect(wrapper.vm.pollingTimer).toBeTruthy();
            expect(wrapper.vm.pollingCount).toBe(0);
        });

        it('should stop polling when query completes', async () => {
            await wrapper.vm.startPolling();
            
            QueryService.getQueryStatus.mockResolvedValueOnce({
                data: { status: 'COMPLETED' }
            });

            await wrapper.vm.pollQueryStatus();
            
            expect(wrapper.vm.pollingTimer).toBeNull();
            expect(QueryService.getQueryResults).toHaveBeenCalled();
        });

        it('should handle polling timeout', async () => {
            wrapper.vm.maxPollingCount = 1;
            await wrapper.vm.startPolling();
            
            QueryService.getQueryStatus.mockResolvedValueOnce({
                data: { status: 'RUNNING' }
            });

            await wrapper.vm.pollQueryStatus();
            
            expect(wrapper.vm.error).toBe('查询超时');
            expect(wrapper.vm.pollingTimer).toBeNull();
        });
    });

    describe('Data handling', () => {
        it('should format data according to column types', () => {
            const timestampCell = wrapper.vm.renderCell('2025-03-17T10:00:00Z', { type: 'TIMESTAMP' });
            expect(UtilService.formatDateTime).toHaveBeenCalled();

            const numberCell = wrapper.vm.renderCell(1000.5, { type: 'DECIMAL' });
            expect(numberCell).toBe('1,000.5');

            const nullCell = wrapper.vm.renderCell(null, { type: 'VARCHAR' });
            expect(nullCell.className).toBe('null-value');
        });

        it('should handle table changes', async () => {
            const pagination = { current: 2, pageSize: 20 };
            const filters = { name: ['Test 1'] };
            const sorter = { field: 'id', order: 'descend' };

            await wrapper.vm.handleTableChange(pagination, filters, sorter);
            
            expect(wrapper.vm.pagination.current).toBe(2);
            expect(wrapper.vm.pagination.pageSize).toBe(20);
            expect(wrapper.vm.sorter).toEqual(sorter);
            expect(wrapper.vm.filters).toEqual({ name: ['Test 1'] });
            expect(QueryService.getQueryResults).toHaveBeenCalled();
        });
    });

    describe('Export functionality', () => {
        it('should prevent export when data exceeds limit', async () => {
            wrapper.setData({
                stats: { totalRows: 20000 },
                maxExportSize: 10000
            });

            await wrapper.vm.exportData();
            
            expect(wrapper.vm.$message.error).toHaveBeenCalled();
            expect(UtilService.downloadFile).not.toHaveBeenCalled();
        });

        it('should export data successfully', async () => {
            const format = 'excel';
            const url = 'mock-export-url';
            
            QueryService.getExportUrl.mockReturnValue(url);
            
            await wrapper.vm.exportData(format);
            
            expect(QueryService.getExportUrl).toHaveBeenCalledWith(mockQueryId, format);
            expect(UtilService.downloadFile).toHaveBeenCalled();
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('导出成功');
        });
    });

    describe('Copy functionality', () => {
        it('should copy selected rows', async () => {
            const selectedRows = [
                { id: 1, name: 'Test 1' },
                { id: 2, name: 'Test 2' }
            ];
            
            wrapper.vm.selectedRows = selectedRows;
            await wrapper.vm.copySelectedData();
            
            expect(UtilService.copyToClipboard).toHaveBeenCalled();
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('数据已复制到剪贴板');
        });

        it('should show warning when no rows selected', async () => {
            wrapper.vm.selectedRows = [];
            await wrapper.vm.copySelectedData();
            
            expect(UtilService.copyToClipboard).not.toHaveBeenCalled();
            expect(wrapper.vm.$message.warning).toHaveBeenCalled();
        });
    });

    describe('Visual options', () => {
        it('should toggle row numbers', async () => {
            await wrapper.setData({
                visualOptions: { showRowNumbers: true }
            });
            
            expect(wrapper.vm.columns[0].title).toBe('#');

            await wrapper.setData({
                visualOptions: { showRowNumbers: false }
            });
            
            expect(wrapper.vm.columns[0].title).not.toBe('#');
        });

        it('should toggle text wrapping', async () => {
            await wrapper.setData({
                visualOptions: { wrapText: true }
            });
            
            expect(wrapper.vm.columns.some(col => !col.ellipsis)).toBe(true);

            await wrapper.setData({
                visualOptions: { wrapText: false }
            });
            
            expect(wrapper.vm.columns.every(col => col.ellipsis)).toBe(true);
        });
    });
});