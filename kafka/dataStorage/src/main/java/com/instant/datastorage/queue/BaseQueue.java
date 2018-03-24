package com.instant.datastorage.queue;

import com.instant.datastorage.common.Config;
import com.instant.datastorage.constant.AppConstant;
import com.instant.datastorage.entity.KafkaMessage;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by nijie on 2016/8/1.
 */
@Deprecated
public class BaseQueue {
    private static final Logger logger = Logger.getLogger(BaseQueue.class);
    private volatile Queue<Object> queue = new ConcurrentLinkedQueue();
    private int queueSize = 0;
    public void push(Object message){
        queue.offer(message);
        queueSize++;
    }

    public List getData(){
        //从队列中获取最大数据量
        synchronized (this) {
            int maxFetchSize = Config.getInteger("queue.fetch.size.max");
            List list = new ArrayList();
            int count = 0;
            logger.info(this.toString() + " queue size:" + queueSize);
            while (!queue.isEmpty() && count++ < maxFetchSize) {
                list.add(queue.poll());
            }
            queueSize = queueSize - count;
            return list;
        }
    }

    public List getAllData(){
        synchronized (this) {
            List list = new ArrayList();
            int count = 0;
            logger.info(this.toString() + " queue size:" + queueSize);
            while (!queue.isEmpty()) {
                list.add(queue.poll());
                count++;
            }
            queueSize = queueSize - count;
            return list;
        }
    }
}
