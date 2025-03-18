package com.insightdata.domain.nlquery.converter;

import com.insightdata.domain.nlquery.NLQueryRequest;

public interface NLToSqlConverter {
    SqlConversionResult convert(NLQueryRequest request);
}