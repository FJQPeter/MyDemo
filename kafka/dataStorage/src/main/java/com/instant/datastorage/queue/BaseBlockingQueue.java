package com.instant.datastorage.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by nijie on 2016/8/31.
 */
public class BaseBlockingQueue {

    private volatile LinkedBlockingQueue<Object> queue;

    public BaseBlockingQueue(int maxSize){
        queue = new LinkedBlockingQueue<Object>(maxSize);
    }

    /**
     * 向队列中放入数据
     * 阻塞方法，当队列满时，会等待直到有空余时再存入数据并返回
     * @param obj
     * @throws InterruptedException
     */
    public void put(Object obj) throws InterruptedException {
        queue.put(obj);
    }

    /**
     * 从队列中获取一条数据
     * 阻塞方法，若队列中无数据则阻塞线程，直到获取到数据才返回
     * @return
     * @throws InterruptedException
     */
    public Object take() throws InterruptedException {
        return queue.take();
    }

    /**
     * 从队列中获取全部数据
     * 非阻塞方法
     * @return
     */
    public List getAllData(){
        List list = new ArrayList();
        while(!queue.isEmpty()){   
            list.add(queue.poll());
        }
        return list;
    }
}
