/**
 * 数据源表单组件
 * 用于添加和编辑数据源
 */
const DataSourceForm = {
    props: {
        // 编辑模式时的数据源ID
        dataSourceId: {
            type: [Number, String],
            default: null
        },
        // 是否为只读模式（查看详情）
        readonly: {
            type: Boolean,
            default: false
        }
    },
    data() {
        return {
            form: this.$form.createForm(this),
            loading: false,
            testingConnection: false,
            supportedTypes: [],
            dataSource: {
                name: '',
                type: null,
                host: '',
                port: null,
                databaseName: '',
                username: '',
                password: '',
                connectionProperties: {},
                active: true,
                description: ''
            },
            // 连接属性的键值对列表
            connectionPropertiesList: []
        };
    },
    computed: {
        isEdit() {
            return !!this.dataSourceId;
        },
        title() {
            if (this.readonly) return '查看数据源';
            return this.isEdit ? '编辑数据源' : '添加数据源';
        }
    },
    mounted() {
        this.fetchSupportedTypes();
        
        if (this.isEdit) {
            this.fetchDataSource();
        }
    },
    methods: {
        fetchSupportedTypes() {
            axios.get('/datasources/types')
                .then(response => {
                    this.supportedTypes = response.data;
                })
                .catch(error => {
                    console.error('获取数据源类型失败:', error);
                    this.$message.error('获取数据源类型失败');
                });
        },
        fetchDataSource() {
            this.loading = true;
            
            axios.get(`/datasources/${this.dataSourceId}`)
                .then(response => {
                    this.dataSource = response.data;
                    
                    // 将连接属性转换为键值对列表
                    this.connectionPropertiesList = Object.entries(this.dataSource.connectionProperties || {})
                        .map(([key, value]) => ({ key, value }));
                    
                    // 设置表单初始值
                    this.$nextTick(() => {
                        this.form.setFieldsValue({
                            name: this.dataSource.name,
                            type: this.dataSource.type,
                            host: this.dataSource.host,
                            port: this.dataSource.port,
                            databaseName: this.dataSource.databaseName,
                            username: this.dataSource.username,
                            active: this.dataSource.active,
                            description: this.dataSource.description
                        });
                    });
                })
                .catch(error => {
                    console.error('获取数据源详情失败:', error);
                    this.$message.error('获取数据源详情失败');
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        handleSubmit(e) {
            e.preventDefault();
            
            this.form.validateFields((err, values) => {
                if (err) return;
                
                // 构建连接属性对象
                const connectionProperties = {};
                this.connectionPropertiesList.forEach(item => {
                    if (item.key && item.value !== undefined) {
                        connectionProperties[item.key] = item.value;
                    }
                });
                
                // 构建请求数据
                const data = {
                    ...values,
                    connectionProperties
                };
                
                // 如果是编辑模式，保留ID
                if (this.isEdit) {
                    data.id = this.dataSourceId;
                }
                
                this.loading = true;
                
                const request = this.isEdit
                    ? axios.put(`/datasources/${this.dataSourceId}`, data)
                    : axios.post('/datasources', data);
                
                request
                    .then(() => {
                        this.$message.success(`${this.isEdit ? '更新' : '创建'}数据源成功`);
                        this.$router.push('/datasource/list');
                    })
                    .catch(error => {
                        console.error(`${this.isEdit ? '更新' : '创建'}数据源失败:`, error);
                        this.$message.error(`${this.isEdit ? '更新' : '创建'}数据源失败`);
                    })
                    .finally(() => {
                        this.loading = false;
                    });
            });
        },
        handleTestConnection() {
            this.form.validateFields((err, values) => {
                if (err) return;
                
                // 构建连接属性对象
                const connectionProperties = {};
                this.connectionPropertiesList.forEach(item => {
                    if (item.key && item.value !== undefined) {
                        connectionProperties[item.key] = item.value;
                    }
                });
                
                // 构建请求数据
                const data = {
                    ...values,
                    connectionProperties
                };
                
                this.testingConnection = true;
                
                axios.post('/datasources/test-connection', data)
                    .then(response => {
                        if (response.data.success) {
                            this.$message.success('连接成功');
                        } else {
                            this.$message.error(`连接失败: ${response.data.message}`);
                        }
                    })
                    .catch(error => {
                        console.error('测试连接失败:', error);
                        this.$message.error('测试连接失败');
                    })
                    .finally(() => {
                        this.testingConnection = false;
                    });
            });
        },
        handleCancel() {
            this.$router.push('/datasource/list');
        },
        addConnectionProperty() {
            this.connectionPropertiesList.push({ key: '', value: '' });
        },
        removeConnectionProperty(index) {
            this.connectionPropertiesList.splice(index, 1);
        },
        handleTypeChange(value) {
            // 根据数据源类型设置默认端口
            switch (value) {
                case 'MYSQL':
                    this.form.setFieldsValue({ port: 3306 });
                    break;
                case 'POSTGRESQL':
                    this.form.setFieldsValue({ port: 5432 });
                    break;
                case 'ORACLE':
                    this.form.setFieldsValue({ port: 1521 });
                    break;
                case 'SQL_SERVER':
                    this.form.setFieldsValue({ port: 1433 });
                    break;
                case 'DB2':
                    this.form.setFieldsValue({ port: 50000 });
                    break;
                default:
                    break;
            }
        }
    },
    template: `
        <div class="datasource-form-container">
            <div class="page-header">
                <h1>{{ title }}</h1>
            </div>
            
            <a-spin :spinning="loading">
                <a-form :form="form" @submit="handleSubmit" layout="vertical">
                    <a-row :gutter="16">
                        <a-col :span="12">
                            <a-form-item label="数据源名称" required>
                                <a-input
                                    v-decorator="[
                                        'name',
                                        {
                                            rules: [{ required: true, message: '请输入数据源名称' }],
                                            initialValue: dataSource.name
                                        }
                                    ]"
                                    placeholder="请输入数据源名称"
                                    :disabled="readonly"
                                />
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="数据源类型" required>
                                <a-select
                                    v-decorator="[
                                        'type',
                                        {
                                            rules: [{ required: true, message: '请选择数据源类型' }],
                                            initialValue: dataSource.type
                                        }
                                    ]"
                                    placeholder="请选择数据源类型"
                                    :disabled="readonly"
                                    @change="handleTypeChange"
                                >
                                    <a-select-option v-for="type in supportedTypes" :key="type" :value="type">
                                        {{ type }}
                                    </a-select-option>
                                </a-select>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    
                    <a-row :gutter="16">
                        <a-col :span="12">
                            <a-form-item label="主机地址" required>
                                <a-input
                                    v-decorator="[
                                        'host',
                                        {
                                            rules: [{ required: true, message: '请输入主机地址' }],
                                            initialValue: dataSource.host
                                        }
                                    ]"
                                    placeholder="请输入主机地址"
                                    :disabled="readonly"
                                />
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="端口" required>
                                <a-input-number
                                    v-decorator="[
                                        'port',
                                        {
                                            rules: [{ required: true, message: '请输入端口号' }],
                                            initialValue: dataSource.port
                                        }
                                    ]"
                                    :min="1"
                                    :max="65535"
                                    style="width: 100%"
                                    placeholder="请输入端口号"
                                    :disabled="readonly"
                                />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    
                    <a-row :gutter="16">
                        <a-col :span="12">
                            <a-form-item label="数据库名称" required>
                                <a-input
                                    v-decorator="[
                                        'databaseName',
                                        {
                                            rules: [{ required: true, message: '请输入数据库名称' }],
                                            initialValue: dataSource.databaseName
                                        }
                                    ]"
                                    placeholder="请输入数据库名称"
                                    :disabled="readonly"
                                />
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="用户名" required>
                                <a-input
                                    v-decorator="[
                                        'username',
                                        {
                                            rules: [{ required: true, message: '请输入用户名' }],
                                            initialValue: dataSource.username
                                        }
                                    ]"
                                    placeholder="请输入用户名"
                                    :disabled="readonly"
                                />
                            </a-form-item>
                        </a-col>
                    </a-row>
                    
                    <a-row :gutter="16">
                        <a-col :span="12">
                            <a-form-item label="密码" :required="!isEdit">
                                <a-input-password
                                    v-decorator="[
                                        'password',
                                        {
                                            rules: [{ required: !isEdit, message: '请输入密码' }],
                                            initialValue: ''
                                        }
                                    ]"
                                    placeholder="请输入密码"
                                    :disabled="readonly"
                                />
                                <div v-if="isEdit" class="form-help-text">
                                    如不修改密码，请留空
                                </div>
                            </a-form-item>
                        </a-col>
                        <a-col :span="12">
                            <a-form-item label="状态">
                                <a-switch
                                    v-decorator="[
                                        'active',
                                        {
                                            valuePropName: 'checked',
                                            initialValue: dataSource.active !== false
                                        }
                                    ]"
                                    :disabled="readonly"
                                />
                                <span class="status-text">{{ form.getFieldValue('active') ? '激活' : '禁用' }}</span>
                            </a-form-item>
                        </a-col>
                    </a-row>
                    
                    <a-form-item label="描述">
                        <a-textarea
                            v-decorator="[
                                'description',
                                {
                                    initialValue: dataSource.description
                                }
                            ]"
                            :rows="4"
                            placeholder="请输入描述信息"
                            :disabled="readonly"
                        />
                    </a-form-item>
                    
                    <a-form-item label="连接属性">
                        <div v-for="(prop, index) in connectionPropertiesList" :key="index" class="connection-property-item">
                            <a-row :gutter="8">
                                <a-col :span="10">
                                    <a-input
                                        v-model="prop.key"
                                        placeholder="属性名"
                                        :disabled="readonly"
                                    />
                                </a-col>
                                <a-col :span="10">
                                    <a-input
                                        v-model="prop.value"
                                        placeholder="属性值"
                                        :disabled="readonly"
                                    />
                                </a-col>
                                <a-col :span="4" v-if="!readonly">
                                    <a-button type="danger" icon="delete" @click="removeConnectionProperty(index)" />
                                </a-col>
                            </a-row>
                        </div>
                        <a-button v-if="!readonly" type="dashed" style="width: 100%; margin-top: 8px" @click="addConnectionProperty">
                            <a-icon type="plus" /> 添加连接属性
                        </a-button>
                    </a-form-item>
                    
                    <a-form-item>
                        <a-button v-if="!readonly" type="primary" html-type="submit" :loading="loading">
                            {{ isEdit ? '更新' : '创建' }}
                        </a-button>
                        <a-button v-if="!readonly" :loading="testingConnection" style="margin-left: 8px" @click="handleTestConnection">
                            测试连接
                        </a-button>
                        <a-button style="margin-left: 8px" @click="handleCancel">
                            {{ readonly ? '返回' : '取消' }}
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-spin>
        </div>
    `
};

// 注册组件
Vue.component('datasource-form', DataSourceForm);
