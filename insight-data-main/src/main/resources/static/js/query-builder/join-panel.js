/**
 * 连接配置面板组件
 * 用于配置表之间的连接关系和连接类型
 */
const JoinPanel = {
    props: {
        // 当前连接配置
        value: {
            type: Array,
            required: true
        },
        // 可用的表和列
        tables: {
            type: Array,
            required: true
        },
        // 是否禁用
        disabled: {
            type: Boolean,
            default: false
        }
    },

    data() {
        return {
            // 支持的连接类型
            joinTypes: [
                { value: 'INNER', text: '内连接', icon: 'connection' },
                { value: 'LEFT', text: '左连接', icon: 'align-left' },
                { value: 'RIGHT', text: '右连接', icon: 'align-right' },
                { value: 'FULL', text: '全连接', icon: 'block' }
            ],
            // 正在编辑的连接
            editingJoin: null,
            // 建议的表关系
            suggestions: [],
            // 加载状态
            loading: false
        };
    },

    computed: {
        // 所有可用的列
        availableColumns() {
            return this.tables.reduce((columns, table) => {
                table.columns.forEach(column => {
                    columns.push({
                        id: `${table.id}.${column.name}`,
                        tableId: table.id,
                        tableName: table.name,
                        ...column
                    });
                });
                return columns;
            }, []);
        }
    },

    methods: {
        // 添加新连接
        async addJoin() {
            const join = {
                id: UtilService.generateUniqueId(),
                leftTableId: '',
                leftColumn: '',
                rightTableId: '',
                rightColumn: '',
                type: 'INNER'
            };

            const joins = [...this.value, join];
            this.$emit('input', joins);
            this.editingJoin = join;

            // 如果已有其他表，尝试获取建议的关系
            if (this.tables.length > 1) {
                await this.loadSuggestions();
            }
        },

        // 编辑连接
        editJoin(join) {
            this.editingJoin = { ...join };
        },

        // 删除连接
        removeJoin(joinId) {
            this.$confirm({
                title: '确认删除',
                content: '确定要删除这个表连接吗？',
                okText: '确认',
                okType: 'danger',
                cancelText: '取消',
                onOk: () => {
                    const joins = this.value.filter(j => j.id !== joinId);
                    this.$emit('input', joins);
                    if (this.editingJoin?.id === joinId) {
                        this.editingJoin = null;
                    }
                }
            });
        },

        // 保存连接
        saveJoin() {
            if (!this.validateJoin(this.editingJoin)) {
                return;
            }

            const joins = this.value.map(j => 
                j.id === this.editingJoin.id ? this.editingJoin : j
            );
            this.$emit('input', joins);
            this.editingJoin = null;
        },

        // 取消编辑
        cancelEdit() {
            this.editingJoin = null;
        },

        // 验证连接配置
        validateJoin(join) {
            if (!join.leftTableId || !join.leftColumn) {
                this.$message.error('请选择左表和列');
                return false;
            }
            if (!join.rightTableId || !join.rightColumn) {
                this.$message.error('请选择右表和列');
                return false;
            }
            if (join.leftTableId === join.rightTableId) {
                this.$message.error('不能连接同一个表');
                return false;
            }
            return true;
        },

        // 加载表关系建议
        async loadSuggestions() {
            if (!this.editingJoin) return;

            this.loading = true;
            try {
                const leftTable = this.tables.find(t => t.id === this.editingJoin.leftTableId);
                const rightTable = this.tables.find(t => t.id === this.editingJoin.rightTableId);

                if (leftTable && rightTable) {
                    const response = await QueryService.inferTableRelationship(
                        this.$root.dataSourceId,
                        leftTable.name,
                        rightTable.name
                    );

                    this.suggestions = response.data.relationships || [];

                    if (this.suggestions.length > 0) {
                        // 自动填充最高置信度的建议
                        const bestMatch = this.suggestions[0];
                        this.editingJoin.leftColumn = bestMatch.leftColumn;
                        this.editingJoin.rightColumn = bestMatch.rightColumn;

                        this.$notification.info({
                            message: '发现可能的表关系',
                            description: `已自动填充推荐的连接列：${leftTable.name}.${bestMatch.leftColumn} = ${rightTable.name}.${bestMatch.rightColumn}`
                        });
                    }
                }
            } catch (error) {
                console.error('加载表关系建议失败:', error);
            } finally {
                this.loading = false;
            }
        },

        // 处理左表变更
        async handleLeftTableChange(tableId) {
            this.editingJoin.leftTableId = tableId;
            this.editingJoin.leftColumn = '';
            await this.loadSuggestions();
        },

        // 处理右表变更
        async handleRightTableChange(tableId) {
            this.editingJoin.rightTableId = tableId;
            this.editingJoin.rightColumn = '';
            await this.loadSuggestions();
        },

        // 获取表的列
        getTableColumns(tableId) {
            const table = this.tables.find(t => t.id === tableId);
            return table ? table.columns : [];
        },

        // 获取连接类型图标
        getJoinTypeIcon(type) {
            return this.joinTypes.find(t => t.value === type)?.icon || 'question';
        }
    },

    template: `
        <div class="join-panel">
            <!-- 连接列表 -->
            <div class="join-list">
                <a-list
                    :dataSource="value"
                    :locale="{ emptyText: '尚未配置表连接' }"
                >
                    <template #header>
                        <div class="join-list-header">
                            <h3>表连接配置</h3>
                            <a-button
                                type="primary"
                                icon="plus"
                                @click="addJoin"
                                :disabled="disabled"
                            >
                                添加连接
                            </a-button>
                        </div>
                    </template>

                    <a-list-item slot="renderItem" slot-scope="join">
                        <div class="join-item">
                            <!-- 连接信息展示 -->
                            <div class="join-info">
                                <a-tag :color="join.type === 'INNER' ? 'blue' : 'green'">
                                    <a-icon :type="getJoinTypeIcon(join.type)" />
                                    {{ joinTypes.find(t => t.value === join.type)?.text }}
                                </a-tag>
                                <div class="join-tables">
                                    {{ tables.find(t => t.id === join.leftTableId)?.name }}.
                                    {{ join.leftColumn }}
                                    <a-icon type="swap" style="margin: 0 8px" />
                                    {{ tables.find(t => t.id === join.rightTableId)?.name }}.
                                    {{ join.rightColumn }}
                                </div>
                            </div>

                            <!-- 操作按钮 -->
                            <div class="join-actions">
                                <a-button-group>
                                    <a-button
                                        type="link"
                                        icon="edit"
                                        @click="editJoin(join)"
                                        :disabled="disabled"
                                    />
                                    <a-button
                                        type="link"
                                        icon="delete"
                                        @click="removeJoin(join.id)"
                                        :disabled="disabled"
                                    />
                                </a-button-group>
                            </div>
                        </div>
                    </a-list-item>
                </a-list>
            </div>

            <!-- 编辑对话框 -->
            <a-modal
                v-model="!!editingJoin"
                :title="editingJoin?.id ? '编辑连接' : '添加连接'"
                @ok="saveJoin"
                @cancel="cancelEdit"
                :confirmLoading="loading"
            >
                <a-spin :spinning="loading">
                    <a-form layout="vertical">
                        <!-- 连接类型 -->
                        <a-form-item label="连接类型">
                            <a-radio-group
                                v-model="editingJoin.type"
                                button-style="solid"
                            >
                                <a-radio-button
                                    v-for="type in joinTypes"
                                    :key="type.value"
                                    :value="type.value"
                                >
                                    <a-icon :type="type.icon" />
                                    {{ type.text }}
                                </a-radio-button>
                            </a-radio-group>
                        </a-form-item>

                        <!-- 左表配置 -->
                        <a-form-item label="左表">
                            <a-row :gutter="8">
                                <a-col :span="12">
                                    <a-select
                                        v-model="editingJoin.leftTableId"
                                        placeholder="选择表"
                                        @change="handleLeftTableChange"
                                    >
                                        <a-select-option
                                            v-for="table in tables"
                                            :key="table.id"
                                            :value="table.id"
                                        >
                                            {{ table.name }}
                                        </a-select-option>
                                    </a-select>
                                </a-col>
                                <a-col :span="12">
                                    <a-select
                                        v-model="editingJoin.leftColumn"
                                        placeholder="选择列"
                                        :disabled="!editingJoin.leftTableId"
                                    >
                                        <a-select-option
                                            v-for="column in getTableColumns(editingJoin.leftTableId)"
                                            :key="column.name"
                                            :value="column.name"
                                        >
                                            {{ column.name }}
                                        </a-select-option>
                                    </a-select>
                                </a-col>
                            </a-row>
                        </a-form-item>

                        <!-- 右表配置 -->
                        <a-form-item label="右表">
                            <a-row :gutter="8">
                                <a-col :span="12">
                                    <a-select
                                        v-model="editingJoin.rightTableId"
                                        placeholder="选择表"
                                        @change="handleRightTableChange"
                                    >
                                        <a-select-option
                                            v-for="table in tables"
                                            :key="table.id"
                                            :value="table.id"
                                        >
                                            {{ table.name }}
                                        </a-select-option>
                                    </a-select>
                                </a-col>
                                <a-col :span="12">
                                    <a-select
                                        v-model="editingJoin.rightColumn"
                                        placeholder="选择列"
                                        :disabled="!editingJoin.rightTableId"
                                    >
                                        <a-select-option
                                            v-for="column in getTableColumns(editingJoin.rightTableId)"
                                            :key="column.name"
                                            :value="column.name"
                                        >
                                            {{ column.name }}
                                        </a-select-option>
                                    </a-select>
                                </a-col>
                            </a-row>
                        </a-form-item>

                        <!-- 建议列表 -->
                        <a-form-item v-if="suggestions.length > 0">
                            <div class="suggestions">
                                <h4>推荐的连接关系：</h4>
                                <a-list size="small">
                                    <a-list-item
                                        v-for="(suggestion, index) in suggestions"
                                        :key="index"
                                        class="suggestion-item"
                                        @click="() => {
                                            editingJoin.leftColumn = suggestion.leftColumn;
                                            editingJoin.rightColumn = suggestion.rightColumn;
                                        }"
                                    >
                                        <a-tag color="blue">
                                            {{ suggestion.confidence.toFixed(2) }}
                                        </a-tag>
                                        {{ suggestion.leftColumn }} = {{ suggestion.rightColumn }}
                                    </a-list-item>
                                </a-list>
                            </div>
                        </a-form-item>
                    </a-form>
                </a-spin>
            </a-modal>
        </div>
    `
};

// 导入依赖
import QueryService from '../services/query-service.js';
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('join-panel', JoinPanel);