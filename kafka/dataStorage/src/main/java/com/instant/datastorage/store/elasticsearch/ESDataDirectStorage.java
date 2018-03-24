package com.instant.datastorage.store.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.instant.datastorage.entity.ESDataEntity;
import com.instant.datastorage.queue.MsgForESQueue;
import com.instant.datastorage.util.JSONUtil;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by nijie on 2016/8/4.
 */
public class ESDataDirectStorage implements Runnable {

    private static final Logger logger = Logger.getLogger(ESDataDirectStorage.class);

    private final long THREAD_SLEEP_TIME = 300;

    public void run() {
        while (true) {
            long start = System.currentTimeMillis();
            try {
                MsgForESQueue queue = MsgForESQueue.getInstance();
                List<ESDataEntity> dataList = queue.getAllData();
//                logger.info("get data from esDataQueue:"+dataList.size());
                if (dataList.size() > 0) {
                    ESClient.indexReportData(dataList);
                    logger.info("**** put into elasticsearch. Data size:" + dataList.size() + " cost " + (System.currentTimeMillis() - start) + "ms");
                }
            } catch (Exception e) {
                logger.error("ESDataDirectStorage thread error." + e.getMessage(), e);
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

    /*private void store(List<ESDataEntity> dataList) throws JsonProcessingException, ExecutionException, InterruptedException, UnknownHostException {
        if(dataList.size() > 0){
            Map<String,Map<String,List<String>>> dataMap = new HashMap();
            for(ESDataEntity dataEntity : dataList) {
                String index = dataEntity.getIndex();
                String type = dataEntity.getType();
                Map<String,List<String>> typeMap = dataMap.get(index);
                if(typeMap == null){
                    typeMap = new HashMap();
                    dataMap.put(index,typeMap);
                }
                List<String> datas = typeMap.get(type);
                if(datas == null){
                    datas = new ArrayList();
                    typeMap.put(type,datas);
                }
                datas.add(JSONUtil.mapToJsonStr(dataEntity.getContent()));
            }
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<String,Map<String,List<String>>> typeEntity : dataMap.entrySet()) {
                String index = typeEntity.getKey();
                Map<String, List<String>> types = typeEntity.getValue();
                for (Map.Entry<String, List<String>> typeData : types.entrySet()) {
                    String type = typeData.getKey();
                    List<String> datas = typeData.getValue();
                    ESClient.indexReportData(datas, index, type);
                    sb.append("[index:").append(index)
                            .append(",type:").append(type)
                            .append(",rows:").append(datas.size()).append("]");
                }
            }
            logger.info("put into elasticsearch "+sb.toString()+" cost " +(System.currentTimeMillis()-start)+"ms");
        }
    }*/
}
