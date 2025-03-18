/**
 * 查询参数输入组件
 * 支持不同类型参数的输入和验证
 */
const ParameterInput = {
    props: {
        // SQL语句
        sql: {
            type: String,
            required: true
        },
        // 参数值
        value: {
            type: Object,
            default: () => ({})
        },
        // 禁用状态
        disabled: {
            type: Boolean,
            default: false
        }
    },

    data() {
        return {
            parameters: [],         // 参数列表
            validation: {},         // 验证状态
            inferredTypes: {},      // 推断的参数类型
            suggestions: {},        // 参数值建议
            showTypeInference: true // 是否显示类型推断
        };
    },

    computed: {
        // 是否所有必填参数都已填写
        isComplete() {
            return this.parameters.every(param => {
                const value = this.value[param.name];
                return value !== undefined && value !== '' && this.validation[param.name] !== false;
            });
        }
    },

    watch: {
        sql: {
            immediate: true,
            handler(newSql) {
                this.extractParameters(newSql);
            }
        },
        
        value: {
            deep: true,
            handler() {
                this.validateAll();
            }
        }
    },

    methods: {
        /**
         * 从SQL中提取参数
         * @param {string} sql SQL语句
         */
        extractParameters(sql) {
            const params = [];
            const paramRegex = /:(\w+)/g;
            let match;

            while ((match = paramRegex.exec(sql)) !== null) {
                const paramName = match[1];
                if (!params.find(p => p.name === paramName)) {
                    params.push({
                        name: paramName,
                        type: this.inferParameterType(sql, paramName),
                        required: true
                    });
                }
            }

            this.parameters = params;
            this.initValidation();
            this.inferTypes();
            this.loadSuggestions();
        },

        /**
         * 推断参数类型
         * @param {string} sql SQL语句
         * @param {string} paramName 参数名
         * @returns {string} 参数类型
         */
        inferParameterType(sql, paramName) {
            // 基于上下文推断参数类型
            const context = this.getParameterContext(sql, paramName);
            
            // 日期类型
            if (/date|time|timestamp/i.test(context)) {
                return 'date';
            }
            
            // 数字类型
            if (/number|int|decimal|float|double/i.test(context) ||
                /[<>]=?/.test(context)) {
                return 'number';
            }
            
            // 布尔类型
            if (/boolean|bit/i.test(context) ||
                /is\s+(:?\w+)/i.test(context)) {
                return 'boolean';
            }
            
            // 数组类型
            if (/in\s*\(.*:(\w+).*\)/i.test(context)) {
                return 'array';
            }
            
            // 默认为字符串类型
            return 'string';
        },

        /**
         * 获取参数上下文
         * @param {string} sql SQL语句
         * @param {string} paramName 参数名
         * @returns {string} 上下文
         */
        getParameterContext(sql, paramName) {
            const paramPosition = sql.indexOf(':' + paramName);
            if (paramPosition === -1) {
                return '';
            }

            // 获取参数前后的SQL片段
            const start = Math.max(0, paramPosition - 50);
            const end = Math.min(sql.length, paramPosition + 50);
            return sql.substring(start, end);
        },

        /**
         * 初始化验证状态
         */
        initValidation() {
            this.validation = {};
            this.parameters.forEach(param => {
                this.validation[param.name] = true;
            });
        },

        /**
         * 推断所有参数类型
         */
        async inferTypes() {
            if (!this.showTypeInference) return;

            try {
                const response = await QueryService.inferParameterTypes(
                    this.sql,
                    this.parameters.map(p => p.name)
                );
                this.inferredTypes = response.data;
                
                // 更新参数类型
                this.parameters.forEach(param => {
                    if (this.inferredTypes[param.name]) {
                        param.type = this.inferredTypes[param.name];
                    }
                });
            } catch (error) {
                console.error('参数类型推断失败:', error);
            }
        },

        /**
         * 加载参数值建议
         */
        async loadSuggestions() {
            for (const param of this.parameters) {
                try {
                    const response = await QueryService.getParameterSuggestions(
                        this.sql,
                        param.name
                    );
                    this.$set(this.suggestions, param.name, response.data);
                } catch (error) {
                    console.error(`加载参数 ${param.name} 的建议值失败:`, error);
                }
            }
        },

        /**
         * 验证所有参数
         */
        validateAll() {
            let isValid = true;
            this.parameters.forEach(param => {
                const value = this.value[param.name];
                const validationResult = this.validateParameter(param, value);
                this.$set(this.validation, param.name, validationResult);
                if (!validationResult) {
                    isValid = false;
                }
            });
            this.$emit('validation', isValid);
        },

        /**
         * 验证单个参数
         * @param {Object} param 参数定义
         * @param {*} value 参数值
         * @returns {boolean} 验证结果
         */
        validateParameter(param, value) {
            if (param.required && (value === undefined || value === '')) {
                return false;
            }

            switch (param.type) {
                case 'number':
                    return !isNaN(value);
                case 'date':
                    return !value || value instanceof Date || !isNaN(Date.parse(value));
                case 'boolean':
                    return typeof value === 'boolean' || value === undefined;
                case 'array':
                    return Array.isArray(value) || value === undefined;
                default:
                    return true;
            }
        },

        /**
         * 处理参数值变化
         * @param {string} paramName 参数名
         * @param {*} value 参数值
         */
        handleValueChange(paramName, value) {
            const newValue = { ...this.value, [paramName]: value };
            this.$emit('input', newValue);
            this.$emit('change', newValue);
        }
    },

    template: `
        <div class="parameter-input">
            <a-form layout="vertical">
                <a-form-item
                    v-for="param in parameters"
                    :key="param.name"
                    :label="param.name"
                    :validateStatus="validation[param.name] ? '' : 'error'"
                    :help="validation[param.name] ? '' : '请输入有效的' + param.type + '类型值'"
                >
                    <!-- 字符串类型 -->
                    <a-input
                        v-if="param.type === 'string'"
                        v-model="value[param.name]"
                        :placeholder="'请输入' + param.name"
                        :disabled="disabled"
                        @change="e => handleValueChange(param.name, e.target.value)"
                        :allowClear="true"
                    >
                        <a-select
                            v-if="suggestions[param.name]?.length"
                            slot="addonAfter"
                            style="width: 150px"
                            :value="value[param.name]"
                            @change="val => handleValueChange(param.name, val)"
                        >
                            <a-select-option
                                v-for="item in suggestions[param.name]"
                                :key="item.value"
                                :value="item.value"
                            >
                                {{ item.label }}
                            </a-select-option>
                        </a-select>
                    </a-input>

                    <!-- 数字类型 -->
                    <a-input-number
                        v-else-if="param.type === 'number'"
                        v-model="value[param.name]"
                        style="width: 100%"
                        :disabled="disabled"
                        @change="val => handleValueChange(param.name, val)"
                    />

                    <!-- 日期类型 -->
                    <a-date-picker
                        v-else-if="param.type === 'date'"
                        v-model="value[param.name]"
                        style="width: 100%"
                        :disabled="disabled"
                        :showTime="true"
                        format="YYYY-MM-DD HH:mm:ss"
                        @change="val => handleValueChange(param.name, val)"
                    />

                    <!-- 布尔类型 -->
                    <a-switch
                        v-else-if="param.type === 'boolean'"
                        v-model="value[param.name]"
                        :disabled="disabled"
                        @change="val => handleValueChange(param.name, val)"
                    />

                    <!-- 数组类型 -->
                    <a-select
                        v-else-if="param.type === 'array'"
                        v-model="value[param.name]"
                        mode="tags"
                        style="width: 100%"
                        :disabled="disabled"
                        @change="val => handleValueChange(param.name, val)"
                    >
                        <a-select-option
                            v-for="item in suggestions[param.name] || []"
                            :key="item.value"
                            :value="item.value"
                        >
                            {{ item.label }}
                        </a-select-option>
                    </a-select>
                </a-form-item>
            </a-form>
        </div>
    `
};

// 导入依赖
import QueryService from '../services/query-service.js';

// 注册组件
Vue.component('parameter-input', ParameterInput);