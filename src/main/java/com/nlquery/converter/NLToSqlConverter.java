package com.nlquery.converter;

import com.domain.model.metadata.SchemaInfo;
import com.nlquery.NLQueryRequest;
import com.nlquery.QueryContext;
import com.nlquery.preprocess.PreprocessedText;

/**
 * 自然语言转SQL转换器接口
 */
public interface NLToSqlConverter {

    /**
     * 将自然语言查询转换为SQL
     *
     * @param request 查询请求
     * @return SQL转换结果
     */
    SqlConversionResult convert(NLQueryRequest request);

    /**
     * 将自然语言查询转换为SQL
     *
     * @param preprocessedText 自然语言查询
     * @param schemaInfo
     * @return SQL转换结果
     */
    SqlConversionResult convert(PreprocessedText preprocessedText, SchemaInfo schemaInfo);

}