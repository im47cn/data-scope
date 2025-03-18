package com.insightdata.domain.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.insightdata.domain.exception.DataSourceException;
import com.insightdata.domain.metadata.enums.DataSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.metadata.model.TableInfo;
import com.insightdata.domain.repository.DataSourceRepository;
import com.insightdata.domain.repository.SchemaInfoRepository;
import com.insightdata.domain.repository.TableInfoRepository;
import com.insightdata.domain.service.CredentialEncryptionService;
import com.insightdata.domain.service.DataSourceService;

/**
 * 数据源服务实现类
 */
@Service
public class DataSourceServiceImpl implements DataSourceService {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    private final DataSourceRepository dataSourceRepository;
    private final SchemaInfoRepository schemaInfoRepository;
    private final TableInfoRepository tableInfoRepository;
    private final CredentialEncryptionService encryptionService;

    @Autowired
    public DataSourceServiceImpl(
            DataSourceRepository dataSourceRepository,
            SchemaInfoRepository schemaInfoRepository,
            TableInfoRepository tableInfoRepository,
            CredentialEncryptionService encryptionService) {
        this.dataSourceRepository = dataSourceRepository;
        this.schemaInfoRepository = schemaInfoRepository;
        this.tableInfoRepository = tableInfoRepository;
        this.encryptionService = encryptionService;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DataSource> getDataSourceById(String id) {
        return dataSourceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DataSource> getDataSourceByName(String name) {
        return dataSourceRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSource> getAllDataSources() {
        return dataSourceRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSource> getDataSourcesByType(DataSourceType type) {
        return dataSourceRepository.findByType(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSource> getDataSourcesByActive(boolean active) {
        return dataSourceRepository.findByActive(active);
    }

    @Override
    @Transactional
    public DataSource createDataSource(DataSource dataSource) {
        // 检查数据源名称是否已存在
        String name = getFieldValue(dataSource, "name");
        if (dataSourceRepository.existsByName(name)) {
            throw new DataSourceException("数据源名称已存在: " + name);
        }

        // 生成UUID作为ID
        String id = UUID.randomUUID().toString();
        setFieldValue(dataSource, "id", id);

        // 加密密码
        encryptPassword(dataSource);

        // 设置创建和更新时间
        LocalDateTime now = LocalDateTime.now();
        setFieldValue(dataSource, "createdAt", now);
        setFieldValue(dataSource, "updatedAt", now);

        // 默认启用
        if (getFieldValue(dataSource, "enabled") == null) {
            setFieldValue(dataSource, "enabled", true);
        }

        // 保存数据源
        return dataSourceRepository.save(dataSource);
    }

    @Override
    @Transactional
    public DataSource updateDataSource(DataSource dataSource) {
        // 检查数据源是否存在
        String id = getFieldValue(dataSource, "id");
        if (id == null || id.isEmpty()) {
            throw new DataSourceException("更新数据源时ID不能为空");
        }

        DataSource existingDataSource = dataSourceRepository.findById(id)
                .orElseThrow(() -> new DataSourceException("数据源不存在: " + id));

        // 检查名称是否被其他数据源使用
        String name = getFieldValue(dataSource, "name");
        if (name != null && !name.equals(getFieldValue(existingDataSource, "name"))) {
            if (dataSourceRepository.existsByName(name)) {
                throw new DataSourceException("数据源名称已被使用: " + name);
            }
        }

        // 如果提供了新密码，则加密
        String password = getFieldValue(dataSource, "password");
        if (password != null && !password.isEmpty()) {
            encryptPassword(dataSource);
        } else {
            // 使用原有的加密密码和盐值
            setFieldValue(dataSource, "encryptedPassword", getFieldValue(existingDataSource, "encryptedPassword"));
            setFieldValue(dataSource, "encryptionSalt", getFieldValue(existingDataSource, "encryptionSalt"));
        }

        // 更新时间
        setFieldValue(dataSource, "updatedAt", LocalDateTime.now());

        // 保存数据源
        return dataSourceRepository.save(dataSource);
    }

    @Override
    @Transactional
    public void deleteDataSource(String id) {
        // 检查数据源是否存在
        DataSource dataSource = dataSourceRepository.findById(id)
                .orElseThrow(() -> new DataSourceException("数据源不存在: " + id));

        // 获取数据源名称用于日志
        String name = getFieldValue(dataSource, "name");

        // 删除数据源
        dataSourceRepository.deleteById(id);

        // 使用带有明确类型的日志记录方式
        logger.info("已删除数据源: {}", String.valueOf(name));
    }

    @Override
    public boolean testConnection(DataSource dataSource) {
        // TODO: 实现数据源连接测试
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public SchemaInfo getSchemaInfo(String dataSourceId, String schemaName) {
        // TODO: 实现获取Schema信息的逻辑
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchemaInfo> getSchemas(String dataSourceId) {
        // TODO: 实现获取所有Schema的逻辑
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TableInfo> getTables(String dataSourceId, String schemaName) {
        // TODO: 实现获取表信息的逻辑
        return null;
    }

    @Override
    @Transactional
    public String syncMetadata(String dataSourceId) {
        // TODO: 实现元数据同步逻辑
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DataSourceType> getSupportedTypes() {
        return Arrays.asList(DataSourceType.values());
    }

    /**
     * 加密数据源密码
     *
     * @param dataSource 数据源对象
     */
    private void encryptPassword(DataSource dataSource) {
        String password = getFieldValue(dataSource, "password");
        if (password != null && !password.isEmpty()) {
            CredentialEncryptionService.EncryptionResult result = encryptionService.encrypt(password);
            setFieldValue(dataSource, "encryptedPassword", result.getEncryptedPassword());
            setFieldValue(dataSource, "encryptionSalt", result.getSalt());
        }
    }

    /**
     * 使用反射设置对象的字段值
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @param value     字段值
     */
    private void setFieldValue(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            logger.error("反射设置字段值失败: {}", fieldName, e);
        }
    }

    /**
     * 使用反射获取对象的字段值
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @return 字段值，如果发生异常则返回null
     */
    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            logger.error("反射获取字段值失败: {}", fieldName, e);
            return null;
        }
    }
}