/**
 * 查询模板组件
 * 支持保存、管理和使用参数化查询模板
 */
const QueryTemplate = {
    props: {
        // 数据源ID
        dataSourceId: {
            type: String,
            required: true
        }
    },

    data() {
        return {
            loading: false,
            templates: [],
            selectedTemplate: null,
            // 模板表单
            form: {
                name: '',
                description: '',
                sql: '',
                parameters: [],
                tags: [],
                isPublic: false
            },
            // 模板参数
            parameterValues: {},
            // 对话框状态
            dialogs: {
                save: false,
                params: false
            },
            // 表单验证规则
            rules: {
                name: [
                    { required: true, message: '请输入模板名称', trigger: 'blur' },
                    { min: 2, max: 50, message: '长度在 2 到 50 个字符之间', trigger: 'blur' }
                ],
                sql: [
                    { required: true, message: '请输入SQL语句', trigger: 'blur' }
                ]
            }
        };
    },

    computed: {
        // 模板是否有参数
        hasParameters() {
            return this.form.parameters && this.form.parameters.length > 0;
        },
        
        // 参数是否都已填写
        isParametersComplete() {
            return !this.hasParameters || Object.keys(this.parameterValues).length === this.form.parameters.length;
        }
    },

    created() {
        this.loadTemplates();
    },

    methods: {
        // 加载查询模板列表
        async loadTemplates() {
            this.loading = true;
            try {
                const response = await QueryService.getQueryTemplates(this.dataSourceId);
                this.templates = response.data;
            } catch (error) {
                console.error('加载查询模板失败:', error);
                this.$message.error('加载查询模板失败');
            } finally {
                this.loading = false;
            }
        },

        // 保存查询模板
        async saveTemplate() {
            try {
                await this.$refs.templateForm.validate();
            } catch (error) {
                return;
            }

            try {
                const template = { ...this.form };
                // 解析SQL中的参数
                template.parameters = this.extractParameters(template.sql);
                
                if (template.id) {
                    await QueryService.updateQueryTemplate(template.id, template);
                    this.$message.success('更新模板成功');
                } else {
                    await QueryService.createQueryTemplate(template);
                    this.$message.success('创建模板成功');
                }
                
                this.dialogs.save = false;
                this.loadTemplates();
            } catch (error) {
                console.error('保存查询模板失败:', error);
                this.$message.error('保存查询模板失败');
            }
        },

        // 删除查询模板
        async deleteTemplate(template) {
            this.$confirm({
                title: '确认删除',
                content: '确定要删除模板 "' + template.name + '" 吗？',
                okText: '确认',
                okType: 'danger',
                cancelText: '取消',
                onOk: async () => {
                    try {
                        await QueryService.deleteQueryTemplate(template.id);
                        this.$message.success('删除模板成功');
                        this.loadTemplates();
                    } catch (error) {
                        console.error('删除查询模板失败:', error);
                        this.$message.error('删除查询模板失败');
                    }
                }
            });
        },

        // 使用模板
        async useTemplate(template) {
            this.selectedTemplate = template;
            
            if (template.parameters && template.parameters.length > 0) {
                this.parameterValues = {};
                this.dialogs.params = true;
            } else {
                this.applyTemplate(template);
            }
        },

        // 应用模板
        applyTemplate(template) {
            let sql = template.sql;
            
            // 替换参数
            if (template.parameters) {
                template.parameters.forEach(param => {
                    const value = this.parameterValues[param.name];
                    sql = sql.replace(new RegExp(':' + param.name, 'g'), value);
                });
            }
            
            this.$emit('use', {
                sql,
                name: template.name
            });
            
            this.dialogs.params = false;
        },

        // 从SQL中提取参数
        extractParameters(sql) {
            const parameters = [];
            const paramRegex = /:(\w+)/g;
            let match;
            
            while ((match = paramRegex.exec(sql)) !== null) {
                const paramName = match[1];
                if (!parameters.find(p => p.name === paramName)) {
                    parameters.push({
                        name: paramName,
                        type: 'string',
                        description: ''
                    });
                }
            }
            
            return parameters;
        },

        // 打开保存模板对话框
        showSaveDialog(sql) {
            this.form = {
                name: '',
                description: '',
                sql: sql || '',
                parameters: [],
                tags: [],
                isPublic: false
            };
            this.dialogs.save = true;
        },

        // 编辑模板
        editTemplate(template) {
            this.form = { ...template };
            this.dialogs.save = true;
        },

        // 重置表单
        resetForm() {
            this.$refs.templateForm.resetFields();
        }
    },

    template: `
        <div class="query-template">
            <!-- 模板列表 -->
            <div class="template-list">
                <a-list :dataSource="templates" :loading="loading" itemLayout="horizontal">
                    <a-list-item slot="renderItem" slot-scope="template">
                        <a-list-item-meta>
                            <template slot="title">
                                <span>{{ template.name }}</span>
                                <a-tag v-if="template.isPublic" color="blue">公共</a-tag>
                            </template>
                            <template slot="description">
                                {{ template.description }}
                                <div class="template-tags">
                                    <a-tag v-for="tag in template.tags" :key="tag">
                                        {{ tag }}
                                    </a-tag>
                                </div>
                            </template>
                        </a-list-item-meta>
                        <div class="template-actions">
                            <a-button-group size="small">
                                <a-tooltip title="使用模板">
                                    <a-button type="primary" @click="useTemplate(template)">
                                        <a-icon type="play-circle" />
                                    </a-button>
                                </a-tooltip>
                                <a-tooltip title="编辑">
                                    <a-button @click="editTemplate(template)">
                                        <a-icon type="edit" />
                                    </a-button>
                                </a-tooltip>
                                <a-tooltip title="删除">
                                    <a-button type="danger" @click="deleteTemplate(template)">
                                        <a-icon type="delete" />
                                    </a-button>
                                </a-tooltip>
                            </a-button-group>
                        </div>
                    </a-list-item>
                </a-list>
            </div>

            <!-- 保存模板对话框 -->
            <a-modal v-model="dialogs.save" title="保存查询模板" @ok="saveTemplate" @cancel="resetForm" :maskClosable="false">
                <a-form-model ref="templateForm" :model="form" :rules="rules" :label-col="{ span: 4 }" :wrapper-col="{ span: 18 }">
                    <a-form-model-item label="名称" prop="name">
                        <a-input v-model="form.name" placeholder="请输入模板名称" />
                    </a-form-model-item>
                    <a-form-model-item label="描述">
                        <a-textarea v-model="form.description" placeholder="请输入模板描述" :rows="3" />
                    </a-form-model-item>
                    <a-form-model-item label="SQL" prop="sql">
                        <a-textarea v-model="form.sql" placeholder="请输入SQL语句" :rows="6" />
                    </a-form-model-item>
                    <a-form-model-item label="标签">
                        <a-select v-model="form.tags" mode="tags" placeholder="请输入标签" style="width: 100%" />
                    </a-form-model-item>
                    <a-form-model-item label="权限">
                        <a-switch v-model="form.isPublic" checked-children="公共" un-checked-children="私有" />
                    </a-form-model-item>
                </a-form-model>
            </a-modal>

            <!-- 参数输入对话框 -->
            <a-modal v-model="dialogs.params" title="输入参数" @ok="applyTemplate(selectedTemplate)" :maskClosable="false">
                <template v-if="selectedTemplate">
                    <a-form-model layout="vertical">
                        <a-form-model-item v-for="param in selectedTemplate.parameters" :key="param.name" :label="param.description || param.name">
                            <a-input v-if="param.type === 'string'" v-model="parameterValues[param.name]" :placeholder="'请输入' + (param.description || param.name)" />
                            <a-input-number v-else-if="param.type === 'number'" v-model="parameterValues[param.name]" style="width: 100%" />
                            <a-date-picker v-else-if="param.type === 'date'" v-model="parameterValues[param.name]" style="width: 100%" />
                        </a-form-model-item>
                    </a-form-model>
                </template>
            </a-modal>
        </div>
    `
};

// 导入依赖
import QueryService from '../services/query-service.js';

// 注册组件
Vue.component('query-template', QueryTemplate);