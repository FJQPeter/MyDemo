package com.instant.datastorage.entity;

import java.util.Map;

/**
 * Created by nijie on 2016/7/29.
 */
public class KafkaMessage {

    private String topic;
    private String content;
    private Map<String,Object> data;

    public String getTopic(){
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
