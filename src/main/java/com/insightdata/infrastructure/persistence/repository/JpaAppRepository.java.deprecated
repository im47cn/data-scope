package com.insightdata.infrastructure.persistence.repository;

import com.insightdata.infrastructure.persistence.entity.AppEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 应用JPA仓库接口
 */
@Repository
public interface JpaAppRepository extends JpaRepository<AppEntity, Long> {
    
    /**
     * 根据应用编码查找应用
     * 
     * @param code 应用编码
     * @return 应用实体
     */
    Optional<AppEntity> findByCode(String code);
    
    /**
     * 根据应用名称模糊查询应用列表
     * 
     * @param name 应用名称
     * @return 应用实体列表
     */
    List<AppEntity> findByNameContaining(String name);
    
    /**
     * 根据应用编码判断应用是否存在
     * 
     * @param code 应用编码
     * @return 是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 查询所有已发布应用
     * 
     * @return 已发布应用列表
     */
    @Query("SELECT a FROM AppEntity a WHERE a.publishStatus = 1")
    List<AppEntity> findAllPublished();
}