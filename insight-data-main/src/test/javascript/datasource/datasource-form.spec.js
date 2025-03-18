import { mount } from '@vue/test-utils';
import DataSourceService from '../../../../insight-data-main/src/main/resources/static/js/services/datasource-service';
import UtilService from '../../../../insight-data-main/src/main/resources/static/js/services/util-service';

jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/datasource-service');
jest.mock('../../../../insight-data-main/src/main/resources/static/js/services/util-service');

describe('DataSourceForm', () => {
    let wrapper;
    const mockDataSource = {
        id: '1',
        name: 'Test DB',
        type: 'MYSQL',
        host: 'localhost',
        port: 3306,
        databaseName: 'testdb',
        username: 'test',
        password: '',
        active: true,
        tags: ['test', 'dev'],
        description: 'Test database',
        connectionProperties: { useSSL: false }
    };

    beforeEach(() => {
        jest.clearAllMocks();
        
        // Setup service mocks
        DataSourceService.getSupportedTypes.mockResolvedValue({
            data: ['MYSQL', 'POSTGRESQL']
        });
        
        DataSourceService.getDataSource.mockResolvedValue({
            data: mockDataSource
        });
        
        // Mount component
        wrapper = mount(DataSourceForm, {
            propsData: {
                dataSourceId: null
            },
            stubs: [
                'a-form',
                'a-form-item',
                'a-input',
                'a-input-password',
                'a-input-number',
                'a-select',
                'a-textarea',
                'a-switch',
                'a-button',
                'a-space'
            ],
            mocks: {
                $router: {
                    push: jest.fn()
                },
                $message: {
                    success: jest.fn(),
                    error: jest.fn()
                },
                $notification: {
                    success: jest.fn(),
                    error: jest.fn()
                }
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    describe('Component initialization', () => {
        it('should initialize with empty form in create mode', () => {
            expect(wrapper.vm.isEdit).toBe(false);
            expect(wrapper.vm.formData.name).toBe('');
            expect(wrapper.vm.formData.active).toBe(true);
        });

        it('should load data source in edit mode', async () => {
            wrapper = mount(DataSourceForm, {
                propsData: {
                    dataSourceId: '1'
                },
                stubs: ['a-form', 'a-form-item', 'a-input', 'a-select']
            });

            await wrapper.vm.$nextTick();
            expect(DataSourceService.getDataSource).toHaveBeenCalledWith('1');
            expect(wrapper.vm.formData.name).toBe(mockDataSource.name);
        });

        it('should load supported types on mount', async () => {
            await wrapper.vm.$nextTick();
            expect(DataSourceService.getSupportedTypes).toHaveBeenCalled();
            expect(wrapper.vm.supportedTypes).toEqual(['MYSQL', 'POSTGRESQL']);
        });
    });

    describe('Form validation', () => {
        it('should validate required fields', async () => {
            wrapper.vm.$refs.form = {
                validate: jest.fn().mockRejectedValue(new Error('Validation failed'))
            };

            await wrapper.vm.handleSubmit();
            expect(wrapper.vm.$refs.form.validate).toHaveBeenCalled();
            expect(DataSourceService.createDataSource).not.toHaveBeenCalled();
        });

        it('should validate host format', () => {
            const validHosts = ['localhost', 'db.example.com', '192.168.1.1'];
            const invalidHosts = ['invalid@host', 'host with spaces', '256.256.256.256'];

            validHosts.forEach(host => {
                wrapper.vm.validateHost({}, host, jest.fn());
            });

            invalidHosts.forEach(host => {
                const callback = jest.fn();
                wrapper.vm.validateHost({}, host, callback);
                expect(callback).toHaveBeenCalledWith(expect.any(Error));
            });
        });

        it('should validate port range', () => {
            const callback = jest.fn();
            wrapper.vm.validatePort({}, 0, callback);
            expect(callback).toHaveBeenCalledWith(expect.any(Error));

            wrapper.vm.validatePort({}, 65536, callback);
            expect(callback).toHaveBeenCalledWith(expect.any(Error));

            wrapper.vm.validatePort({}, 3306, callback);
            expect(callback).toHaveBeenCalledTimes(2);
        });
    });

    describe('Form submission', () => {
        it('should create new data source successfully', async () => {
            const formData = {
                name: 'New DB',
                type: 'MYSQL',
                host: 'localhost',
                port: 3306,
                databaseName: 'newdb',
                username: 'test',
                password: 'test',
                active: true
            };

            wrapper.setData({ formData });
            wrapper.vm.$refs.form = {
                validate: jest.fn().mockResolvedValue()
            };

            DataSourceService.createDataSource.mockResolvedValue({});

            await wrapper.vm.handleSubmit();
            expect(DataSourceService.createDataSource).toHaveBeenCalledWith(formData);
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('数据源创建成功');
            expect(wrapper.vm.$router.push).toHaveBeenCalledWith('/datasource/list');
        });

        it('should update existing data source successfully', async () => {
            wrapper = mount(DataSourceForm, {
                propsData: {
                    dataSourceId: '1'
                },
                stubs: ['a-form', 'a-form-item', 'a-input', 'a-select']
            });

            wrapper.vm.$refs.form = {
                validate: jest.fn().mockResolvedValue()
            };

            DataSourceService.updateDataSource.mockResolvedValue({});

            await wrapper.vm.handleSubmit();
            expect(DataSourceService.updateDataSource).toHaveBeenCalledWith('1', wrapper.vm.formData);
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('数据源更新成功');
        });

        it('should handle submission errors', async () => {
            const error = new Error('API Error');
            wrapper.vm.$refs.form = {
                validate: jest.fn().mockResolvedValue()
            };

            DataSourceService.createDataSource.mockRejectedValue(error);

            await wrapper.vm.handleSubmit();
            expect(wrapper.vm.$message.error).toHaveBeenCalledWith('保存数据源失败');
        });
    });

    describe('Connection testing', () => {
        it('should test connection successfully', async () => {
            wrapper.vm.$refs.form = {
                validate: jest.fn().mockResolvedValue()
            };

            DataSourceService.testNewConnection.mockResolvedValue({
                data: {
                    success: true,
                    databaseVersion: 'MySQL 8.0.26'
                }
            });

            await wrapper.vm.handleTest();
            expect(wrapper.vm.$notification.success).toHaveBeenCalled();
        });

        it('should handle connection test failure', async () => {
            wrapper.vm.$refs.form = {
                validate: jest.fn().mockResolvedValue()
            };

            DataSourceService.testNewConnection.mockRejectedValue({
                response: {
                    data: {
                        message: 'Connection refused'
                    }
                }
            });

            await wrapper.vm.handleTest();
            expect(wrapper.vm.$notification.error).toHaveBeenCalled();
        });
    });

    describe('Type change handling', () => {
        it('should set default port when type changes', () => {
            wrapper.vm.handleTypeChange('POSTGRESQL');
            expect(wrapper.vm.formData.port).toBe(5432);

            wrapper.vm.handleTypeChange('MYSQL');
            expect(wrapper.vm.formData.port).toBe(3306);
        });

        it('should not override existing port', () => {
            wrapper.setData({
                formData: {
                    port: 1234
                }
            });

            wrapper.vm.handleTypeChange('MYSQL');
            expect(wrapper.vm.formData.port).toBe(1234);
        });
    });
});