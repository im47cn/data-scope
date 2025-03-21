package com.insightdata.infrastructure.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.insightdata.infrastructure.persistence.entity.QueryModelEntity;

@Mapper
public interface QueryModelMapper {

    void insert(QueryModelEntity queryModel);

    QueryModelEntity findById(String id);

    List<QueryModelEntity> findAll();

    void update(QueryModelEntity queryModel);

    void deleteById(String id);

    List<QueryModelEntity> findByNameContaining(String name);

    boolean existsByName(String name);

    boolean existsById(String id);
}