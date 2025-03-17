/**
 * 查询参数输入面板组件
 * 支持参数定义、验证和值输入
 */
const ParameterInput = {
    props: {
        // 原始SQL语句
        sql: {
            type: String,
            required: true
        },
        // 已保存的参数值
        value: {
            type: Object,
            default: () => ({})
        },
        // 禁用状态
        disabled: {
            type: Boolean,
            default: false
        },
        // 参数定义（可选，用于覆盖自动检测）
        parameterDefinitions: {
            type: Array,
            default: () => []
        }
    },

    data() {
        return {
            parameters: [],
            paramValues: {},
            validationErrors: {},
            isValid: false
        };
    },

    computed: {
        hasParameters() {
            return this.parameters.length > 0;
        },

        // 所有参数是否都已填写且有效
        isComplete() {
            return this.hasParameters &&
                this.parameters.every(param => 
                    this.paramValues[param.name] !== undefined &&
                    this.paramValues[param.name] !== '' &&
                    !this.validationErrors[param.name]
                );
        }
    },

    watch: {
        sql: {
            immediate: true,
            handler: 'detectParameters'
        },

        parameterDefinitions: {
            handler: 'updateParameterDefinitions',
            deep: true
        },

        value: {
            handler(newValue) {
                this.paramValues = { ...newValue };
                this.validateAll();
            },
            deep: true
        },

        paramValues: {
            handler(newValues) {
                if (this.hasParameters) {
                    this.validateAll();
                    this.$emit('input', newValues);
                    this.$emit('validation', {
                        isValid: this.isValid,
                        errors: this.validationErrors
                    });
                }
            },
            deep: true
        }
    },

    methods: {
        // 从SQL中检测参数
        detectParameters() {
            if (!this.sql) {
                this.parameters = [];
                return;
            }

            // 查找命名参数 :param_name
            const namedParams = Array.from(this.sql.matchAll(/:(\w+)/g))
                .map(match => ({
                    name: match[1],
                    type: 'string',
                    required: true
                }));

            // 查找占位符参数 ?
            const placeholderParams = Array.from(this.sql.matchAll(/\?/g))
                .map((_, index) => ({
                    name: `param${index + 1}`,
                    type: 'string',
                    required: true
                }));

            this.parameters = [...namedParams, ...placeholderParams];
            this.inferParameterTypes();
            this.initializeValues();
        },

        // 推断参数类型
        inferParameterTypes() {
            const sql = this.sql.toLowerCase();
            
            this.parameters.forEach(param => {
                // 查找参数在SQL中的上下文
                const context = this.findParameterContext(sql, param.name);
                
                if (context.includes('like')) {
                    param.type = 'string';
                } else if (context.includes('= true') || context.includes('= false')) {
                    param.type = 'boolean';
                } else if (context.includes('in (')) {
                    param.type = 'array';
                } else if (
                    context.includes('date') ||
                    context.includes('timestamp') ||
                    context.includes('time')
                ) {
                    param.type = 'datetime';
                } else if (
                    context.includes('number') ||
                    context.includes('int') ||
                    context.includes('decimal') ||
                    context.includes('numeric')
                ) {
                    param.type = 'number';
                }
            });
        },

        // 查找参数在SQL中的上下文
        findParameterContext(sql, paramName) {
            const paramIndex = sql.indexOf(`:${paramName}`);
            if (paramIndex === -1) return '';
            
            // 提取参数前后的SQL片段
            const start = Math.max(0, paramIndex - 50);
            const end = Math.min(sql.length, paramIndex + 50);
            return sql.substring(start, end);
        },

        // 使用自定义参数定义
        updateParameterDefinitions() {
            if (!this.parameterDefinitions.length) return;

            this.parameters = this.parameterDefinitions.map(def => ({
                name: def.name,
                type: def.type || 'string',
                required: def.required !== false,
                defaultValue: def.defaultValue,
                validation: def.validation,
                options: def.options
            }));

            this.initializeValues();
        },

        // 初始化参数值
        initializeValues() {
            const values = { ...this.paramValues };
            
            this.parameters.forEach(param => {
                if (values[param.name] === undefined) {
                    values[param.name] = param.defaultValue !== undefined ? 
                        param.defaultValue : this.getDefaultValueForType(param.type);
                }
            });

            this.paramValues = values;
        },

        // 获取类型的默认值
        getDefaultValueForType(type) {
            switch (type) {
                case 'number': return null;
                case 'boolean': return false;
                case 'array': return [];
                case 'datetime': return null;
                default: return '';
            }
        },

        // 验证所有参数
        validateAll() {
            let isValid = true;
            const errors = {};

            this.parameters.forEach(param => {
                const error = this.validateParameter(param, this.paramValues[param.name]);
                if (error) {
                    errors[param.name] = error;
                    isValid = false;
                }
            });

            this.validationErrors = errors;
            this.isValid = isValid;
        },

        // 验证单个参数
        validateParameter(param, value) {
            if (param.required && (value === undefined || value === '')) {
                return '此参数是必填的';
            }

            if (value !== null && value !== undefined && value !== '') {
                switch (param.type) {
                    case 'number':
                        if (isNaN(value)) {
                            return '请输入有效的数字';
                        }
                        break;
                    case 'datetime':
                        if (!this.isValidDate(value)) {
                            return '请输入有效的日期时间';
                        }
                        break;
                    case 'array':
                        if (!Array.isArray(value)) {
                            return '请输入有效的数组';
                        }
                        break;
                }
            }

            if (param.validation) {
                try {
                    const result = param.validation(value);
                    if (result !== true) {
                        return result;
                    }
                } catch (error) {
                    return error.message;
                }
            }

            return null;
        },

        // 检查日期有效性
        isValidDate(value) {
            const date = new Date(value);
            return date instanceof Date && !isNaN(date);
        },

        // 处理参数值变化
        handleValueChange(paramName, value) {
            this.paramValues = {
                ...this.paramValues,
                [paramName]: value
            };
        },

        // 重置所有参数值
        resetValues() {
            this.initializeValues();
        },

        // 清空所有参数值
        clearValues() {
            const values = {};
            this.parameters.forEach(param => {
                values[param.name] = this.getDefaultValueForType(param.type);
            });
            this.paramValues = values;
        }
    },

    template: `
        <div class="parameter-input" :class="{ disabled }">
            <template v-if="hasParameters">
                <div class="parameter-list">
                    <div 
                        v-for="param in parameters" 
                        :key="param.name"
                        class="parameter-item"
                    >
                        <div class="param-label">
                            {{ param.name }}
                            <span v-if="param.required" class="required">*</span>
                        </div>

                        <!-- 字符串输入 -->
                        <a-input
                            v-if="param.type === 'string'"
                            v-model="paramValues[param.name]"
                            :placeholder="'请输入' + param.name"
                            :disabled="disabled"
                            @change="e => handleValueChange(param.name, e.target.value)"
                        />

                        <!-- 数字输入 -->
                        <a-input-number
                            v-else-if="param.type === 'number'"
                            v-model="paramValues[param.name]"
                            :placeholder="'请输入' + param.name"
                            :disabled="disabled"
                            style="width: 100%"
                            @change="value => handleValueChange(param.name, value)"
                        />

                        <!-- 布尔输入 -->
                        <a-switch
                            v-else-if="param.type === 'boolean'"
                            v-model="paramValues[param.name]"
                            :disabled="disabled"
                            @change="value => handleValueChange(param.name, value)"
                        />

                        <!-- 日期时间输入 -->
                        <a-date-picker
                            v-else-if="param.type === 'datetime'"
                            v-model="paramValues[param.name]"
                            :show-time="true"
                            :disabled="disabled"
                            style="width: 100%"
                            @change="value => handleValueChange(param.name, value)"
                        />

                        <!-- 数组输入 -->
                        <a-select
                            v-else-if="param.type === 'array'"
                            v-model="paramValues[param.name]"
                            mode="tags"
                            :disabled="disabled"
                            style="width: 100%"
                            :options="param.options"
                            @change="value => handleValueChange(param.name, value)"
                        />

                        <!-- 验证错误提示 -->
                        <div 
                            v-if="validationErrors[param.name]"
                            class="validation-error"
                        >
                            {{ validationErrors[param.name] }}
                        </div>
                    </div>
                </div>

                <div class="parameter-actions">
                    <a-space>
                        <a-button
                            type="default"
                            :disabled="disabled"
                            @click="resetValues"
                        >
                            重置
                        </a-button>
                        <a-button
                            type="default"
                            :disabled="disabled"
                            @click="clearValues"
                        >
                            清空
                        </a-button>
                    </a-space>
                </div>
            </template>

            <a-empty 
                v-else 
                description="当前查询无需参数"
            />
        </div>
    `
};

// 导入依赖
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('parameter-input', ParameterInput);