package com.nlquery.converter;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SqlConversionResult {
    private String sql;
    private Map<String, Object> parameters;
    private double confidence;
    private List<String> explanations;
    private List<String> alternativeSqls; // Optional alternative SQL queries
    private boolean success;
    private String errorMessage;
  //  private List<EntityTag> extractedEntities; // Assuming you have an EntityTag class
  //  private QueryIntent queryIntent; // Assuming you have a QueryIntent class

}