import { mount } from '@vue/test-utils';
import UtilService from '../../../main/resources/static/js/services/util-service';

jest.mock('../../../main/resources/static/js/services/util-service');
jest.mock('monaco-editor', () => ({
    editor: {
        create: jest.fn(() => ({
            dispose: jest.fn(),
            onDidChangeModelContent: jest.fn(cb => cb()),
            getValue: jest.fn(() => 'SELECT * FROM test'),
            setValue: jest.fn(),
            getPosition: jest.fn(),
            setPosition: jest.fn(),
            deltaDecorations: jest.fn(),
            addCommand: jest.fn()
        })),
        KeyMod: {
            CtrlCmd: 2048
        },
        KeyCode: {
            KEY_S: 83
        }
    }
}));

describe('SqlPreview', () => {
    let wrapper;
    const mockSql = 'SELECT * FROM users';
    const mockFormatted = 'SELECT\n  *\nFROM\n  users';

    beforeEach(() => {
        jest.clearAllMocks();
        
        // Mock UtilService methods
        UtilService.formatSQL.mockImplementation(sql => mockFormatted);
        UtilService.copyToClipboard.mockResolvedValue();
        UtilService.downloadFile.mockImplementation();

        // Mock window.URL
        global.URL.createObjectURL = jest.fn();
        global.URL.revokeObjectURL = jest.fn();

        wrapper = mount(SqlPreview, {
            propsData: {
                sql: mockSql,
                isValid: true,
                errors: [],
                warnings: [],
                loading: false,
                editable: true
            },
            stubs: [
                'a-button',
                'a-icon',
                'a-space',
                'a-tag',
                'a-tooltip',
                'a-spin',
                'a-collapse',
                'a-collapse-panel',
                'a-list',
                'a-list-item'
            ],
            mocks: {
                $message: {
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
        it('should initialize with provided SQL', () => {
            expect(wrapper.vm.formattedSql).toBe(mockFormatted);
            expect(UtilService.formatSQL).toHaveBeenCalledWith(mockSql);
        });

        it('should setup Monaco editor', () => {
            expect(wrapper.vm.editor).toBeTruthy();
            expect(wrapper.vm.editor.setValue).toHaveBeenCalledWith(mockFormatted);
        });

        it('should handle read-only mode', async () => {
            await wrapper.setProps({ editable: false });
            expect(wrapper.vm.editor.updateOptions).toHaveBeenCalledWith({
                readOnly: true
            });
        });
    });

    describe('SQL formatting', () => {
        it('should format SQL when value changes', async () => {
            await wrapper.setProps({ sql: 'SELECT id FROM users' });
            expect(UtilService.formatSQL).toHaveBeenCalledWith('SELECT id FROM users');
            expect(wrapper.vm.editor.setValue).toHaveBeenCalled();
        });

        it('should preserve cursor position after formatting', async () => {
            const position = { lineNumber: 1, column: 1 };
            wrapper.vm.editor.getPosition.mockReturnValue(position);
            
            await wrapper.setProps({ sql: 'SELECT id FROM users' });
            
            expect(wrapper.vm.editor.setPosition).toHaveBeenCalledWith(position);
        });
    });

    describe('Validation display', () => {
        it('should show validation summary', async () => {
            await wrapper.setProps({
                errors: [{ message: 'Error 1' }],
                warnings: [{ message: 'Warning 1' }]
            });

            expect(wrapper.vm.showValidation).toBe(true);
            expect(wrapper.vm.validationSummary).toEqual({
                errors: 1,
                warnings: 1
            });
        });

        it('should update decorations for errors and warnings', async () => {
            const mockErrors = [
                { line: 1, message: 'Error 1' }
            ];
            const mockWarnings = [
                { line: 2, message: 'Warning 1' }
            ];

            await wrapper.setProps({
                errors: mockErrors,
                warnings: mockWarnings
            });

            expect(wrapper.vm.editor.deltaDecorations).toHaveBeenCalled();
        });
    });

    describe('User interactions', () => {
        it('should copy SQL to clipboard', async () => {
            await wrapper.vm.copyToClipboard();
            
            expect(UtilService.copyToClipboard).toHaveBeenCalledWith(mockSql);
            expect(wrapper.vm.$message.success).toHaveBeenCalledWith('SQL已复制到剪贴板');
            expect(wrapper.vm.copied).toBe(true);
            
            // Wait for copied state to reset
            jest.advanceTimersByTime(2000);
            expect(wrapper.vm.copied).toBe(false);
        });

        it('should handle copy failure', async () => {
            UtilService.copyToClipboard.mockRejectedValue(new Error());
            
            await wrapper.vm.copyToClipboard();
            expect(wrapper.vm.$message.error).toHaveBeenCalledWith('复制失败，请手动复制');
        });

        it('should download SQL file', async () => {
            await wrapper.vm.downloadSql();
            
            expect(global.URL.createObjectURL).toHaveBeenCalled();
            expect(UtilService.downloadFile).toHaveBeenCalled();
            expect(global.URL.revokeObjectURL).toHaveBeenCalled();
        });

        it('should emit changes when editable', async () => {
            const newSql = 'SELECT id FROM users';
            wrapper.vm.editor.getValue.mockReturnValue(newSql);
            
            // Trigger content change
            wrapper.vm.editor.onDidChangeModelContent.mock.calls[0][0]();
            
            expect(wrapper.emitted()['update:sql'][0]).toEqual([newSql]);
            expect(wrapper.emitted().change[0]).toEqual([newSql]);
        });

        it('should not emit changes when not editable', async () => {
            await wrapper.setProps({ editable: false });
            
            wrapper.vm.editor.getValue.mockReturnValue('SELECT id FROM users');
            wrapper.vm.editor.onDidChangeModelContent.mock.calls[0][0]();
            
            expect(wrapper.emitted()['update:sql']).toBeFalsy();
            expect(wrapper.emitted().change).toBeFalsy();
        });
    });

    describe('Keyboard shortcuts', () => {
        it('should setup save shortcut', () => {
            expect(wrapper.vm.editor.addCommand).toHaveBeenCalledWith(
                2048 | 83, // CtrlCmd + S
                expect.any(Function)
            );
        });

        it('should emit save event on shortcut', () => {
            // Get save callback
            const saveCallback = wrapper.vm.editor.addCommand.mock.calls[0][1];
            saveCallback();
            
            expect(wrapper.emitted().save).toBeTruthy();
        });
    });

    describe('Cleanup', () => {
        it('should dispose editor on destroy', () => {
            wrapper.destroy();
            expect(wrapper.vm.editor.dispose).toHaveBeenCalled();
        });
    });
});