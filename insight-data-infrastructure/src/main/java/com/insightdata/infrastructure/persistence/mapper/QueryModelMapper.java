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

    @Insert("INSERT INTO query_models (id, name, description, data_sources, tables, fields, joins, root_filter, " +
            "group_by, order_by, parameters, options, created_at, updated_at, created_by, updated_by, is_public, tags, status) " +
            "VALUES (#{id}, #{name}, #{description}, #{dataSources,jdbcType=VARCHAR}, #{tables,jdbcType=VARCHAR}, " +
            "#{fields,jdbcType=VARCHAR}, #{joins,jdbcType=VARCHAR}, #{rootFilter,jdbcType=VARCHAR}, " +
            "#{groupBy,jdbcType=VARCHAR}, #{orderBy,jdbcType=VARCHAR}, #{parameters,jdbcType=VARCHAR}, " +
            "#{options,jdbcType=VARCHAR}, #{createdAt}, #{updatedAt}, #{createdBy}, #{updatedBy}, #{isPublic}, " +
            "#{tags,jdbcType=VARCHAR}, #{status,jdbcType=VARCHAR})")
    void insert(QueryModelEntity queryModel);

    @Select("SELECT * FROM query_models WHERE id = #{id}")
    @Results({
            @Result(property = "dataSources", column = "data_sources"),
            @Result(property = "isPublic", column = "is_public")
    })
    QueryModelEntity findById(String id);

    @Select("SELECT * FROM query_models")
    List<QueryModelEntity> findAll();

    @Update("UPDATE query_models SET name = #{name}, description = #{description}, data_sources = #{dataSources,jdbcType=VARCHAR}, " +
            "tables = #{tables,jdbcType=VARCHAR}, fields = #{fields,jdbcType=VARCHAR}, joins = #{joins,jdbcType=VARCHAR}, " +
            "root_filter = #{rootFilter,jdbcType=VARCHAR}, group_by = #{groupBy,jdbcType=VARCHAR}, order_by = #{orderBy,jdbcType=VARCHAR}, " +
            "parameters = #{parameters,jdbcType=VARCHAR}, options = #{options,jdbcType=VARCHAR}, " +
            "updated_at = #{updatedAt}, updated_by = #{updatedBy}, is_public = #{isPublic,jdbcType=BOOLEAN}, tags = #{tags,jdbcType=VARCHAR}, status = #{status,jdbcType=VARCHAR} " +
            "WHERE id = #{id}")
    void update(QueryModelEntity queryModel);

    @Delete("DELETE FROM query_models WHERE id = #{id}")
    void deleteById(String id);

    @Select("SELECT * FROM query_models WHERE name LIKE #{name}")
    List<QueryModelEntity> findByNameContaining(String name);

    @Select("SELECT COUNT(*) FROM query_models WHERE name = #{name}")
    boolean existsByName(String name);

    @Select("SELECT COUNT(*) FROM query_models WHERE id = #{id}")
    boolean existsById(String id);
}