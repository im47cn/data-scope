package com.insightdata.application.service.impl;

import com.insightdata.domain.querybuilder.api.QueryModelContract;
import com.insightdata.domain.querybuilder.model.*;
import com.insightdata.domain.querybuilder.repository.QueryModelRepository;
import com.insightdata.application.service.QueryModelApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 查询模型服务实现类
 */
@Service
@RequiredArgsConstructor
public class QueryModelApplicationServiceImpl implements QueryModelApplicationService {

    private final QueryModelRepository repository;

    @Override
    @Transactional
    public QueryModelContract create(QueryModelContract model) {
        if (!validate(model)) {
            throw new IllegalArgumentException("Invalid query model");
        }
        return repository.save((QueryModel) model);
    }

    @Override
    @Transactional
    public QueryModelContract update(QueryModelContract model) {
        if (!validate(model)) {
            throw new IllegalArgumentException("Invalid query model");
        }
        if (!repository.existsById(model.getId())) {
            throw new IllegalArgumentException("Query model not found");
        }
        return repository.save((QueryModel) model);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<QueryModelContract> findById(String id) {
        return repository.findById(id).map(model -> model);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QueryModelContract> findByName(String name) {
        return repository.findByNameContaining(name).stream()
                .map(model -> model)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QueryModelContract> findAll() {
        return repository.findAll().stream()
                .map(model -> model)
                .collect(Collectors.toList());
    }

    @Override
    public boolean validate(QueryModelContract model) {
        if (model == null) {
            return false;
        }

        // 基本验证
        if (model.getName() == null || model.getName().trim().isEmpty()) {
            return false;
        }

        // 验证表引用
        if (model.getTables() == null || model.getTables().isEmpty()) {
            return false;
        }

        // 验证字段选择
        if (model.getFields() == null || model.getFields().isEmpty()) {
            return false;
        }

        // 验证连接条件
        if (model.getJoins() != null) {
            for (String join : model.getJoins()) {
                if (join == null || join.trim().isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    @Transactional
    public QueryModelContract copy(String id, String newName) {
        QueryModel original = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query model not found"));

        QueryModel copy = original.copy();
        copy.setId(null); // 清除ID以创建新记录
        copy.setName(newName);

        return repository.save(copy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> execute(String id, Map<String, Object> parameters) {
        QueryModel model = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query model not found"));

        if (!validateParameters(id, parameters)) {
            throw new IllegalArgumentException("Invalid parameters");
        }

        // TODO: 实现查询执行逻辑
        return Collections.emptyList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParameterDefinition> getParameters(String id) {
        QueryModel model = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query model not found"));

        // TODO: 实现参数获取逻辑
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public QueryModelContract addFilter(String id, Filter filter) {
        QueryModel model = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query model not found"));

        if (filter == null || !filter.isValid()) {
            throw new IllegalArgumentException("Invalid filter");
        }

        // TODO: 实现过滤条件添加逻辑
        return model;
    }

    @Override
    @Transactional
    public QueryModelContract removeFilter(String id, int filterIndex) {
        QueryModel model = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query model not found"));

        // TODO: 实现过滤条件移除逻辑
        return model;
    }

    @Override
    public boolean validateParameters(String id, Map<String, Object> parameters) {
        List<ParameterDefinition> paramDefs = getParameters(id);

        // 检查必填参数
        for (ParameterDefinition def : paramDefs) {
            if (def.isRequired() && !parameters.containsKey(def.getName())) {
                return false;
            }
        }

        // 验证参数值
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Optional<ParameterDefinition> paramDef = paramDefs.stream()
                    .filter(def -> def.getName().equals(entry.getKey()))
                    .findFirst();

            if (paramDef.isPresent() && !paramDef.get().validateValue(entry.getValue())) {
                return false;
            }
        }

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public String getSqlPreview(String id) {
        QueryModel model = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query model not found"));

        // TODO: 实现SQL预览生成逻辑
        return "";
    }

    @Override
    @Transactional(readOnly = true)
    public String getExecutionPlan(String id) {
        QueryModel model = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Query model not found"));

        // TODO: 实现执行计划获取逻辑
        return "";
    }

    @Override
    @Transactional(readOnly = true)
    public List<QueryModelContract> findAll(int page, int size) {
        return repository.findAll(page, size).stream()
                .map(model -> model)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }
}