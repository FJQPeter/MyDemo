package com.instant.datastorage.store.elasticsearch;

import com.instant.datastorage.common.DataMappingConfig;
import com.instant.datastorage.entity.DataMappingInfo;
import com.instant.datastorage.entity.ESDataEntity;
import com.instant.datastorage.queue.MsgForESQueue;
import com.instant.datastorage.util.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Created by nijie on 2016/8/1.
 */
public class ESStorage implements Runnable {

    private static final Logger logger = Logger.getLogger(ESStorage.class);

    private final long THREAD_SLEEP_TIME = 300;

    public void run() {
        long start;
        try{
            MsgForESQueue queue = MsgForESQueue.getInstance();
            while (true) {
                start = System.currentTimeMillis();
                List<ESDataEntity> dataList = queue.getAllData();
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
                    for(Map.Entry<String,Map<String,List<String>>> typeEntity : dataMap.entrySet()) {
                        String index = typeEntity.getKey();
                        Map<String,List<String>> types = typeEntity.getValue();
                        for(Map.Entry<String,List<String>> typeData : types.entrySet()) {
                            String type = typeData.getKey();
                            List<String> datas = null;
                            DataMappingInfo mappingInfo = DataMappingConfig.getESConfig(index,type);
                            if(mappingInfo.getMatchType().equals(DataMappingConfig.fieldMatchType.NO_MATCHING)){
                                datas = typeData.getValue();
                            }else if(mappingInfo.getMatchType().equals(DataMappingConfig.fieldMatchType.FULL_MATCH)){
                                datas = filterDataWithFull(typeData.getValue(),mappingInfo);
                            }else if(mappingInfo.getMatchType().equals(DataMappingConfig.fieldMatchType.PARTLY_MATCH)){
                                datas = filterDataWithPartly(typeData.getValue(),mappingInfo);
                            }
                            ESClient.indexReportData(datas, index, type);
                            logger.info("put into elasticsearch[index:"+index+",type:"+type+"] "+datas.size()+"rows");
                        }
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

    private List<String> filterDataWithFull(List<String> msgJsons,DataMappingInfo config) throws IOException {
        List<String> esJsons = new ArrayList();
        Map<String,String> fieldMapping = config.getFieldMapping();
        for(String msgJson : msgJsons){
            Map<String,Object> msgMap = JSONUtil.jsonStrToMap(msgJson);
            Map<String,Object> esMap = new HashMap();
            for(Map.Entry<String,String> entry : fieldMapping.entrySet()){
                String msgKey = entry.getKey();
                String esKey = entry.getValue();
                esMap.put(esKey,msgMap.get(msgKey));
            }
            esJsons.add(JSONUtil.mapToJsonStr(esMap));
        }
        return esJsons;
    }

    private List<String> filterDataWithPartly(List<String> msgJsons,DataMappingInfo config) throws IOException {
        List<String> esJsons = new ArrayList();
        Map<String,String> fieldMapping = config.getFieldMapping();
        for(String msgJson : msgJsons){
            Map<String,Object> msgMap = JSONUtil.jsonStrToMap(msgJson);
            Map<String,Object> esMap = new HashMap();
            for(Map.Entry<String,Object> entry : msgMap.entrySet()){
                String msgKey = entry.getKey();
                Object value = entry.getValue();
                String esKey = fieldMapping.get(msgKey);
                if(StringUtils.isNotEmpty(esKey)){
                    esMap.put(esKey,value);
                }else{
                    esMap.put(msgKey,value);
                }
            }
            esJsons.add(JSONUtil.mapToJsonStr(esMap));
        }
        return esJsons;
    }
}
