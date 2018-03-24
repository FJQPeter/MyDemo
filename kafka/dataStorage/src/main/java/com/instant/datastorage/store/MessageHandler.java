package com.instant.datastorage.store;

import com.instant.datastorage.entity.KafkaMessage;
import com.instant.datastorage.queue.MessageDataQueue;
import com.instant.datastorage.rule.DroolsFactory;
import com.instant.datastorage.store.cassandra.CassandraDataDirectStorage;
import com.instant.datastorage.store.elasticsearch.ESDataDirectStorage;
import com.instant.datastorage.store.mysql.MysqlStorage;

import org.apache.log4j.Logger;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;


/**
 * kafaka消息处理线程
 * Created by nijie on 2016/7/29.
 */
public class MessageHandler implements Runnable {

    private static final Logger logger = Logger.getLogger(MessageHandler.class);

    public void run() {
        MessageDataQueue dataQueue = MessageDataQueue.getInstantce();
//        new Thread(new CassandraDataDirectStorage()).start();
//        new Thread(new ESDataDirectStorage()).start();
//        new Thread(new CommonDataStorage()).start();
        new Thread(new MysqlStorage()).start();
        KafkaMessage message = null;
        while (true) {
            try {
                KieSession kieSession = DroolsFactory.getInstance().getKieSession();
                //从消息队列获取数据，提交至规则引擎进行处理
                message = (KafkaMessage)dataQueue.take();
                FactHandle fh = kieSession.insert(message);
                kieSession.fireAllRules();
                kieSession.delete(fh);
            }catch (Exception e){
                logger.error("message content:"+message.getContent());
                logger.error("MessageHandler thread error:"+e.getMessage(),e);
            }
        }
    }
}
