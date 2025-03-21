package com.insightdata.facade.querybuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryModelDto {
    private String id;
    private String name;
    private List<String> tables = new ArrayList<>();
    private List<String> fields = new ArrayList<>();
    private List<String> joins = new ArrayList<>();
    private String filter;
    private List<String> groupBy = new ArrayList<>();
    private List<String> orderBy = new ArrayList<>();
    private Map<String, Object> parameters = new HashMap<>();

}