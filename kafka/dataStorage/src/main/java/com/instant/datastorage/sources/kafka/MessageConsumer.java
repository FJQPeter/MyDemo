package com.instant.datastorage.sources.kafka;

import java.util.Map;

import org.apache.log4j.Logger;

import com.instant.datastorage.entity.KafkaMessage;
import com.instant.datastorage.queue.MessageDataQueue;
import com.instant.datastorage.util.JSONUtil;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;


/**
 * Created by nijie on 2016/5/25.
 */
public class MessageConsumer implements Runnable {

    private static final Logger logger = Logger.getLogger(MessageConsumer.class);
    private KafkaStream<byte[], byte[]> stream;
    private String topic;

    public void run() {
        logger.info("start to consumer topic["+topic+"]");
        MessageDataQueue dataQueue = MessageDataQueue.getInstantce();
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()){
            try {
                String json = new String(it.next().message());
                logger.info("get msg from kafka ["+topic+"] : "+json);
                Map<String, Object> dataMap = JSONUtil.jsonStrToMap(json);
                KafkaMessage msg = new KafkaMessage();
                msg.setTopic(topic);
                msg.setContent(json);
                msg.setData(dataMap);
                dataQueue.put(msg);
            }catch (Exception e){
                logger.error("MessageConsumer error.topic["+topic+"] "+e.getMessage(),e);
            }
        }
        logger.info("Consumer of "+topic+" has shutdown.");
    }

    public KafkaStream getStream() {
        return stream;
    }

    public void setStream(KafkaStream stream) {
        this.stream = stream;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

}
