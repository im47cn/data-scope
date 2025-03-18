# Query Builder Components Documentation

## Core Components

1. **QueryBuilderPage (`query-builder-page.js`)**
   - Main container component
   - Handles overall layout and state management
   - Integrates all sub-components

2. **QueryWorkspace (`query-workspace.js`)**
   - SQL editor workspace
   - Integrates CodeMirror for SQL editing
   - Handles SQL formatting and validation

3. **QueryTemplate (`query-template.js`)**
   - Template management component
   - Supports saving and loading query templates
   - Handles parameter extraction and management

4. **ParameterInput (`parameter-input.js`)**
   - Parameter input form component
   - Supports different parameter types
   - Provides validation and type inference

## Services

1. **QueryService (`query-service.js`)**
   - Handles all API calls related to queries
   - Manages query execution and results
   - Handles template operations

2. **SqlFormatter (`sql-formatter.js`)**
   - SQL formatting and highlighting
   - Syntax validation
   - Token parsing and analysis

3. **UtilService (`util-service.js`)**
   - Common utility functions
   - Date formatting
   - File operations
   - String manipulation

## Styles

1. **Global Styles (`style.css`)**
   - Base styles and variables
   - Theme support
   - Layout components

2. **Query Builder Styles (`query-builder.css`)**
   - Main query builder layout
   - Component-specific styles
   - Responsive design

3. **Workspace Styles (`query-workspace.css`)**
   - SQL editor styling
   - Code highlighting
   - Editor tools and panels

4. **Template Styles (`query-template.css`)**
   - Template list and forms
   - Modal dialogs
   - Action buttons

5. **Parameter Input Styles (`parameter-input.css`)**
   - Form controls
   - Validation states
   - Input types

6. **SQL Highlight Styles (`sql-highlight.css`)**
   - Syntax highlighting
   - Token colors
   - Theme-specific highlighting

## Features

1. **SQL Editing**
   - Syntax highlighting
   - Auto-completion
   - Error detection
   - Format and beautify

2. **Template Management**
   - Save and load templates
   - Parameter extraction
   - Template categories
   - Access control

3. **Parameter Handling**
   - Type inference
   - Validation
   - Default values
   - Suggestions

4. **Query Execution**
   - Result preview
   - Export options
   - Error handling
   - Progress tracking

5. **Theme Support**
   - Light/dark modes
   - Custom themes
   - Syntax highlighting themes

## Usage Example

```javascript
// Create a new query workspace
const workspace = new QueryWorkspace({
    dataSourceId: 'ds123',
    initialSql: 'SELECT * FROM users',
    parameters: {
        userId: 1,
        status: 'active'
    }
});

// Save as template
workspace.saveAsTemplate({
    name: 'User Query',
    description: 'Query user data with filters',
    isPublic: true
});

// Execute query
workspace.executeQuery()
    .then(results => {
        console.log('Query results:', results);
    })
    .catch(error => {
        console.error('Query failed:', error);
    });
```

## Installation

1. Include required dependencies:
```html
<!-- Vue.js -->
<script src="https://cdn.jsdelivr.net/npm/vue@2.7.14/dist/vue.min.js"></script>

<!-- Ant Design Vue -->
<link rel="stylesheet" href="https://unpkg.com/ant-design-vue@1.7.8/dist/antd.min.css">
<script src="https://unpkg.com/ant-design-vue@1.7.8/dist/antd.min.js"></script>

<!-- CodeMirror -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.65.2/codemirror.min.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.65.2/codemirror.min.js"></script>
```

2. Import components:
```javascript
import QueryBuilder from './query-builder/index.js';
```

3. Initialize the application:
```javascript
new Vue({
    el: '#app',
    components: {
        QueryBuilder
    }
});
```

## Configuration

Configuration options can be set through component props or global configuration:

```javascript
{
    theme: 'light',              // 'light' or 'dark'
    autoComplete: true,          // Enable/disable auto-completion
    formatOnSave: true,         // Auto-format SQL on save
    maxHistoryItems: 50,        // Maximum number of history items
    exportFormats: ['csv', 'excel', 'json'],
    defaultPageSize: 100,
    timeout: 30000              // Query timeout in milliseconds
}
```

## Best Practices

1. Always validate parameters before query execution
2. Use templates for frequently used queries
3. Implement proper error handling
4. Cache query results when appropriate
5. Use appropriate security measures
6. Follow SQL formatting standards
7. Implement proper access control
8. Monitor query performance
9. Provide meaningful error messages
10. Maintain query history for auditing