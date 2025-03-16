/**
 * 表关系图组件
 * 用于可视化展示表之间的关系
 * 基于D3.js实现
 */
class TableRelationshipGraph {
    constructor(containerId) {
        this.containerId = containerId;
        this.relationships = [];
        this.dataSourceId = null;
        this.width = 800;
        this.height = 600;
        this.svg = null;
        this.simulation = null;
        this.nodes = [];
        this.links = [];
        this.init();
    }

    /**
     * 初始化组件
     */
    init() {
        this.render();
    }

    /**
     * 设置数据源ID
     */
    setDataSourceId(dataSourceId) {
        this.dataSourceId = dataSourceId;
        this.loadTableRelationships();
    }

    /**
     * 加载表关系数据
     */
    loadTableRelationships() {
        if (!this.dataSourceId) {
            return;
        }

        // 显示加载中
        this.showLoading();

        // 获取表关系数据
        fetch(`/api/table-relationships?dataSourceId=${this.dataSourceId}`)
            .then(response => response.json())
            .then(data => {
                this.relationships = data;
                this.prepareGraphData();
                this.renderGraph();
                this.hideLoading();
            })
            .catch(error => {
                console.error('加载表关系失败:', error);
                this.showError('加载表关系失败，请稍后重试');
                this.hideLoading();
            });
    }

    /**
     * 准备图形数据
     */
    prepareGraphData() {
        // 提取所有表名
        const tableNames = new Set();
        this.relationships.forEach(relationship => {
            tableNames.add(relationship.sourceTable);
            tableNames.add(relationship.targetTable);
        });

        // 创建节点
        this.nodes = Array.from(tableNames).map(name => ({
            id: name,
            name: name,
            group: 1
        }));

        // 创建连接
        this.links = this.relationships.map(relationship => ({
            source: relationship.sourceTable,
            target: relationship.targetTable,
            value: relationship.weight,
            type: relationship.type,
            sourceColumn: relationship.sourceColumn,
            targetColumn: relationship.targetColumn
        }));
    }

    /**
     * 渲染组件
     */
    render() {
        const container = document.getElementById(this.containerId);
        if (!container) {
            console.error(`容器 #${this.containerId} 不存在`);
            return;
        }

        // 构建HTML
        let html = `
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5>表关系可视化</h5>
                    <div>
                        <button id="btn-zoom-in" class="btn btn-outline-secondary btn-sm">
                            <i class="fas fa-search-plus"></i>
                        </button>
                        <button id="btn-zoom-out" class="btn btn-outline-secondary btn-sm">
                            <i class="fas fa-search-minus"></i>
                        </button>
                        <button id="btn-reset-zoom" class="btn btn-outline-secondary btn-sm">
                            <i class="fas fa-sync"></i>
                        </button>
                    </div>
                </div>
                <div class="card-body">
                    <div id="relationship-graph-loading" class="text-center py-3" style="display: none;">
                        <div class="spinner-border text-primary" role="status">
                            <span class="visually-hidden">加载中...</span>
                        </div>
                    </div>
                    <div id="relationship-graph-error" class="alert alert-danger" style="display: none;"></div>
                    <div id="relationship-graph-container" style="width: 100%; height: 600px;"></div>
                </div>
            </div>
        `;

        container.innerHTML = html;

        // 绑定事件
        this.bindEvents();
    }

    /**
     * 渲染关系图
     */
    renderGraph() {
        const container = document.getElementById('relationship-graph-container');
        if (!container) {
            console.error('图形容器不存在');
            return;
        }

        // 清空容器
        container.innerHTML = '';

        // 获取容器尺寸
        const rect = container.getBoundingClientRect();
        this.width = rect.width;
        this.height = rect.height;

        // 创建SVG
        this.svg = d3.select('#relationship-graph-container')
            .append('svg')
            .attr('width', this.width)
            .attr('height', this.height)
            .call(d3.zoom().on('zoom', (event) => {
                this.svg.attr('transform', event.transform);
            }))
            .append('g');

        // 添加箭头定义
        this.svg.append('defs').selectAll('marker')
            .data(['end'])
            .enter().append('marker')
            .attr('id', d => d)
            .attr('viewBox', '0 -5 10 10')
            .attr('refX', 15)
            .attr('refY', 0)
            .attr('markerWidth', 6)
            .attr('markerHeight', 6)
            .attr('orient', 'auto')
            .append('path')
            .attr('d', 'M0,-5L10,0L0,5')
            .attr('fill', '#999');

        // 创建力导向图
        this.simulation = d3.forceSimulation(this.nodes)
            .force('link', d3.forceLink(this.links).id(d => d.id).distance(150))
            .force('charge', d3.forceManyBody().strength(-300))
            .force('center', d3.forceCenter(this.width / 2, this.height / 2))
            .on('tick', () => this.ticked());

        // 创建连接
        this.link = this.svg.append('g')
            .attr('class', 'links')
            .selectAll('line')
            .data(this.links)
            .enter().append('line')
            .attr('stroke-width', d => Math.sqrt(d.value) * 2)
            .attr('stroke', '#999')
            .attr('stroke-opacity', 0.6)
            .attr('marker-end', 'url(#end)');

        // 创建连接标签
        this.linkText = this.svg.append('g')
            .attr('class', 'link-labels')
            .selectAll('text')
            .data(this.links)
            .enter().append('text')
            .attr('font-size', 10)
            .attr('fill', '#666')
            .text(d => `${d.sourceColumn} → ${d.targetColumn}`);

        // 创建节点
        this.node = this.svg.append('g')
            .attr('class', 'nodes')
            .selectAll('circle')
            .data(this.nodes)
            .enter().append('circle')
            .attr('r', 10)
            .attr('fill', this.getNodeColor)
            .call(d3.drag()
                .on('start', (event, d) => this.dragstarted(event, d))
                .on('drag', (event, d) => this.dragged(event, d))
                .on('end', (event, d) => this.dragended(event, d)));

        // 创建节点标签
        this.nodeText = this.svg.append('g')
            .attr('class', 'node-labels')
            .selectAll('text')
            .data(this.nodes)
            .enter().append('text')
            .attr('font-size', 12)
            .attr('dx', 15)
            .attr('dy', 4)
            .text(d => d.name);

        // 添加节点提示
        this.node.append('title')
            .text(d => d.name);
    }

    /**
     * 更新图形位置
     */
    ticked() {
        this.link
            .attr('x1', d => d.source.x)
            .attr('y1', d => d.source.y)
            .attr('x2', d => d.target.x)
            .attr('y2', d => d.target.y);

        this.linkText
            .attr('x', d => (d.source.x + d.target.x) / 2)
            .attr('y', d => (d.source.y + d.target.y) / 2);

        this.node
            .attr('cx', d => d.x)
            .attr('cy', d => d.y);

        this.nodeText
            .attr('x', d => d.x)
            .attr('y', d => d.y);
    }

    /**
     * 拖拽开始
     */
    dragstarted(event, d) {
        if (!event.active) this.simulation.alphaTarget(0.3).restart();
        d.fx = d.x;
        d.fy = d.y;
    }

    /**
     * 拖拽中
     */
    dragged(event, d) {
        d.fx = event.x;
        d.fy = event.y;
    }

    /**
     * 拖拽结束
     */
    dragended(event, d) {
        if (!event.active) this.simulation.alphaTarget(0);
        d.fx = null;
        d.fy = null;
    }

    /**
     * 获取节点颜色
     */
    getNodeColor(d) {
        // 使用d3的颜色比例尺
        const color = d3.scaleOrdinal(d3.schemeCategory10);
        return color(d.group);
    }

    /**
     * 绑定事件
     */
    bindEvents() {
        const container = document.getElementById(this.containerId);
        if (!container) return;

        // 放大按钮
        container.querySelector('#btn-zoom-in').addEventListener('click', () => {
            this.zoomIn();
        });

        // 缩小按钮
        container.querySelector('#btn-zoom-out').addEventListener('click', () => {
            this.zoomOut();
        });

        // 重置按钮
        container.querySelector('#btn-reset-zoom').addEventListener('click', () => {
            this.resetZoom();
        });
    }

    /**
     * 放大
     */
    zoomIn() {
        const zoom = d3.zoom().on('zoom', (event) => {
            this.svg.attr('transform', event.transform);
        });
        
        d3.select('#relationship-graph-container svg')
            .transition()
            .duration(300)
            .call(zoom.scaleBy, 1.2);
    }

    /**
     * 缩小
     */
    zoomOut() {
        const zoom = d3.zoom().on('zoom', (event) => {
            this.svg.attr('transform', event.transform);
        });
        
        d3.select('#relationship-graph-container svg')
            .transition()
            .duration(300)
            .call(zoom.scaleBy, 0.8);
    }

    /**
     * 重置缩放
     */
    resetZoom() {
        const zoom = d3.zoom().on('zoom', (event) => {
            this.svg.attr('transform', event.transform);
        });
        
        d3.select('#relationship-graph-container svg')
            .transition()
            .duration(300)
            .call(zoom.transform, d3.zoomIdentity);
    }

    /**
     * 显示加载中
     */
    showLoading() {
        const loading = document.getElementById('relationship-graph-loading');
        if (loading) {
            loading.style.display = 'block';
        }
    }

    /**
     * 隐藏加载中
     */
    hideLoading() {
        const loading = document.getElementById('relationship-graph-loading');
        if (loading) {
            loading.style.display = 'none';
        }
    }

    /**
     * 显示错误信息
     */
    showError(message) {
        const error = document.getElementById('relationship-graph-error');
        if (error) {
            error.textContent = message;
            error.style.display = 'block';
            
            // 3秒后自动隐藏
            setTimeout(() => {
                error.style.display = 'none';
            }, 3000);
        }
    }
}

// 导出组件
export default TableRelationshipGraph;