package com.domain.model.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 索引信息实体类
 * 表示数据库表的索引及其元数据
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class IndexInfo {

    /**
     * 数据源ID
     */
    private Long dataSourceId;

    /**
     * 模式名称
     */
    private String schemaName;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 索引名称
     */
    private String name;

    /**
     * 索引类型
     * 例如: BTREE, HASH, GIN, GIST等
     */
    private String type;

    /**
     * 是否唯一索引
     */
    private boolean unique;

    /**
     * 是否是主键索引
     */
    private boolean primaryKey;

    /**
     * 是否是聚集索引
     */
    private boolean clustered;

    /**
     * 索引包含的列
     */
    @Builder.Default
    private List<IndexColumnInfo> columns = new ArrayList<>();

    /**
     * 索引过滤条件
     */
    private String filterCondition;

    /**
     * 索引大小（字节）
     */
    private Long size;

    /**
     * 索引的描述/注释
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 添加索引列
     */
    public void addColumn(IndexColumnInfo column) {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        columns.add(column);
    }

    /**
     * 获取索引列名称列表
     */
    public List<String> getColumnNames() {
        if (columns == null || columns.isEmpty()) {
            return new ArrayList<>();
        }

        return columns.stream()
                .map(col -> col.getColumnName())
                .collect(Collectors.toList());
    }

    /**
     * 检查索引是否包含指定列
     */
    public boolean containsColumn(String columnName) {
        if (columns == null || columns.isEmpty()) {
            return false;
        }

        return columns.stream()
                .anyMatch(col -> col.getColumnName().equalsIgnoreCase(columnName));
    }

    /**
     * 获取索引的可读名称
     * 例如: 表名_列名1_列名2_idx
     */
    public String getReadableName() {
        if (name != null && !name.isEmpty()) {
            return name;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(tableName);

        if (columns != null && !columns.isEmpty()) {
            for (IndexColumnInfo column : columns) {
                sb.append("_").append(column.getColumnName());
            }
        }

        if (primaryKey) {
            sb.append("_pk");
        } else if (unique) {
            sb.append("_uk");
        } else {
            sb.append("_idx");
        }

        return sb.toString();
    }

    /**
     * 生成创建索引的SQL语句
     */
    public String generateCreateIndexStatement() {
        StringBuilder sb = new StringBuilder();

        if (primaryKey) {
            sb.append("ALTER TABLE ");
            if (schemaName != null && !schemaName.isEmpty()) {
                sb.append(schemaName).append(".");
            }
            sb.append(tableName);
            sb.append(" ADD CONSTRAINT ").append(name);
            sb.append(" PRIMARY KEY (");
            sb.append(columns.stream()
                    .map(col -> col.getColumnName())
                    .collect(Collectors.joining(", ")));
            sb.append(")");
        } else {
            sb.append("CREATE ");
            if (unique) {
                sb.append("UNIQUE ");
            }
            if (clustered) {
                sb.append("CLUSTERED ");
            }
            sb.append("INDEX ").append(name);
            sb.append(" ON ");
            if (schemaName != null && !schemaName.isEmpty()) {
                sb.append(schemaName).append(".");
            }
            sb.append(tableName).append(" (");

            sb.append(columns.stream()
                    .map(col -> {
                        StringBuilder colSb = new StringBuilder();
                        colSb.append(col.getColumnName());
                        if (col.sortOrder != null && !col.sortOrder.isEmpty()) {
                            colSb.append(" ").append(col.sortOrder);
                        }
                        return colSb.toString();
                    })
                    .collect(Collectors.joining(", ")));

            sb.append(")");

            if (type != null && !type.isEmpty()) {
                sb.append(" USING ").append(type);
            }

            if (filterCondition != null && !filterCondition.isEmpty()) {
                sb.append(" WHERE ").append(filterCondition);
            }
        }

        return sb.toString();
    }

    /**
     * 获取索引的完全限定名
     */
    public String getQualifiedName() {
        if (schemaName != null && !schemaName.isEmpty()) {
            return schemaName + "." + tableName + "." + name;
        }
        return tableName + "." + name;
    }

    /**
     * 获取索引的展示信息
     */
    public String getDisplayInfo() {
        StringBuilder sb = new StringBuilder();

        if (primaryKey) {
            sb.append("主键: ");
        } else if (unique) {
            sb.append("唯一索引: ");
        } else {
            sb.append("索引: ");
        }

        sb.append(name);

        sb.append(" (");
        if (columns != null && !columns.isEmpty()) {
            sb.append(columns.stream()
                    .map(col -> col.getColumnName())
                    .collect(Collectors.joining(", ")));
        }
        sb.append(")");

        if (type != null && !type.isEmpty()) {
            sb.append(" [").append(type).append("]");
        }

        return sb.toString();
    }

}