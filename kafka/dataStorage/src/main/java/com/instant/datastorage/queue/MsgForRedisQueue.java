package com.instant.datastorage.queue;

import com.instant.datastorage.common.Config;

/**
 * Created by nijie on 2016/9/5.
 */
public class MsgForRedisQueue extends BaseBlockingQueue {

    private static MsgForRedisQueue queue = new MsgForRedisQueue();

    public MsgForRedisQueue() {
        super(Config.getInteger("queue.size.data.redis"));
    }

    public static MsgForRedisQueue getInstance(){
        return queue;
    }
}
