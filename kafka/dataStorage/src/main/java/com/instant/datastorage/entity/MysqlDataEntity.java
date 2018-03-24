package com.instant.datastorage.entity;

import java.util.Map;

public class MysqlDataEntity {
    private Map<String,MysqlField> fileds;
    private String table_name;
    
	
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public Map<String,MysqlField> getFileds() {
		return fileds;
	}
	public void setFileds(Map<String,MysqlField> fileds) {
		this.fileds = fileds;
	}
	
}
