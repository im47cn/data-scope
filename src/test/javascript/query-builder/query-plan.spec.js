import { mount } from '@vue/test-utils';
import QueryService from '../../../main/resources/static/js/services/query-service';
import UtilService from '../../../main/resources/static/js/services/util-service';

jest.mock('../../../main/resources/static/js/services/query-service');
jest.mock('../../../main/resources/static/js/services/util-service');

describe('QueryPlan', () => {
    let wrapper;
    const mockQueryId = 'test-query-1';
    const mockSql = 'SELECT * FROM users';
    const mockPlanData = {
        operation: 'Seq Scan',
        table: 'users',
        cost: 100,
        rows: 1000,
        children: [
            {
                operation: 'Index Scan',
                table: 'orders',
                cost: 50,
                rows: 500,
                children: []
            }
        ]
    };

    beforeEach(() => {
        jest.clearAllMocks();
        
        // Mock service methods
        QueryService.analyzeQuery.mockResolvedValue({
            data: {
                plan: mockPlanData,
                statistics: {
                    totalCost: 150,
                    avgCost: 75
                },
                metrics: {
                    totalRows: 1500,
                    estimatedMemory: 1024 * 1024,
                    estimatedTime: 100
                },
                suggestions: [
                    {
                        level: 'critical',
                        message: 'Consider adding index on users.id'
                    }
                ]
            }
        });

        UtilService.debounce.mockImplementation(fn => fn);
        UtilService.formatFileSize.mockImplementation(size => `${size} bytes`);
        UtilService.copyToClipboard.mockResolvedValue();

        // Mock D3 visualization libraries
        global.D3Tree = jest.fn(() => ({
            destroy: jest.fn()
        }));
        global.D3Graph = jest.fn(() => ({
            destroy: jest.fn()
        }));

        wrapper = mount(QueryPlan, {
            propsData: {
                queryId: mockQueryId,
                sql: mockSql,
                mode: 'tree'
            },
            stubs: [
                'a-radio-group',
                'a-radio-button',
                'a-button',
                'a-icon',
                'a-row',
                'a-col',
                'a-statistic',
                'a-spin',
                'a-empty',
                'a-collapse',
                'a-collapse-panel',
                'a-timeline',
                'a-timeline-item',
                'a-badge'
            ],
            mocks: {
                $message: {
                    success: jest.fn(),
                    error: jest.fn()
                }
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    describe('Initial loading', () => {
        it('should load query plan on mount', async () => {
            expect(QueryService.analyzeQuery).toHaveBeenCalledWith({
                queryId: mockQueryId,
                sql: mockSql
            });
            
            expect(wrapper.vm.planData).toEqual(mockPlanData);
            expect(wrapper.vm.hasData).toBe(true);
        });

        it('should handle loading errors', async () => {
            const error = new Error('Analysis failed');
            QueryService.analyzeQuery.mockRejectedValueOnce(error);
            
            await wrapper.vm.loadQueryPlan();
            
            expect(wrapper.vm.error).toBeTruthy();
            expect(wrapper.vm.loading).toBe(false);
        });
    });

    describe('Plan visualization', () => {
        it('should transform plan data to tree format', () => {
            const treeData = wrapper.vm.transformToTree(mockPlanData);
            
            expect(treeData.name).toContain('Seq Scan');
            expect(treeData.children).toHaveLength(1);
            expect(treeData.children[0].name).toContain('Index Scan');
        });

        it('should transform plan data to graph format', () => {
            const { nodes, edges } = wrapper.vm.transformToGraph(mockPlanData);
            
            expect(nodes).toHaveLength(2);
            expect(edges).toHaveLength(1);
            expect(nodes[0].label).toContain('Seq Scan');
            expect(nodes[1].label).toContain('Index Scan');
        });

        it('should format node labels correctly', () => {
            const node = {
                operation: 'Index Scan',
                table: 'users',
                cost: 100.123,
                rows: 1000
            };

            const label = wrapper.vm.formatNodeLabel(node);
            
            expect(label).toContain('Index Scan');
            expect(label).toContain('[users]');
            expect(label).toContain('成本: 100.12');
            expect(label).toContain('行数: 1,000');
        });

        it('should handle visualization mode changes', async () => {
            const renderSpy = jest.spyOn(wrapper.vm, 'renderPlan');
            
            await wrapper.setProps({ mode: 'graph' });
            
            expect(renderSpy).toHaveBeenCalled();
            expect(wrapper.vm.mode).toBe('graph');
        });
    });

    describe('Critical path analysis', () => {
        it('should identify critical path correctly', () => {
            const criticalPath = wrapper.vm.criticalPath;
            
            expect(criticalPath).toHaveLength(2);
            expect(criticalPath[0].operation).toBe('Seq Scan');
            expect(criticalPath[0].cost).toBe(100);
        });

        it('should calculate node colors based on cost', () => {
            const criticalNode = {
                cost: 100,
                isCritical: true
            };
            expect(wrapper.vm.getNodeColor(criticalNode)).toBe('#f5222d');

            const highCostNode = {
                cost: 80,
                isCritical: false
            };
            expect(wrapper.vm.getNodeColor(highCostNode)).toBe('#faad14');

            const normalNode = {
                cost: 50,
                isCritical: false
            };
            expect(wrapper.vm.getNodeColor(normalNode)).toBe('#1890ff');
        });
    });

    describe('User interactions', () => {
        it('should copy plan details', async () => {
            await wrapper.vm.copyPlanDetails();
            
            expect(UtilService.copyToClipboard).toHaveBeenCalledWith(
                JSON.stringify(mockPlanData, null, 2)
            );
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('计划详情已复制到剪贴板');
        });

        it('should handle copy failure', async () => {
            UtilService.copyToClipboard.mockRejectedValueOnce(new Error());
            
            await wrapper.vm.copyPlanDetails();
            
            expect(wrapper.vm.$message.error).toHaveBeenCalledWith('复制失败，请手动复制');
        });

        it('should emit node click events', () => {
            const node = { operation: 'Seq Scan' };
            wrapper.vm.handleNodeClick(node);
            
            expect(wrapper.emitted()['node-click'][0]).toEqual([node]);
        });
    });

    describe('Cleanup', () => {
        it('should clean up resources on destroy', () => {
            const chart = { destroy: jest.fn() };
            wrapper.vm.chart = chart;
            
            wrapper.destroy();
            
            expect(chart.destroy).toHaveBeenCalled();
        });

        it('should remove resize listener on destroy', () => {
            const removeEventListenerSpy = jest.spyOn(window, 'removeEventListener');
            
            wrapper.destroy();
            
            expect(removeEventListenerSpy).toHaveBeenCalledWith(
                'resize',
                wrapper.vm.debouncedRender
            );
        });
    });
});