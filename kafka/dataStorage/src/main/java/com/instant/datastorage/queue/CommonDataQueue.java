package com.instant.datastorage.queue;

import com.instant.datastorage.common.Config;
import com.instant.datastorage.entity.ESDataEntity;
import com.instant.datastorage.entity.RedisDataEntity;

import java.util.Map;

/**
 * Created by nijie on 2016/9/6.
 */
public class CommonDataQueue extends BaseBlockingQueue {

    private static CommonDataQueue queue = new CommonDataQueue();

    private CommonDataQueue() {
        super(Config.getInteger("queue.size.data.common"));
    }

    public static CommonDataQueue getInstance(){
        return queue;
    }

    public static void pushEsData(String index,String type,String id,Map<String,Object> data) throws InterruptedException {
        ESDataEntity dataEntity = ESDataEntity.create(index,type,id,data);
        getInstance().put(dataEntity);
    }

    public static void pushRedisData(String key,String field,String value) throws InterruptedException {
        RedisDataEntity entity = new RedisDataEntity();
        entity.setKey(key);
        entity.setField(field);
        entity.setValue(value);
        getInstance().put(entity);
    }
}
