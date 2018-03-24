package com.instant.datastorage.store;

import com.instant.datastorage.entity.ESDataEntity;
import com.instant.datastorage.entity.RedisDataEntity;
import com.instant.datastorage.queue.CommonDataQueue;
import com.instant.datastorage.store.elasticsearch.ESClient;
import com.instant.datastorage.store.redis.RedisPoolClient;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nijie on 2016/9/6.
 */
public class CommonDataStorage implements Runnable {

    private static final Logger logger = Logger.getLogger(CommonDataStorage.class);

    private final long THREAD_SLEEP_TIME = 300;

    public void run() {
        while (true) {
            long start = System.currentTimeMillis();
            try {
                List data = CommonDataQueue.getInstance().getAllData();
                if(data.size() > 0){
                    List<ESDataEntity> esDatas = new ArrayList();
                    List<RedisDataEntity> redisDatas = new ArrayList();
                    for(Object obj : data){
                        if(obj instanceof ESDataEntity) {
                            esDatas.add((ESDataEntity) obj);
                        }else if(obj instanceof RedisDataEntity){
                            redisDatas.add((RedisDataEntity)obj);
                        }
                    }
                    if(esDatas.size() > 0){
                        ESClient.indexReportData(esDatas);
                    }
                    if(redisDatas.size() > 0){
                        RedisPoolClient.putHash(redisDatas);
                    }
                    logger.info("**** Common data storage. ES Data size:" + esDatas.size() + ", Redis Data size:"+redisDatas.size()+" cost " + (System.currentTimeMillis() - start) + "ms");
                }
            } catch (Exception e) {
                logger.error("CommonDataStorage thread error." + e.getMessage(), e);
            }
            //本次处理花费的时间
            long cost = System.currentTimeMillis() - start;
            if (cost < THREAD_SLEEP_TIME) {
                try {
                    Thread.sleep(THREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
