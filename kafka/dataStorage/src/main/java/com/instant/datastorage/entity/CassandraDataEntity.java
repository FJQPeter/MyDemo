package com.instant.datastorage.entity;

import java.util.Map;

/**
 * Created by nijie on 2016/8/2.
 */
public class CassandraDataEntity {

    private String table;
    private Map<String,Object> dataMap;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }
}
