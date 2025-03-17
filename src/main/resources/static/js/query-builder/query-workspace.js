/**
 * SQL工作区组件
 * 集成SQL编辑器、格式化、模板等功能
 */
const QueryWorkspace = {
    props: {
        // 数据源ID
        dataSourceId: {
            type: String,
            required: true
        },
        // SQL内容
        value: {
            type: String,
            default: ''
        },
        // 是否禁用
        disabled: {
            type: Boolean,
            default: false
        }
    },

    data() {
        return {
            editor: null,           // CodeMirror实例
            editorConfig: {
                mode: 'text/x-sql',
                theme: 'idea',
                lineNumbers: true,
                smartIndent: true,
                indentWithTabs: false,
                lineWrapping: true,
                matchBrackets: true,
                autoCloseBrackets: true,
                autoCloseTags: true,
                foldGutter: true,
                gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter'],
                extraKeys: {
                    'Ctrl-Space': 'autocomplete',
                    'Ctrl-F': 'findPersistent',
                    'Ctrl-Alt-F': this.formatSql,
                    'Ctrl-S': this.saveAsTemplate,
                    'Alt-/': this.showSuggestions
                },
                hintOptions: {
                    tables: {},
                    completeSingle: false
                }
            },
            suggestions: [],        // 自动完成建议
            showSuggestionList: false,
            currentWord: '',
            templates: [],          // 可用的查询模板
            showTemplateDialog: false,
            selectedTemplate: null,
            toolbarConfig: {
                format: true,
                template: true,
                history: true,
                fullscreen: true
            },
            isFullscreen: false
        };
    },

    computed: {
        formattedSql() {
            return SqlFormatter.format(this.value);
        }
    },

    mounted() {
        this.initEditor();
        this.loadTemplates();
        this.initMetadata();
    },

    methods: {
        /**
         * 初始化编辑器
         */
        initEditor() {
            // 初始化CodeMirror编辑器
            this.editor = CodeMirror(this.$refs.editor, {
                ...this.editorConfig,
                value: this.value
            });

            // 监听变更事件
            this.editor.on('change', (cm) => {
                const value = cm.getValue();
                this.$emit('input', value);
                this.$emit('change', value);
            });

            // 监听光标位置变化
            this.editor.on('cursorActivity', this.handleCursorActivity);

            // 设置快捷键
            this.setupShortcuts();
        },

        /**
         * 设置编辑器快捷键
         */
        setupShortcuts() {
            this.editor.setOption('extraKeys', {
                ...this.editorConfig.extraKeys,
                'Tab': (cm) => {
                    if (cm.somethingSelected()) {
                        cm.indentSelection('add');
                    } else {
                        cm.replaceSelection('    ');
                    }
                }
            });
        },

        /**
         * 初始化数据库元数据
         */
        async initMetadata() {
            try {
                const response = await DataSourceService.getSchemaInfo(this.dataSourceId);
                const metadata = response.data;
                
                // 更新自动完成提示的表和字段信息
                this.editorConfig.hintOptions.tables = this.transformMetadataForHint(metadata);
                
                // 重新设置编辑器配置
                this.editor.setOption('hintOptions', this.editorConfig.hintOptions);
            } catch (error) {
                console.error('加载数据库元数据失败:', error);
                this.$message.error('加载数据库元数据失败');
            }
        },

        /**
         * 转换元数据为提示格式
         */
        transformMetadataForHint(metadata) {
            const result = {};
            metadata.tables.forEach(table => {
                result[table.name] = {
                    columns: table.columns.map(col => ({
                        name: col.name,
                        type: col.type,
                        description: col.description
                    }))
                };
            });
            return result;
        },

        /**
         * 加载查询模板
         */
        async loadTemplates() {
            try {
                const response = await QueryService.getQueryTemplates(this.dataSourceId);
                this.templates = response.data;
            } catch (error) {
                console.error('加载查询模板失败:', error);
                this.$message.error('加载查询模板失败');
            }
        },

        /**
         * 格式化SQL
         */
        formatSql() {
            const formatted = SqlFormatter.format(this.editor.getValue());
            this.editor.setValue(formatted);
        },

        /**
         * 保存为模板
         */
        saveAsTemplate() {
            this.showTemplateDialog = true;
        },

        /**
         * 应用模板
         */
        applyTemplate(template) {
            const sql = template.sql;
            const currentPosition = this.editor.getCursor();
            this.editor.replaceRange(sql, currentPosition);
            this.showTemplateDialog = false;
        },

        /**
         * 处理光标位置变化
         */
        handleCursorActivity(cm) {
            const pos = cm.getCursor();
            const token = cm.getTokenAt(pos);
            
            if (token.string !== this.currentWord) {
                this.currentWord = token.string;
                if (token.string.length >= 2) {
                    this.showSuggestions();
                }
            }
        },

        /**
         * 显示建议
         */
        async showSuggestions() {
            if (!this.currentWord || this.currentWord.length < 2) {
                return;
            }

            try {
                const response = await QueryService.getQuerySuggestions(
                    this.dataSourceId,
                    this.currentWord
                );
                
                this.suggestions = response.data;
                if (this.suggestions.length > 0) {
                    this.showSuggestionList = true;
                }
            } catch (error) {
                console.error('获取建议失败:', error);
            }
        },

        /**
         * 插入建议
         */
        insertSuggestion(suggestion) {
            const cursor = this.editor.getCursor();
            const token = this.editor.getTokenAt(cursor);
            const start = { line: cursor.line, ch: token.start };
            const end = { line: cursor.line, ch: token.end };
            
            this.editor.replaceRange(suggestion.text, start, end);
            this.showSuggestionList = false;
        },

        /**
         * 切换全屏
         */
        toggleFullscreen() {
            this.isFullscreen = !this.isFullscreen;
            if (this.isFullscreen) {
                this.$el.classList.add('fullscreen');
            } else {
                this.$el.classList.remove('fullscreen');
            }
            this.editor.refresh();
        }
    },

    beforeDestroy() {
        // 清理编辑器实例
        if (this.editor) {
            this.editor.toTextArea();
        }
    },

    template: `
        <div class="query-workspace" :class="{ 'fullscreen': isFullscreen }">
            <!-- 工具栏 -->
            <div class="workspace-toolbar">
                <a-space>
                    <a-tooltip title="格式化 (Ctrl+Alt+F)" v-if="toolbarConfig.format">
                        <a-button @click="formatSql">
                            <a-icon type="align-left" />
                        </a-button>
                    </a-tooltip>
                    
                    <a-tooltip title="模板 (Ctrl+S)" v-if="toolbarConfig.template">
                        <a-button @click="saveAsTemplate">
                            <a-icon type="save" />
                        </a-button>
                    </a-tooltip>
                    
                    <a-tooltip title="全屏" v-if="toolbarConfig.fullscreen">
                        <a-button @click="toggleFullscreen">
                            <a-icon :type="isFullscreen ? 'fullscreen-exit' : 'fullscreen'" />
                        </a-button>
                    </a-tooltip>
                </a-space>
            </div>

            <!-- 编辑器容器 -->
            <div ref="editor" class="sql-editor"></div>

            <!-- 建议列表 -->
            <a-card v-if="showSuggestionList" class="suggestion-list">
                <a-list size="small" :dataSource="suggestions">
                    <a-list-item slot="renderItem" slot-scope="item">
                        <a @click="insertSuggestion(item)">
                            {{ item.text }}
                            <span class="suggestion-type">{{ item.type }}</span>
                        </a>
                    </a-list-item>
                </a-list>
            </a-card>

            <!-- 模板对话框 -->
            <query-template
                v-model="showTemplateDialog"
                :dataSourceId="dataSourceId"
                :templates="templates"
                @select="applyTemplate"
            />
        </div>
    `
};

// 导入依赖
import QueryService from '../services/query-service.js';
import DataSourceService from '../services/datasource-service.js';
import SqlFormatter from '../services/sql-formatter.js';

// 注册组件
Vue.component('query-workspace', QueryWorkspace);