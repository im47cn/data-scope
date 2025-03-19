/**
 * 数据源管理主组件
 */
const DataSource = {
    data() {
        return {
            activeKey: '1',
            dataSourceId: null,
            loading: false
        };
    },

    computed: {
        isDetailView() {
            return this.activeKey === '2' && this.dataSourceId;
        },
        isEditView() {
            return this.activeKey === '3' && this.dataSourceId;
        },
        isAddView() {
            return this.activeKey === '3' && !this.dataSourceId;
        }
    },

    mounted() {
        // 从URL中获取参数
        const params = new URLSearchParams(window.location.search);
        const action = params.get('action');
        const id = params.get('id');

        if (action === 'add') {
            this.activeKey = '3';
            this.dataSourceId = null;
        } else if (action === 'edit' && id) {
            this.activeKey = '3';
            this.dataSourceId = id;
        } else if (action === 'view' && id) {
            this.activeKey = '2';
            this.dataSourceId = id;
        } else {
            this.activeKey = '1';
            this.dataSourceId = null;
        }
    },

    methods: {
        handleTabChange(key) {
            this.activeKey = key;
            
            // 更新URL，但不重新加载页面
            const url = new URL(window.location);
            if (key === '1') {
                url.searchParams.delete('action');
                url.searchParams.delete('id');
            }
            window.history.pushState({}, '', url);
        },

        handleAdd() {
            this.dataSourceId = null;
            this.activeKey = '3';
            
            // 更新URL，但不重新加载页面
            const url = new URL(window.location);
            url.searchParams.set('action', 'add');
            url.searchParams.delete('id');
            window.history.pushState({}, '', url);
        },

        handleEdit(id) {
            this.dataSourceId = id;
            this.activeKey = '3';
            
            // 更新URL，但不重新加载页面
            const url = new URL(window.location);
            url.searchParams.set('action', 'edit');
            url.searchParams.set('id', id);
            window.history.pushState({}, '', url);
        },

        handleView(id) {
            this.dataSourceId = id;
            this.activeKey = '2';
            
            // 更新URL，但不重新加载页面
            const url = new URL(window.location);
            url.searchParams.set('action', 'view');
            url.searchParams.set('id', id);
            window.history.pushState({}, '', url);
        },

        handleBack() {
            this.activeKey = '1';
            this.dataSourceId = null;
            
            // 更新URL，但不重新加载页面
            const url = new URL(window.location);
            url.searchParams.delete('action');
            url.searchParams.delete('id');
            window.history.pushState({}, '', url);
        },

        handleSaveSuccess(id) {
            this.$message.success('数据源保存成功');
            this.dataSourceId = id;
            this.activeKey = '2';
            
            // 更新URL，但不重新加载页面
            const url = new URL(window.location);
            url.searchParams.set('action', 'view');
            url.searchParams.set('id', id);
            window.history.pushState({}, '', url);
        },

        handleDeleteSuccess() {
            this.$message.success('数据源删除成功');
            this.activeKey = '1';
            this.dataSourceId = null;
            
            // 更新URL，但不重新加载页面
            const url = new URL(window.location);
            url.searchParams.delete('action');
            url.searchParams.delete('id');
            window.history.pushState({}, '', url);
        }
    },

    template: `
        <div class="datasource-container">
            <a-spin :spinning="loading">
                <a-tabs 
                    :activeKey="activeKey" 
                    @change="handleTabChange"
                    :animated="false"
                >
                    <a-tab-pane key="1" tab="数据源列表">
                        <datasource-list 
                            @view="handleView"
                            @edit="handleEdit"
                            @add="handleAdd"
                        />
                    </a-tab-pane>
                    
                    <a-tab-pane key="2" tab="数据源详情" :disabled="!isDetailView">
                        <div v-if="isDetailView">
                            <div class="page-header">
                                <a-button @click="handleBack" icon="arrow-left">返回列表</a-button>
                                <a-button 
                                    type="primary" 
                                    @click="handleEdit(dataSourceId)" 
                                    icon="edit"
                                    style="margin-left: 8px;"
                                >
                                    编辑
                                </a-button>
                            </div>
                            <datasource-detail 
                                :dataSourceId="dataSourceId"
                                @delete-success="handleDeleteSuccess"
                            />
                        </div>
                    </a-tab-pane>
                    
                    <a-tab-pane key="3" tab="添加/编辑数据源" :disabled="!isEditView && !isAddView">
                        <div v-if="isEditView || isAddView">
                            <div class="page-header">
                                <a-button @click="handleBack" icon="arrow-left">返回列表</a-button>
                            </div>
                            <datasource-form 
                                :dataSourceId="dataSourceId"
                                @save-success="handleSaveSuccess"
                                @cancel="handleBack"
                            />
                        </div>
                    </a-tab-pane>
                </a-tabs>
            </a-spin>
        </div>
    `
};

// 注册组件
Vue.component('datasource', DataSource);