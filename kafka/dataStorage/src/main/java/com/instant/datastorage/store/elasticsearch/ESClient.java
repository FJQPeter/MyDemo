package com.instant.datastorage.store.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.instant.datastorage.common.Config;
import com.instant.datastorage.entity.ESDataEntity;
import com.instant.datastorage.util.JSONUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by nijie on 2016/8/1.
 */
public class ESClient {

    private static final Logger logger = Logger.getLogger(ESClient.class);
    private static TransportClient esClient;

    public static TransportClient getClient() throws UnknownHostException {
        if(esClient == null) {
            String clusterName = Config.getString("es.cluster.name");
            if(StringUtils.isEmpty(clusterName)){
                clusterName = "elasticsearch";
            }
            Settings settings = Settings.settingsBuilder().put("cluster.name",clusterName).build();
            esClient = TransportClient.builder().settings(settings).build();
            int port = Config.getInteger("es.port");
            String host = Config.getString("es.host");
            logger.info("create es client [clusterName="+clusterName+"; host="+host+"; port="+port+"]");
            String[] hosts = host.split(",");
            for(String str : hosts) {
                esClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(str), port));
            }
        }
        return esClient;
    }

    public static void indexReportData(List<String> dataList, String indexName,String typeName) throws UnknownHostException, JsonProcessingException, ExecutionException, InterruptedException {
//        ObjectMapper mapper = new ObjectMapper();
        if(CollectionUtils.isEmpty(dataList))
            return;
        TransportClient client = getClient();
//        createIndexIfNotExist(indexName);

        BulkRequestBuilder bulkBuilder = client.prepareBulk();
        for(String data : dataList) {
            bulkBuilder.add(client.prepareIndex(indexName,typeName).setSource(data));
        }
        BulkResponse bulkResponse = bulkBuilder.get();
        if(bulkResponse.hasFailures()){
            logger.warn("bulk index["+indexName+"] TYPE["+typeName+"] failure message:"+bulkResponse.buildFailureMessage());
//            for(BulkItemResponse itemResponse :bulkResponse.getItems()){
//            }
        }
    }

    public static void indexReportData(List<ESDataEntity> dataList) throws UnknownHostException {
        if(CollectionUtils.isEmpty(dataList))
            return;
        TransportClient client = getClient();
        BulkRequestBuilder bulkBuilder = client.prepareBulk();
        for(ESDataEntity dataEntity : dataList){
//            String json = JSONUtil.mapToJsonStr(dataEntity.getContent());
            String id = dataEntity.getId();
            //id不为空则设置ID字段值，否则由es服务自动生成
            if(StringUtils.isNotEmpty(id)){
                bulkBuilder.add(client.prepareIndex(dataEntity.getIndex(),dataEntity.getType(),id).setSource(dataEntity.getContent()));
            }else {
                bulkBuilder.add(client.prepareIndex(dataEntity.getIndex(),dataEntity.getType()).setSource(dataEntity.getContent()));
            }
        }
        BulkResponse bulkResponse = bulkBuilder.get();
        if(bulkResponse.hasFailures()){
            logger.warn("bulk failure message:"+bulkResponse.buildFailureMessage());
//            for(BulkItemResponse itemResponse :bulkResponse.getItems()){
//            }
        }
    }
}
