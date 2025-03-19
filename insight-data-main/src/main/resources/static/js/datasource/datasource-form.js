/**
 * 数据源表单组件
 * 用于创建和编辑数据源
 */
const DataSourceForm = {
    props: {
        // 编辑模式时的数据源ID
        dataSourceId: {
            type: String,
            default: null
        }
    },

    data() {
        return {
            loading: false,
            saving: false,
            testing: false,
            formData: {
                name: '',
                type: undefined,
                host: '',
                port: undefined,
                databaseName: '',
                username: '',
                password: '',
                description: '',
                connectionProperties: {},
                active: true,
                tags: []
            },
            supportedTypes: [],
            // 数据源类型对应的默认端口
            defaultPorts: {
                MYSQL: 3306,
                POSTGRESQL: 5432,
                DB2: 50000,
                ORACLE: 1521,
                SQLSERVER: 1433
            },
            // 数据源类型对应的连接属性说明
            connectionPropertiesHelp: {
                MYSQL: '示例：{"useSSL": false, "serverTimezone": "UTC"}',
                POSTGRESQL: '示例：{"ssl": true, "sslmode": "verify-full"}',
                DB2: '示例：{"currentSchema": "myschema", "retrieveMessagesFromServerOnGetMessage": true}',
                ORACLE: '示例：{"oracle.jdbc.ReadTimeout": 30000}',
                SQLSERVER: '示例：{"encrypt": true, "trustServerCertificate": false}'
            },
            rules: {
                name: [
                    { required: true, message: '请输入数据源名称', trigger: 'blur' },
                    { min: 2, max: 50, message: '长度在 2 到 50 个字符之间', trigger: 'blur' },
                    { validator: this.validateDuplicateName, trigger: 'blur' }
                ],
                type: [
                    { required: true, message: '请选择数据源类型', trigger: 'change' }
                ],
                host: [
                    { required: true, message: '请输入主机地址', trigger: 'blur' },
                    { validator: this.validateHost, trigger: 'blur' }
                ],
                port: [
                    { required: true, message: '请输入端口号', trigger: 'blur' },
                    { type: 'number', message: '端口号必须为数字', trigger: 'blur' },
                    { validator: this.validatePort, trigger: 'blur' }
                ],
                databaseName: [
                    { required: true, message: '请输入数据库名称', trigger: 'blur' },
                    { pattern: /^[a-zA-Z0-9_]+$/, message: '数据库名称只能包含字母、数字和下划线', trigger: 'blur' }
                ],
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ],
                connectionProperties: [
                    { validator: this.validateConnectionProperties, trigger: 'blur' }
                ]
            }
        };
    },

    computed: {
        isEdit() {
            return !!this.dataSourceId;
        },
        title() {
            return this.isEdit ? '编辑数据源' : '创建数据源';
        },
        connectionPropertiesPlaceholder() {
            return this.formData.type ? this.connectionPropertiesHelp[this.formData.type] : '请输入连接属性（JSON格式）';
        }
    },

    created() {
        this.debouncedTestConnection = UtilService.debounce(this.handleTest, 1000);
        this.fetchSupportedTypes();
        if (this.isEdit) {
            this.fetchDataSource();
        }
    },

    methods: {
        async fetchSupportedTypes() {
            try {
                const response = await DataSourceService.getSupportedTypes();
                this.supportedTypes = response.data;
            } catch (error) {
                console.error('获取数据源类型失败:', error);
                this.$message.error('获取数据源类型失败');
            }
        },

        async fetchDataSource() {
            this.loading = true;
            try {
                const response = await DataSourceService.getDataSource(this.dataSourceId);
                const { password, ...data } = response.data;
                if (typeof data.connectionProperties === 'string') {
                    try {
                        data.connectionProperties = JSON.parse(data.connectionProperties);
                    } catch (e) {
                        data.connectionProperties = {};
                    }
                }
                this.formData = { ...this.formData, ...data };
            } catch (error) {
                console.error('获取数据源信息失败:', error);
                this.$message.error('获取数据源信息失败');
            } finally {
                this.loading = false;
            }
        },

        async validateDuplicateName(rule, value, callback) {
            if (!value) {
                callback();
                return;
            }

            try {
                const response = await DataSourceService.checkNameExists(value, this.dataSourceId);
                if (response.data.exists) {
                    callback(new Error('数据源名称已存在'));
                } else {
                    callback();
                }
            } catch (error) {
                console.error('检查数据源名称失败:', error);
                callback();
            }
        },

        validateHost(rule, value, callback) {
            const hostPattern = /^[a-zA-Z0-9][a-zA-Z0-9-._]+[a-zA-Z0-9]$/;
            const ipPattern = /^(\d{1,3}\.){3}\d{1,3}$/;
            
            if (value === 'localhost') {
                callback();
                return;
            }
            
            if (!hostPattern.test(value) && !ipPattern.test(value)) {
                callback(new Error('主机地址格式不正确'));
                return;
            }
            
            if (ipPattern.test(value)) {
                const parts = value.split('.');
                const isValid = parts.every(part => {
                    const num = parseInt(part, 10);
                    return num >= 0 && num <= 255;
                });
                if (!isValid) {
                    callback(new Error('IP地址格式不正确'));
                    return;
                }
            }
            
            callback();
        },

        validatePort(rule, value, callback) {
            if (value < 1 || value > 65535) {
                callback(new Error('端口号必须在 1-65535 之间'));
                return;
            }
            callback();
        },

        validateConnectionProperties(rule, value, callback) {
            if (!value || value === '') {
                callback();
                return;
            }

            if (typeof value === 'string') {
                try {
                    JSON.parse(value);
                    callback();
                } catch (e) {
                    callback(new Error('连接属性必须是有效的JSON格式'));
                }
            } else if (typeof value === 'object') {
                callback();
            } else {
                callback(new Error('连接属性格式不正确'));
            }
        },

        handleTypeChange(value) {
            // 设置默认端口
            if (this.defaultPorts[value] && !this.formData.port) {
                this.formData.port = this.defaultPorts[value];
            }
        },

        async handleTest() {
            try {
                await this.$refs.form.validate();
            } catch (error) {
                return;
            }

            this.testing = true;
            try {
                const testData = UtilService.deepClone(this.formData);
                if (typeof testData.connectionProperties === 'string') {
                    try {
                        testData.connectionProperties = JSON.parse(testData.connectionProperties);
                    } catch (e) {
                        testData.connectionProperties = {};
                    }
                }

                let response;
                if (this.isEdit) {
                    response = await DataSourceService.testConnection(this.dataSourceId);
                } else {
                    response = await DataSourceService.testNewConnection(testData);
                }

                if (response.data.success) {
                    this.$notification.success({
                        message: '连接测试成功',
                        description: `成功连接到 ${response.data.databaseVersion}`,
                        duration: 4
                    });
                } else {
                    this.$notification.error({
                        message: '连接测试失败',
                        description: response.data.message,
                        duration: 4
                    });
                }
            } catch (error) {
                console.error('连接测试失败:', error);
                this.$notification.error({
                    message: '连接测试失败',
                    description: error.response?.data?.message || '请检查连接信息是否正确',
                    duration: 4
                });
            } finally {
                this.testing = false;
            }
        },

        async handleSubmit() {
            try {
                await this.$refs.form.validate();
            } catch (error) {
                return;
            }

            this.saving = true;
            try {
                const submitData = UtilService.deepClone(this.formData);
                if (typeof submitData.connectionProperties === 'string') {
                    try {
                        submitData.connectionProperties = JSON.parse(submitData.connectionProperties);
                    } catch (e) {
                        submitData.connectionProperties = {};
                    }
                }

                if (this.isEdit) {
                    await DataSourceService.updateDataSource(this.dataSourceId, submitData);
                    this.$message.success('数据源更新成功');
                } else {
                    await DataSourceService.createDataSource(submitData);
                    this.$message.success('数据源创建成功');
                }
                this.$router.push('/datasource/list');
            } catch (error) {
                console.error('保存数据源失败:', error);
                this.$message.error(error.response?.data?.message || '保存数据源失败');
            } finally {
                this.saving = false;
            }
        },

        handleCancel() {
            this.$router.push('/datasource/list');
        }
    },

    template: `
        <div class="datasource-form-container">
            <div class="page-header">
                <h1>{{ title }}</h1>
            </div>

            <a-spin :spinning="loading">
                <a-form 
                    ref="form"
                    :model="formData"
                    :rules="rules"
                    :label-col="{ span: 4 }"
                    :wrapper-col="{ span: 16 }"
                >
                    <a-form-item label="数据源名称" name="name">
                        <a-input
                            v-model="formData.name"
                            placeholder="请输入数据源名称"
                            :maxLength="50"
                            :disabled="loading"
                            allow-clear
                        >
                            <a-tooltip slot="suffix" title="数据源名称用于标识和区分不同的数据源">
                                <a-icon type="info-circle" style="color: rgba(0,0,0,.45)" />
                            </a-tooltip>
                        </a-input>
                    </a-form-item>

                    <a-form-item label="数据源类型" name="type">
                        <a-select
                            v-model="formData.type"
                            placeholder="请选择数据源类型"
                            :disabled="loading || isEdit"
                            @change="handleTypeChange"
                        >
                            <a-select-option 
                                v-for="type in supportedTypes"
                                :key="type"
                                :value="type"
                            >
                                {{ type }}
                            </a-select-option>
                        </a-select>
                    </a-form-item>

                    <a-form-item label="主机地址" name="host">
                        <a-input
                            v-model="formData.host"
                            placeholder="请输入主机地址"
                            :disabled="loading"
                            allow-clear
                        >
                            <a-tooltip slot="suffix" title="支持域名或IP地址">
                                <a-icon type="info-circle" style="color: rgba(0,0,0,.45)" />
                            </a-tooltip>
                        </a-input>
                    </a-form-item>

                    <a-form-item label="端口号" name="port">
                        <a-input-number
                            v-model="formData.port"
                            placeholder="请输入端口号"
                            :min="1"
                            :max="65535"
                            :disabled="loading"
                            style="width: 150px"
                        />
                    </a-form-item>

                    <a-form-item label="数据库名称" name="databaseName">
                        <a-input
                            v-model="formData.databaseName"
                            placeholder="请输入数据库名称"
                            :disabled="loading"
                            allow-clear
                        />
                    </a-form-item>

                    <a-form-item label="用户名" name="username">
                        <a-input
                            v-model="formData.username"
                            placeholder="请输入用户名"
                            :disabled="loading"
                            allow-clear
                        />
                    </a-form-item>

                    <a-form-item label="密码" name="password">
                        <a-input-password
                            v-model="formData.password"
                            placeholder="请输入密码"
                            :disabled="loading"
                            allow-clear
                        />
                    </a-form-item>

                    <a-form-item label="连接属性" name="connectionProperties">
                        <a-textarea
                            v-model="formData.connectionProperties"
                            :placeholder="connectionPropertiesPlaceholder"
                            :rows="4"
                            :disabled="loading"
                        />
                    </a-form-item>

                    <a-form-item label="描述">
                        <a-textarea
                            v-model="formData.description"
                            placeholder="请输入数据源描述"
                            :rows="4"
                            :disabled="loading"
                        />
                    </a-form-item>

                    <a-form-item label="标签">
                        <a-select
                            v-model="formData.tags"
                            mode="tags"
                            placeholder="请输入标签"
                            :disabled="loading"
                        />
                    </a-form-item>

                    <a-form-item label="状态">
                        <a-switch
                            v-model="formData.active"
                            :disabled="loading"
                            checked-children="激活"
                            un-checked-children="禁用"
                        />
                    </a-form-item>

                    <a-form-item :wrapper-col="{ span: 16, offset: 4 }">
                        <a-space>
                            <a-button
                                type="primary"
                                @click="handleSubmit"
                                :loading="saving"
                            >
                                {{ isEdit ? '更新' : '创建' }}
                            </a-button>
                            <a-button
                                type="default"
                                @click="handleTest"
                                :loading="testing"
                            >
                                测试连接
                            </a-button>
                            <a-button @click="handleCancel">
                                取消
                            </a-button>
                        </a-space>
                    </a-form-item>
                </a-form>
            </a-spin>
        </div>
    `
};

// 注册组件
Vue.component('datasource-form', DataSourceForm);
