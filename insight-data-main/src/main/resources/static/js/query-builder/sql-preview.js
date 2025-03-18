/**
 * SQL预览组件
 * 提供实时SQL预览、语法高亮和验证反馈
 */
const SqlPreview = {
    props: {
        // SQL语句
        sql: {
            type: String,
            default: ''
        },
        // 是否有效
        isValid: {
            type: Boolean,
            default: true
        },
        // 错误信息
        errors: {
            type: Array,
            default: () => []
        },
        // 警告信息
        warnings: {
            type: Array,
            default: () => []
        },
        // 是否正在加载
        loading: {
            type: Boolean,
            default: false
        },
        // 是否可编辑
        editable: {
            type: Boolean,
            default: false
        },
        // 代码编辑器配置
        editorConfig: {
            type: Object,
            default: () => ({
                theme: 'vs',
                language: 'sql',
                minimap: { enabled: false },
                scrollBeyondLastLine: false,
                automaticLayout: true,
                lineNumbers: 'on',
                roundedSelection: false,
                scrollbar: {
                    vertical: 'visible',
                    horizontal: 'visible',
                    useShadows: false,
                    verticalScrollbarSize: 10,
                    horizontalScrollbarSize: 10
                }
            })
        }
    },

    data() {
        return {
            editor: null,
            decorations: [],
            copied: false
        };
    },

    computed: {
        // 是否显示验证结果
        showValidation() {
            return !!(this.errors.length || this.warnings.length);
        },

        // 验证结果统计
        validationSummary() {
            return {
                errors: this.errors.length,
                warnings: this.warnings.length
            };
        },

        // 格式化后的SQL
        formattedSql() {
            return this.sql ? this.formatSql(this.sql) : '';
        }
    },

    watch: {
        sql: {
            handler(newSql) {
                if (this.editor) {
                    const position = this.editor.getPosition();
                    this.editor.setValue(this.formatSql(newSql));
                    if (position) {
                        this.editor.setPosition(position);
                    }
                }
                this.updateDecorations();
            }
        },

        errors: {
            handler() {
                this.updateDecorations();
            },
            deep: true
        },

        warnings: {
            handler() {
                this.updateDecorations();
            },
            deep: true
        }
    },

    mounted() {
        this.initEditor();
    },

    beforeDestroy() {
        if (this.editor) {
            this.editor.dispose();
        }
    },

    methods: {
        // 初始化代码编辑器
        initEditor() {
            // 配置Monaco编辑器
            require(['vs/editor/editor.main'], () => {
                // 注册SQL格式化提供程序
                monaco.languages.registerDocumentFormattingEditProvider('sql', {
                    provideDocumentFormattingEdits: (model) => {
                        return [{
                            range: model.getFullModelRange(),
                            text: this.formatSql(model.getValue())
                        }];
                    }
                });

                // 创建编辑器实例
                this.editor = monaco.editor.create(this.$refs.editor, {
                    value: this.formatSql(this.sql),
                    readOnly: !this.editable,
                    ...this.editorConfig
                });

                // 监听内容变化
                this.editor.onDidChangeModelContent(() => {
                    if (this.editable) {
                        const value = this.editor.getValue();
                        this.$emit('update:sql', value);
                        this.$emit('change', value);
                    }
                });

                // 绑定快捷键
                this.editor.addCommand(monaco.KeyMod.CtrlCmd | monaco.KeyCode.KEY_S, () => {
                    this.$emit('save');
                });

                // 初始化装饰器
                this.updateDecorations();
            });
        },

        // 更新编辑器装饰器
        updateDecorations() {
            if (!this.editor) return;

            const decorations = [];

            // 添加错误装饰器
            this.errors.forEach(error => {
                if (error.line) {
                    decorations.push({
                        range: new monaco.Range(error.line, 1, error.line, 1),
                        options: {
                            isWholeLine: true,
                            glyphMarginClassName: 'sql-error-glyph',
                            glyphMarginHoverMessage: { value: error.message }
                        }
                    });
                }
            });

            // 添加警告装饰器
            this.warnings.forEach(warning => {
                if (warning.line) {
                    decorations.push({
                        range: new monaco.Range(warning.line, 1, warning.line, 1),
                        options: {
                            isWholeLine: true,
                            glyphMarginClassName: 'sql-warning-glyph',
                            glyphMarginHoverMessage: { value: warning.message }
                        }
                    });
                }
            });

            this.decorations = this.editor.deltaDecorations(this.decorations, decorations);
        },

        // 格式化SQL
        formatSql(sql) {
            return UtilService.formatSQL(sql);
        },

        // 复制SQL到剪贴板
        async copyToClipboard() {
            try {
                await UtilService.copyToClipboard(this.sql);
                this.copied = true;
                setTimeout(() => {
                    this.copied = false;
                }, 2000);
                this.$message.success('SQL已复制到剪贴板');
            } catch (error) {
                this.$message.error('复制失败，请手动复制');
            }
        },

        // 下载SQL文件
        downloadSql() {
            const blob = new Blob([this.sql], { type: 'text/plain' });
            const url = URL.createObjectURL(blob);
            UtilService.downloadFile(url, 'query.sql');
            URL.revokeObjectURL(url);
        }
    },

    template: `
        <div class="sql-preview">
            <!-- 工具栏 -->
            <div class="preview-toolbar">
                <div class="toolbar-left">
                    <a-space>
                        <a-tooltip :title="copied ? '已复制' : '复制SQL'">
                            <a-button
                                type="link"
                                :disabled="!sql"
                                @click="copyToClipboard"
                            >
                                <a-icon :type="copied ? 'check' : 'copy'" />
                                {{ copied ? '已复制' : '复制' }}
                            </a-button>
                        </a-tooltip>
                        <a-tooltip title="下载SQL">
                            <a-button
                                type="link"
                                :disabled="!sql"
                                @click="downloadSql"
                            >
                                <a-icon type="download" />
                                下载
                            </a-button>
                        </a-tooltip>
                        <a-tooltip v-if="editable" title="保存SQL">
                            <a-button
                                type="link"
                                :disabled="!sql"
                                @click="$emit('save')"
                            >
                                <a-icon type="save" />
                                保存
                            </a-button>
                        </a-tooltip>
                    </a-space>
                </div>

                <!-- 验证结果 -->
                <div v-if="showValidation" class="toolbar-right">
                    <a-space>
                        <a-tag v-if="validationSummary.errors" color="error">
                            {{ validationSummary.errors }} 个错误
                        </a-tag>
                        <a-tag v-if="validationSummary.warnings" color="warning">
                            {{ validationSummary.warnings }} 个警告
                        </a-tag>
                    </a-space>
                </div>
            </div>

            <!-- 编辑器容器 -->
            <div class="editor-container" :class="{ loading }">
                <div ref="editor" class="monaco-editor"></div>
                <div v-if="loading" class="editor-loading">
                    <a-spin />
                </div>
            </div>

            <!-- 验证消息 -->
            <div v-if="showValidation" class="validation-messages">
                <a-collapse v-if="errors.length">
                    <a-collapse-panel header="错误" key="errors">
                        <a-list
                            size="small"
                            :dataSource="errors"
                        >
                            <a-list-item slot="renderItem" slot-scope="error">
                                <a-icon type="close-circle" theme="filled" style="color: #f5222d" />
                                <span style="margin-left: 8px">{{ error.message }}</span>
                            </a-list-item>
                        </a-list>
                    </a-collapse-panel>
                </a-collapse>

                <a-collapse v-if="warnings.length">
                    <a-collapse-panel header="警告" key="warnings">
                        <a-list
                            size="small"
                            :dataSource="warnings"
                        >
                            <a-list-item slot="renderItem" slot-scope="warning">
                                <a-icon type="warning" theme="filled" style="color: #faad14" />
                                <span style="margin-left: 8px">{{ warning.message }}</span>
                            </a-list-item>
                        </a-list>
                    </a-collapse-panel>
                </a-collapse>
            </div>
        </div>
    `
};

// 导入依赖
import UtilService from '../services/util-service.js';

// 注册组件
Vue.component('sql-preview', SqlPreview);