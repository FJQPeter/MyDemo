package com.instant.datastorage.entity;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Created by nijie on 2016/8/2.
 */
public class ESDataEntity {

    private String index;
    private String type;
    private String id;
    private Map<String,Object> content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Object> getContent() {
        return content;
    }

    public void setContent(Map<String, Object> content) {
        this.content = content;
    }

    public static ESDataEntity create(String index,String type,String id,Map<String,Object> content){
        ESDataEntity dataEntity = new ESDataEntity();
        dataEntity.setIndex(index);
        dataEntity.setType(type);
        if(StringUtils.isNotEmpty(id))
            dataEntity.setId(id);
        dataEntity.setContent(content);
        return dataEntity;
    }

}
