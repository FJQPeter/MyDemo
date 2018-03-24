package com.instant.datastorage.store.redis;

import com.instant.datastorage.entity.RedisDataEntity;
import com.instant.datastorage.queue.MsgForRedisQueue;
import org.apache.log4j.Logger;

/**
 * Created by nijie on 2016/9/5.
 */
public class RedisDataStorage implements Runnable{

    private static final Logger logger = Logger.getLogger(RedisDataStorage.class);

    private final long THREAD_SLEEP_TIME = 300;

    public void run() {
        MsgForRedisQueue queue = MsgForRedisQueue.getInstance();
        while (true) {
            try {
                long start = System.currentTimeMillis();
                RedisDataEntity data = (RedisDataEntity)queue.take();

            }catch (Exception e){
                logger.error("RedisDataStorage thread error."+e.getMessage(),e);
            }
        }
    }

    private void setValue(){

    }
}
