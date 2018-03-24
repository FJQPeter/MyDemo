package com.instant.datastorage.entity;

public class MysqlField {
	
	/**
	 * 字段名
	 */
	private String field;
	
	/**
	 * 值
	 */
	private Object value;
	
	/**
	 * 字段类型
	 */
	private String type;
	
	public MysqlField() {}
	
	public MysqlField(String field, Object value, String type) {
		this.field = field;
		this.value = value;
		this.type = type;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
