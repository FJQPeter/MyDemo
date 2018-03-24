package com.instant.datastorage.store.cassandra;

import com.instant.datastorage.common.DataMappingConfig;
import com.instant.datastorage.entity.CassandraDataEntity;
import com.instant.datastorage.entity.DataMappingInfo;
import com.instant.datastorage.entity.KafkaMessage;
import com.instant.datastorage.queue.MsgForCassandraQueue;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nijie on 2016/8/1.
 */
public class CassandraStorage implements Runnable {

    private static final Logger logger = Logger.getLogger(CassandraStorage.class);
    private final long THREAD_SLEEP_TIME = 300;

    public void run() {
        long start;
        try{
            MsgForCassandraQueue queue = MsgForCassandraQueue.getInstance();
            while (true) {
                start = System.currentTimeMillis();
                List<CassandraDataEntity> dataList = queue.getAllData();
                if(dataList.size() > 0){
                    Map<String,List<Map<String,Object>>> dataMap = new HashMap();
                    for(CassandraDataEntity dataEntity : dataList) {
                        String tableName = dataEntity.getTable();
                        List<Map<String,Object>> datas = dataMap.get(tableName);
                        if(datas == null){
                            datas = new ArrayList();
                            dataMap.put(tableName,datas);
                        }
                        datas.add(dataEntity.getDataMap());
                    }
                    for(Map.Entry<String,List<Map<String,Object>>> entry : dataMap.entrySet()){
                        String tableName = entry.getKey();
                        DataMappingInfo mappingInfo = DataMappingConfig.getCassandraConfig(tableName);
                        List<Map<String,Object>> datas = null;
                        if(mappingInfo.getMatchType().equals(DataMappingConfig.fieldMatchType.NO_MATCHING)){
                            datas = entry.getValue();
                        }else if(mappingInfo.getMatchType().equals(DataMappingConfig.fieldMatchType.FULL_MATCH)){
                            datas = filterDataWithFull(entry.getValue(),mappingInfo);
                        }else if(mappingInfo.getMatchType().equals(DataMappingConfig.fieldMatchType.PARTLY_MATCH)){
                            datas = filterDataWithPartly(entry.getValue(),mappingInfo);
                        }
                        if(datas != null && datas.size() >0)
                            CassandraPooledClient.batchInsert(datas,entry.getKey());
                        logger.info("put into cassandra["+entry.getKey()+"] "+entry.getValue().size()+"rows");
                    }
                }
                //本次处理花费的时间
                long cost = System.currentTimeMillis() - start;
                if (cost < THREAD_SLEEP_TIME) {
                    Thread.sleep(THREAD_SLEEP_TIME);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private List<Map<String,Object>> filterDataWithFull(List<Map<String,Object>> msgDatas,DataMappingInfo config){
        List<Map<String,Object>> list = new ArrayList();
        Map<String,String> fieldMapping = config.getFieldMapping();
        for(Map<String,Object> msgData : msgDatas){
            Map<String,Object> cassData = new HashMap();
            for(Map.Entry<String,String> entry : fieldMapping.entrySet()){
                String msgKey = entry.getKey();
                String cassKey = entry.getValue();
                cassData.put(cassKey,msgData.get(msgKey));
            }

            list.add(cassData);
        }
        return list;
    }

    private List<Map<String,Object>> filterDataWithPartly(List<Map<String,Object>> msgDatas,DataMappingInfo config){
        List<Map<String,Object>> list = new ArrayList();
        Map<String,String> fieldMapping = config.getFieldMapping();
        for(Map<String,Object> msgData : msgDatas){
            Map<String,Object> cassData = new HashMap();
            for(Map.Entry<String,Object> msg : msgData.entrySet()){
                String msgKey = msg.getKey();
                Object value = msg.getValue();
                String cassKey = fieldMapping.get(msgKey);
                if(StringUtils.isNotEmpty(cassKey))
                    cassData.put(cassKey,value);
                else
                    cassData.put(msgKey,value);
            }
            list.add(cassData);
        }
        return list;
    }
}
