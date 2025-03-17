import { mount } from '@vue/test-utils';
import UtilService from '../../../main/resources/static/js/services/util-service';

describe('ParameterInput', () => {
    let wrapper;
    const mockSql = 'SELECT * FROM users WHERE id = :userId AND status IN (:statuses) AND created_at > :startDate';

    beforeEach(() => {
        wrapper = mount(ParameterInput, {
            propsData: {
                sql: mockSql,
                value: {}
            },
            stubs: [
                'a-input',
                'a-input-number',
                'a-switch',
                'a-date-picker',
                'a-select',
                'a-space',
                'a-button',
                'a-empty'
            ]
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    describe('Parameter detection', () => {
        it('should detect named parameters from SQL', () => {
            expect(wrapper.vm.parameters).toHaveLength(3);
            expect(wrapper.vm.parameters.map(p => p.name)).toEqual(['userId', 'statuses', 'startDate']);
        });

        it('should detect placeholder parameters', () => {
            wrapper.setProps({
                sql: 'SELECT * FROM users WHERE id = ? AND name = ?'
            });

            expect(wrapper.vm.parameters).toHaveLength(2);
            expect(wrapper.vm.parameters.map(p => p.name)).toEqual(['param1', 'param2']);
        });

        it('should infer parameter types correctly', () => {
            const sql = `
                SELECT * FROM users 
                WHERE id = :userId 
                AND active = :isActive 
                AND created_at > :startDate 
                AND status IN (:statuses)
                AND name LIKE :searchText
            `;

            wrapper.setProps({ sql });

            const types = wrapper.vm.parameters.reduce((acc, param) => {
                acc[param.name] = param.type;
                return acc;
            }, {});

            expect(types).toEqual({
                userId: 'number',
                isActive: 'boolean',
                startDate: 'datetime',
                statuses: 'array',
                searchText: 'string'
            });
        });
    });

    describe('Parameter definitions', () => {
        it('should use custom parameter definitions', async () => {
            const parameterDefinitions = [
                {
                    name: 'userId',
                    type: 'number',
                    required: true,
                    defaultValue: 1
                },
                {
                    name: 'status',
                    type: 'string',
                    required: true,
                    options: ['active', 'inactive']
                }
            ];

            await wrapper.setProps({ parameterDefinitions });
            
            expect(wrapper.vm.parameters).toHaveLength(2);
            expect(wrapper.vm.paramValues.userId).toBe(1);
        });

        it('should handle parameter validation', () => {
            const parameterDefinitions = [
                {
                    name: 'age',
                    type: 'number',
                    validation: value => value >= 0 || '年龄不能为负数'
                }
            ];

            wrapper.setProps({ parameterDefinitions });
            wrapper.vm.handleValueChange('age', -1);
            
            expect(wrapper.vm.validationErrors.age).toBe('年龄不能为负数');
            expect(wrapper.vm.isValid).toBe(false);
        });
    });

    describe('Value management', () => {
        it('should initialize default values', () => {
            expect(wrapper.vm.paramValues).toHaveProperty('userId');
            expect(wrapper.vm.paramValues).toHaveProperty('statuses');
            expect(wrapper.vm.paramValues).toHaveProperty('startDate');
        });

        it('should handle value changes', async () => {
            await wrapper.vm.handleValueChange('userId', 123);
            
            expect(wrapper.vm.paramValues.userId).toBe(123);
            expect(wrapper.emitted().input[0][0]).toEqual(
                expect.objectContaining({ userId: 123 })
            );
        });

        it('should validate required fields', () => {
            const param = {
                name: 'userId',
                type: 'number',
                required: true
            };

            const error = wrapper.vm.validateParameter(param, '');
            expect(error).toBe('此参数是必填的');
        });

        it('should validate number type', () => {
            const param = {
                name: 'userId',
                type: 'number'
            };

            expect(wrapper.vm.validateParameter(param, 'abc')).toBe('请输入有效的数字');
            expect(wrapper.vm.validateParameter(param, '123')).toBeNull();
        });

        it('should validate datetime type', () => {
            const param = {
                name: 'startDate',
                type: 'datetime'
            };

            expect(wrapper.vm.validateParameter(param, 'invalid-date')).toBe('请输入有效的日期时间');
            expect(wrapper.vm.validateParameter(param, '2025-03-17')).toBeNull();
        });
    });

    describe('Reset and clear', () => {
        it('should reset values to defaults', async () => {
            await wrapper.vm.handleValueChange('userId', 123);
            await wrapper.vm.resetValues();
            
            expect(wrapper.vm.paramValues.userId).toBe(wrapper.vm.getDefaultValueForType('number'));
        });

        it('should clear all values', async () => {
            await wrapper.vm.handleValueChange('userId', 123);
            await wrapper.vm.clearValues();
            
            expect(wrapper.vm.paramValues.userId).toBe(null);
            expect(wrapper.vm.paramValues.statuses).toEqual([]);
        });
    });

    describe('Component state', () => {
        it('should handle disabled state', async () => {
            await wrapper.setProps({ disabled: true });
            
            expect(wrapper.classes()).toContain('disabled');
            const inputs = wrapper.findAll('input');
            inputs.wrappers.forEach(input => {
                expect(input.attributes('disabled')).toBeTruthy();
            });
        });

        it('should track completion status', async () => {
            await wrapper.vm.handleValueChange('userId', 123);
            await wrapper.vm.handleValueChange('statuses', ['active']);
            await wrapper.vm.handleValueChange('startDate', '2025-03-17');
            
            expect(wrapper.vm.isComplete).toBe(true);
        });

        it('should emit validation state', async () => {
            await wrapper.vm.handleValueChange('userId', 'invalid');
            
            expect(wrapper.emitted().validation[0][0]).toEqual({
                isValid: false,
                errors: expect.any(Object)
            });
        });
    });
});