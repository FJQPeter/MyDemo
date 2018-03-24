package com.instant.datastorage.queue;

import com.instant.datastorage.common.Config;
import com.instant.datastorage.entity.CassandraDataEntity;

import java.util.Map;

/**
 * Created by nijie on 2016/8/1.
 */
public class MsgForCassandraQueue extends BaseBlockingQueue {

    private static MsgForCassandraQueue queue = new MsgForCassandraQueue();

    private MsgForCassandraQueue(){
        super(Config.getInteger("queue.size.data.cassandra"));
    }

    public static MsgForCassandraQueue getInstance(){
        return queue;
    }

    public static void pushMsg(String table,Map<String,Object> data) throws InterruptedException {
        CassandraDataEntity entity = new CassandraDataEntity();
        entity.setTable(table);
        entity.setDataMap(data);
        MsgForCassandraQueue.getInstance().put(entity);
    }


}
