package com.instant.datastorage.queue;


import java.util.Map;

import com.instant.datastorage.common.Config;
import com.instant.datastorage.entity.MysqlDataEntity;
import com.instant.datastorage.entity.MysqlField;

public class MsgForMysqlQueue extends BaseBlockingQueue{
	private static MsgForMysqlQueue queue = new MsgForMysqlQueue();	
	
    public MsgForMysqlQueue(){
        super(Config.getInteger("queue.size.data.mysql"));
    }

    public static MsgForMysqlQueue getInstance(){
        return queue;
    }

    public static void pushMsg(String table_name,Map<String,MysqlField> map) throws InterruptedException {
        MysqlDataEntity dataEntity = new MysqlDataEntity();
        dataEntity.setTable_name(table_name);
        dataEntity.setFileds(map);
        
        MsgForMysqlQueue.getInstance().put(dataEntity);
    }
    
}
