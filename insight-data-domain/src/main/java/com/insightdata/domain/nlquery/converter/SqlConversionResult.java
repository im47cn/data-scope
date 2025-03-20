package com.insightdata.domain.nlquery.converter;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
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