package com.nlquery.converter;

import com.nlquery.NLQueryRequest;

public interface NLToSqlConverter {
    SqlConversionResult convert(NLQueryRequest request);
}