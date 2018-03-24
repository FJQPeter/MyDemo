package com.instant.datastorage.entity;

import java.util.Map;

/**
 * Created by nijie on 2016/8/2.
 */
public class DataMappingInfo {

    private String tableName;

    private String matchType;

    private Map<String,String> fieldMapping;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(Map<String, String> fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }
}
