package com.instant.datastorage;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.instant.datastorage.common.Config;
import com.instant.datastorage.common.ConfigReloadThread;
import com.instant.datastorage.common.DataMappingConfig;
import com.instant.datastorage.rule.RuleLoadTool;
import com.instant.datastorage.sources.kafka.KafkaConsumerManager;
import com.instant.datastorage.store.MessageHandler;
import com.instant.datastorage.store.cassandra.CassandraPooledClient;

/**
 * Created by nijie on 2016/7/28.
 */
public class StorageMain {

    private static final Logger logger = Logger.getLogger(StorageMain.class);

    private static void init() throws IOException, DocumentException {
        Config.init();
   //     DataMappingConfig.loadConfigFile();
        RuleLoadTool.loadRules();
        //初始化Cassandra连接
       // CassandraPooledClient.init();
    }

    public static void main(String[] args) {
        try {
            init();

            new ConfigReloadThread().start();
            //启动kafka消息消费监听
            new Thread(new KafkaConsumerManager()).start();
            //启动消息处理线程
            new Thread(new MessageHandler()).start();
            
        }catch (Exception e){
            logger.error("StorageMain error.",e);
        }
    }


}
