/**
 * 查询条件构建器组件
 * 支持构建复杂的WHERE子句，包括嵌套条件和逻辑运算
 */
const ConditionBuilder = {
    props: {
        // 当前条件配置
        value: {
            type: Object,
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
            // 支持的操作符
            operators: [
                { value: '=', text: '等于', types: ['string', 'number', 'date', 'boolean'] },
                { value: '!=', text: '不等于', types: ['string', 'number', 'date', 'boolean'] },
                { value: '>', text: '大于', types: ['number', 'date'] },
                { value: '>=', text: '大于等于', types: ['number', 'date'] },
                { value: '<', text: '小于', types: ['number', 'date'] },
                { value: '<=', text: '小于等于', types: ['number', 'date'] },
                { value: 'LIKE', text: '包含', types: ['string'] },
                { value: 'NOT LIKE', text: '不包含', types: ['string'] },
                { value: 'IN', text: '在列表中', types: ['string', 'number'] },
                { value: 'NOT IN', text: '不在列表中', types: ['string', 'number'] },
                { value: 'BETWEEN', text: '在范围内', types: ['number', 'date'] },
                { value: 'NOT BETWEEN', text: '不在范围内', types: ['number', 'date'] },
                { value: 'IS NULL', text: '为空', types: ['all'] },
                { value: 'IS NOT NULL', text: '不为空', types: ['all'] }
            ]
        };
    },

    computed: {
        // 当前条件组的逻辑运算符
        logic: {
            get() {
                return this.value.logic || 'AND';
            },
            set(value) {
                this.$emit('input', { ...this.value, logic: value });
            }
        },

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
        // 添加条件
        addCondition() {
            const conditions = [...this.value.conditions];
            conditions.push({
                tableId: '',
                column: '',
                operator: '=',
                value: null,
                valueType: 'string'
            });
            this.$emit('input', { ...this.value, conditions });
        },

        // 添加条件组
        addGroup() {
            const conditions = [...this.value.conditions];
            conditions.push({
                logic: 'AND',
                conditions: []
            });
            this.$emit('input', { ...this.value, conditions });
        },

        // 移除条件或条件组
        removeItem(index) {
            const conditions = [...this.value.conditions];
            conditions.splice(index, 1);
            this.$emit('input', { ...this.value, conditions });
        },

        // 更新条件
        updateCondition(index, updates) {
            const conditions = [...this.value.conditions];
            conditions[index] = { ...conditions[index], ...updates };
            
            // 如果更改了列，自动设置值类型
            if (updates.column) {
                const column = this.availableColumns.find(col => 
                    col.id === `${conditions[index].tableId}.${updates.column}`
                );
                if (column) {
                    conditions[index].valueType = this.getColumnType(column);
                    conditions[index].value = this.getDefaultValue(conditions[index].valueType);
                }
            }
            
            this.$emit('input', { ...this.value, conditions });
        },

        // 更新条件组
        updateGroup(index, value) {
            const conditions = [...this.value.conditions];
            conditions[index] = value;
            this.$emit('input', { ...this.value, conditions });
        },

        // 获取列类型
        getColumnType(column) {
            const typeMap = {
                'INTEGER': 'number',
                'BIGINT': 'number',
                'DECIMAL': 'number',
                'NUMERIC': 'number',
                'FLOAT': 'number',
                'DOUBLE': 'number',
                'DATE': 'date',
                'TIMESTAMP': 'date',
                'BOOLEAN': 'boolean',
                'VARCHAR': 'string',
                'CHAR': 'string',
                'TEXT': 'string'
            };
            return typeMap[column.type] || 'string';
        },

        // 获取默认值
        getDefaultValue(type) {
            switch (type) {
                case 'number':
                    return 0;
                case 'boolean':
                    return false;
                case 'date':
                    return null;
                case 'string':
                default:
                    return '';
            }
        },

        // 获取针对特定列类型的可用操作符
        getAvailableOperators(column) {
            if (!column) return this.operators;
            const type = this.getColumnType(column);
            return this.operators.filter(op => 
                op.types.includes(type) || op.types.includes('all')
            );
        },

        // 获取操作符的值编辑器类型
        getOperatorValueType(operator) {
            switch (operator) {
                case 'IN':
                case 'NOT IN':
                    return 'list';
                case 'BETWEEN':
                case 'NOT BETWEEN':
                    return 'range';
                case 'IS NULL':
                case 'IS NOT NULL':
                    return 'none';
                default:
                    return 'single';
            }
        },

        // 格式化显示值
        formatValue(condition) {
            if (!condition.value) return '-';
            
            switch (condition.valueType) {
                case 'date':
                    return UtilService.formatDateTime(condition.value);
                case 'boolean':
                    return condition.value ? '是' : '否';
                case 'list':
                    return Array.isArray(condition.value) 
                        ? condition.value.join(', ') 
                        : condition.value;
                default:
                    return condition.value.toString();
            }
        }
    },

    template: `
        <div class="condition-builder">
            <!-- 逻辑运算符选择 -->
            <div class="condition-logic">
                <a-radio-group 
                    v-model="logic"
                    button-style="solid"
                    :disabled="disabled"
                >
                    <a-radio-button value="AND">AND</a-radio-button>
                    <a-radio-button value="OR">OR</a-radio-button>
                </a-radio-group>
            </div>

            <!-- 条件列表 -->
            <div class="condition-list">
                <template v-for="(item, index) in value.conditions">
                    <!-- 条件组 -->
                    <div 
                        v-if="item.conditions"
                        :key="index"
                        class="condition-group"
                    >
                        <div class="group-header">
                            <span class="group-indicator">组</span>
                            <a-button 
                                type="link"
                                size="small"
                                @click="removeItem(index)"
                                :disabled="disabled"
                            >
                                <a-icon type="delete" />
                            </a-button>
                        </div>
                        
                        <!-- 递归渲染子条件组 -->
                        <condition-builder
                            v-model="item"
                            :tables="tables"
                            :disabled="disabled"
                            @input="val => updateGroup(index, val)"
                        />
                    </div>

                    <!-- 单个条件 -->
                    <div 
                        v-else
                        :key="index"
                        class="condition-item"
                    >
                        <!-- 表和列选择 -->
                        <div class="condition-column">
                            <a-select
                                v-model="item.tableId"
                                placeholder="选择表"
                                style="width: 120px"
                                :disabled="disabled"
                                @change="tableId => updateCondition(index, { tableId, column: '' })"
                            >
                                <a-select-option
                                    v-for="table in tables"
                                    :key="table.id"
                                    :value="table.id"
                                >
                                    {{ table.name }}
                                </a-select-option>
                            </a-select>

                            <a-select
                                v-model="item.column"
                                placeholder="选择列"
                                style="width: 120px"
                                :disabled="disabled || !item.tableId"
                                @change="column => updateCondition(index, { column })"
                            >
                                <a-select-option
                                    v-for="column in availableColumns.filter(c => c.tableId === item.tableId)"
                                    :key="column.name"
                                    :value="column.name"
                                >
                                    {{ column.name }}
                                </a-select-option>
                            </a-select>
                        </div>

                        <!-- 操作符选择 -->
                        <a-select
                            v-model="item.operator"
                            style="width: 120px"
                            :disabled="disabled || !item.column"
                            @change="operator => updateCondition(index, { operator })"
                        >
                            <a-select-option
                                v-for="op in getAvailableOperators(
                                    availableColumns.find(c => 
                                        c.tableId === item.tableId && c.name === item.column
                                    )
                                )"
                                :key="op.value"
                                :value="op.value"
                            >
                                {{ op.text }}
                            </a-select-option>
                        </a-select>

                        <!-- 值输入 -->
                        <div class="condition-value">
                            <!-- 单值输入 -->
                            <template v-if="getOperatorValueType(item.operator) === 'single'">
                                <a-input-number
                                    v-if="item.valueType === 'number'"
                                    v-model="item.value"
                                    style="width: 150px"
                                    :disabled="disabled"
                                    @change="value => updateCondition(index, { value })"
                                />
                                <a-date-picker
                                    v-else-if="item.valueType === 'date'"
                                    v-model="item.value"
                                    style="width: 150px"
                                    :disabled="disabled"
                                    @change="value => updateCondition(index, { value })"
                                />
                                <a-switch
                                    v-else-if="item.valueType === 'boolean'"
                                    v-model="item.value"
                                    :disabled="disabled"
                                    @change="value => updateCondition(index, { value })"
                                />
                                <a-input
                                    v-else
                                    v-model="item.value"
                                    style="width: 150px"
                                    :disabled="disabled"
                                    @change="e => updateCondition(index, { value: e.target.value })"
                                />
                            </template>

                            <!-- 列表值输入 -->
                            <template v-else-if="getOperatorValueType(item.operator) === 'list'">
                                <a-select
                                    v-model="item.value"
                                    mode="tags"
                                    style="width: 200px"
                                    :disabled="disabled"
                                    @change="value => updateCondition(index, { value })"
                                />
                            </template>

                            <!-- 范围值输入 -->
                            <template v-else-if="getOperatorValueType(item.operator) === 'range'">
                                <a-range-picker
                                    v-if="item.valueType === 'date'"
                                    v-model="item.value"
                                    style="width: 250px"
                                    :disabled="disabled"
                                    @change="value => updateCondition(index, { value })"
                                />
                                <div 
                                    v-else 
                                    class="range-input"
                                >
                                    <a-input-number
                                        v-model="item.value[0]"
                                        style="width: 100px"
                                        :disabled="disabled"
                                        @change="value => updateCondition(index, { 
                                            value: [value, item.value[1]] 
                                        })"
                                    />
                                    <span class="range-separator">-</span>
                                    <a-input-number
                                        v-model="item.value[1]"
                                        style="width: 100px"
                                        :disabled="disabled"
                                        @change="value => updateCondition(index, { 
                                            value: [item.value[0], value] 
                                        })"
                                    />
                                </div>
                            </template>
                        </div>

                        <!-- 删除按钮 -->
                        <a-button 
                            type="link"
                            size="small"
                            @click="removeItem(index)"
                            :disabled="disabled"
                        >
                            <a-icon type="delete" />
                        </a-button>
                    </div>
                </template>
            </div>

            <!-- 添加按钮 -->
            <div class="condition-actions">
                <a-button-group>
                    <a-button 
                        type="dashed"
                        @click="addCondition"
                        :disabled="disabled"
                    >
                        <a-icon type="plus" />
                        添加条件
                    </a-button>
                    <a-button
                        type="dashed"
                        @click="addGroup"
                        :disabled="disabled"
                    >
                        <a-icon type="folder-add" />
                        添加条件组
                    </a-button>
                </a-button-group>
            </div>
        </div>
    `
};

// 导入依赖
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('condition-builder', ConditionBuilder);