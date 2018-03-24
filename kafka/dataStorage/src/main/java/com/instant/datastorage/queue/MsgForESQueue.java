package com.instant.datastorage.queue;

import com.instant.datastorage.common.Config;
import com.instant.datastorage.entity.ESDataEntity;

import java.util.Map;

/**
 * Created by nijie on 2016/8/1.
 */
public class MsgForESQueue extends BaseBlockingQueue {

    private static MsgForESQueue queue = new MsgForESQueue();

    private MsgForESQueue(){
        super(Config.getInteger("queue.size.data.es"));
    }

    public static MsgForESQueue getInstance(){
        return queue;
    }

    public static void pushMsg(String index,String type,String id,Map<String,Object> data) throws InterruptedException {
        ESDataEntity dataEntity = ESDataEntity.create(index,type,id,data);
        getInstance().put(dataEntity);
    }

}
