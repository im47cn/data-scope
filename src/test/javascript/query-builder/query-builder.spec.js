/**
 * 查询构建器组件测试
 */

import { mount } from '@vue/test-utils';
import QueryBuilderPage from '../../../main/resources/static/js/query-builder/query-builder-page.js';
import QueryWorkspace from '../../../main/resources/static/js/query-builder/query-workspace.js';
import QueryTemplate from '../../../main/resources/static/js/query-builder/query-template.js';
import ParameterInput from '../../../main/resources/static/js/query-builder/parameter-input.js';

describe('QueryBuilderPage', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(QueryBuilderPage, {
            propsData: {
                dataSourceId: 'test-ds-001'
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    it('should initialize with default state', () => {
        expect(wrapper.vm.queryConfig).toBeDefined();
        expect(wrapper.vm.executionStatus.running).toBe(false);
        expect(wrapper.vm.panels.parameters).toBe(false);
    });

    it('should detect parameters in SQL', async () => {
        const sql = 'SELECT * FROM users WHERE id = :userId AND status = :status';
        await wrapper.setData({ queryConfig: { sql } });
        
        expect(wrapper.vm.panels.parameters).toBe(true);
        const parameterInput = wrapper.findComponent(ParameterInput);
        expect(parameterInput.exists()).toBe(true);
        expect(parameterInput.props('sql')).toBe(sql);
    });

    it('should execute query when all parameters are valid', async () => {
        const mockExecuteQuery = jest.spyOn(wrapper.vm, 'executeQuery');
        const sql = 'SELECT * FROM users WHERE id = :userId';
        const parameters = { userId: 1 };

        await wrapper.setData({
            queryConfig: { sql, parameters },
            executionStatus: { running: false }
        });

        await wrapper.find('button.execute-button').trigger('click');
        
        expect(mockExecuteQuery).toHaveBeenCalled();
    });
});

describe('QueryWorkspace', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(QueryWorkspace, {
            propsData: {
                dataSourceId: 'test-ds-001',
                value: ''
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    it('should initialize CodeMirror editor', () => {
        expect(wrapper.vm.editor).toBeDefined();
        expect(wrapper.vm.editor.getValue()).toBe('');
    });

    it('should format SQL on demand', async () => {
        const unformattedSql = 'SELECT id,name FROM users WHERE status=1';
        const expectedSql = 'SELECT id, name\nFROM users\nWHERE status = 1';

        await wrapper.setProps({ value: unformattedSql });
        wrapper.vm.formatSql();

        expect(wrapper.vm.editor.getValue()).toBe(expectedSql);
    });

    it('should provide auto-completion suggestions', async () => {
        await wrapper.setProps({ value: 'SELECT * FROM ' });
        const suggestions = await wrapper.vm.showSuggestions();
        
        expect(Array.isArray(suggestions)).toBe(true);
        expect(suggestions.length).toBeGreaterThan(0);
        expect(suggestions[0]).toHaveProperty('text');
        expect(suggestions[0]).toHaveProperty('type');
    });
});

describe('QueryTemplate', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(QueryTemplate, {
            propsData: {
                dataSourceId: 'test-ds-001'
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    it('should load templates on mount', () => {
        expect(wrapper.vm.templates).toBeDefined();
        expect(Array.isArray(wrapper.vm.templates)).toBe(true);
    });

    it('should save template with parameters', async () => {
        const template = {
            name: 'Test Template',
            sql: 'SELECT * FROM users WHERE status = :status',
            description: 'Test template description',
            isPublic: true
        };

        await wrapper.vm.saveTemplate(template);
        
        expect(wrapper.emitted('save')).toBeTruthy();
        const savedTemplate = wrapper.emitted('save')[0][0];
        expect(savedTemplate.parameters).toContainEqual({
            name: 'status',
            type: 'string'
        });
    });

    it('should apply template with parameter values', async () => {
        const template = {
            sql: 'SELECT * FROM users WHERE id = :userId',
            parameters: [{ name: 'userId', type: 'number' }]
        };
        const parameterValues = { userId: 123 };

        await wrapper.setData({
            selectedTemplate: template,
            parameterValues
        });

        wrapper.vm.applyTemplate(template);
        
        expect(wrapper.emitted('use')).toBeTruthy();
        const appliedSql = wrapper.emitted('use')[0][0].sql;
        expect(appliedSql).toBe('SELECT * FROM users WHERE id = 123');
    });
});

describe('ParameterInput', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(ParameterInput, {
            propsData: {
                sql: 'SELECT * FROM users WHERE id = :userId AND status = :status',
                value: {}
            }
        });
    });

    afterEach(() => {
        wrapper.destroy();
    });

    it('should extract parameters from SQL', () => {
        expect(wrapper.vm.parameters).toHaveLength(2);
        expect(wrapper.vm.parameters[0].name).toBe('userId');
        expect(wrapper.vm.parameters[1].name).toBe('status');
    });

    it('should infer parameter types', () => {
        const parameters = wrapper.vm.parameters;
        expect(parameters.find(p => p.name === 'userId').type).toBe('number');
        expect(parameters.find(p => p.name === 'status').type).toBe('string');
    });

    it('should validate parameter values', async () => {
        await wrapper.setProps({
            value: {
                userId: 'not-a-number',
                status: 'active'
            }
        });

        const validation = wrapper.vm.validation;
        expect(validation.userId).toBe(false);
        expect(validation.status).toBe(true);
    });

    it('should emit validation status', async () => {
        await wrapper.setProps({
            value: {
                userId: 123,
                status: 'active'
            }
        });

        expect(wrapper.emitted('validation')).toBeTruthy();
        expect(wrapper.emitted('validation')[0][0]).toBe(true);
    });
});

describe('Integration Tests', () => {
    let queryBuilder;

    beforeEach(() => {
        queryBuilder = mount(QueryBuilderPage, {
            propsData: {
                dataSourceId: 'test-ds-001'
            }
        });
    });

    afterEach(() => {
        queryBuilder.destroy();
    });

    it('should handle complete query workflow', async () => {
        // 1. 编写SQL
        await queryBuilder.findComponent(QueryWorkspace).setProps({
            value: 'SELECT * FROM users WHERE status = :status'
        });

        // 2. 检测参数
        expect(queryBuilder.vm.panels.parameters).toBe(true);

        // 3. 填写参数
        const parameterInput = queryBuilder.findComponent(ParameterInput);
        await parameterInput.setProps({
            value: { status: 'active' }
        });

        // 4. 执行查询
        await queryBuilder.find('button.execute-button').trigger('click');
        expect(queryBuilder.vm.executionStatus.running).toBe(true);

        // 5. 保存为模板
        await queryBuilder.vm.saveAsTemplate({
            name: 'Active Users Query',
            description: 'Query all active users'
        });

        // 6. 验证模板保存
        const templates = queryBuilder.findComponent(QueryTemplate);
        expect(templates.vm.templates).toContainEqual(
            expect.objectContaining({
                name: 'Active Users Query'
            })
        );
    });
});