package com.instant.datastorage.sources.kafka;


import com.instant.datastorage.common.Config;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Kafka消费者管理类
 *
 * 注意：新增的topic监听会从kafka的第一条记录开始读取
 * Created by nijie on 2016/5/24.
 */
public class KafkaConsumerManager implements Runnable{

    private static final Logger logger = Logger.getLogger(KafkaConsumerManager.class);

    private static final long CONFIG_REFRESH_TIME = 5*60*1000;

    //正在执行监听的消费者
    private Map<String,ConsumerConnector> consumers = new HashMap();

    private ExecutorService cachedThreadPool;
    // TODO: 2016/7/29 此处监听的topic列表，应该从远程配置库读取
    private List<String> consumeTopics = Arrays.asList(Config.getString("consumer.topics").split(","));

    public KafkaConsumerManager(){
        this.cachedThreadPool = Executors.newCachedThreadPool();
    }

    public void run() {
        logger.info("--- Kafka consumer manager thread start. ---");
        while (true){
            try {
                Set<String> consumingTopics = consumers.keySet();
                logger.info("Topics config list:" + consumeTopics + " Topic consuming list:" + consumingTopics);
                //筛选出需要停止监听的topic列表
                List<String> topicToRemove = new ArrayList();
                for (String topic : consumingTopics) {
                    if (!consumeTopics.contains(topic))
                        topicToRemove.add(topic);
                }
                for (String topic : topicToRemove) {
                    removeConsumer(topic);
                }
                //增加新的topic监听任务
                consumingTopics = consumers.keySet();
                for (String topic : consumeTopics) {
                    if (!consumingTopics.contains(topic))
                        addConsumer(topic);
                }
            }catch (Exception e){
                logger.error("KafkaConsumerManager thread error:"+e.getMessage(),e);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(KafkaConsumerManager.CONFIG_REFRESH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param topic
     */
    private void addConsumer(String topic){
        if(consumers.get(topic) != null){
            logger.warn("start topic["+topic+"] consumer fail.It's already running.");
            return;
        }
        ConsumerConnector consumer = createConsumer();
        consumers.put(topic,consumer);
        List<KafkaStream<byte[], byte[]>> streams = consumer.createMessageStreamsByFilter(new Whitelist(""+topic+""));
        KafkaStream stream = streams.get(0);
        MessageConsumer messageConsumer = new MessageConsumer();
        messageConsumer.setStream(stream);
        messageConsumer.setTopic(topic);
        
        cachedThreadPool.submit(messageConsumer);
    }

    /**
     * 停止对某个topic的消息监听
     * @param topic
     */
    private void removeConsumer(String topic){
        ConsumerConnector consumer = consumers.get(topic);
        if(consumer == null)
            return;
        consumer.shutdown();
        consumers.remove(topic);
    }

    private ConsumerConnector createConsumer(){
        Properties props = new Properties();
        props.put("zookeeper.connect", Config.getString("zookeeper.connect"));
        props.put("group.id", Config.getString("group.id"));
        props.put("zookeeper.session.timeout.ms", Config.getString("zookeeper.session.timeout.ms"));
        props.put("zookeeper.sync.time.ms", Config.getString("zookeeper.sync.time.ms"));
        props.put("auto.commit.interval.ms", Config.getString("auto.commit.interval.ms"));
        props.put("fetch.message.max.bytes",Config.getString("fetch.message.max.bytes"));
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
        return consumerConnector;
    }
}
