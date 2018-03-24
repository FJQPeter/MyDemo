package com.instant.datastorage.queue;


import com.instant.datastorage.common.Config;

/**
 * Created by nijie on 2016/7/29.
 */
public class MessageDataQueue extends BaseBlockingQueue{

    private static MessageDataQueue dataQueue = new MessageDataQueue();

    private MessageDataQueue(){
        super(Config.getInteger("queue.size.message"));
    }

    public static MessageDataQueue getInstantce(){
        return dataQueue;
    }

}
