package com.instant.datastorage.store.cassandra;

import com.instant.datastorage.entity.CassandraDataEntity;
import com.instant.datastorage.queue.MsgForCassandraQueue;
import com.instant.datastorage.util.JSONUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nijie on 2016/8/4.
 */
public class CassandraDataDirectStorage implements Runnable{

    private static final Logger logger = Logger.getLogger(CassandraDataDirectStorage.class);
    private final long THREAD_SLEEP_TIME = 300;

    public void run() {
        MsgForCassandraQueue queue = MsgForCassandraQueue.getInstance();
        while (true) {
            try {
                long start = System.currentTimeMillis();
                List<CassandraDataEntity> dataList = queue.getAllData();
                if (dataList.size() > 0) {
                    Map<String, List<Map<String, Object>>> dataMap = new HashMap();
                    for (CassandraDataEntity entity : dataList) {
                        String tableName = entity.getTable();
                        List<Map<String, Object>> datas = dataMap.get(tableName);
                        if (datas == null) {
                            datas = new ArrayList();
                            dataMap.put(tableName, datas);
                        }
                        datas.add(entity.getDataMap());
                    }
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, List<Map<String, Object>>> entry : dataMap.entrySet()) {
                        String table = entry.getKey();
                        List<Map<String, Object>> datas = entry.getValue();
                        CassandraPooledClient.batchInsert(datas, table);
                        sb.append("[table:").append(table)
                                .append(",rows:").append(datas.size()).append("]");
                    }
                    logger.info("put into cassandra " + sb.toString() + " cost " + (System.currentTimeMillis() - start) + "ms");
                }
                //本次处理花费的时间
                long cost = System.currentTimeMillis() - start;
                if (cost < THREAD_SLEEP_TIME) {
                    Thread.sleep(THREAD_SLEEP_TIME);
                }
            }catch (Exception e){
                logger.error("CassandraDataDirectStorage thread error:"+e.getMessage(),e);
            }
        }

    }
}
