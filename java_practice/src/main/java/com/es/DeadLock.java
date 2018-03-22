package com.es;

import java.util.concurrent.locks.Lock;

/**
 * 死锁
 * Created by FangYan on 2017/10/5 0005.
 */
 class DeadLock implements  Runnable {
    private int num = 100;
    Object obj = new Object();
    boolean flag = true;

    @Override
    public void run() {
        if (flag) {
            while (true) {
                synchronized (obj) {
                   /* if (num > 0) {
                           *//* try {
                                Thread.sleep((10));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                System.out.println(Thread.currentThread().getName()+"****obj****"+num--);
                            }*//*
                        show();
                    } else {
                        while ((true)) {
                            this.show();
                        }
                    }*/
                    try {
                        Thread.sleep(100);
                        System.out.println(Thread.currentThread().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    show();
                }
            }
        }else {
            while (true)
                this.show();
        }
    }

    // 同步函数用的是this锁
    // 函数需要被对象调用。那么函数都有一个所属对象调用，就是this 所以同步函数使用的锁是this
    public synchronized void show() {
        synchronized (obj) {
            if (num > 0) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "******sale****" + num--);
            }else{
                System.out.println(Thread.currentThread().getName()+"我来了");
            }
        }
    }
}
    class Dead{
        public static void main(String[] args) {
                DeadLock deadLock = new DeadLock();
                Thread t1= new Thread(deadLock);
                Thread t2 = new Thread(deadLock);
                t1.start();
            try {
                //这里指的是主线程休息,这里主线程不休息，无法区分t1,t2谁先启动
                Thread.sleep(100); //Thread 1 sleep的时候根本就没有占据锁。因为sleep的方法在同步关键字外面执行的。
                System.out.println(Thread.currentThread().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            deadLock.flag=false;
            t2.start();
            System.out.println("t2启动了e");
        }
    }
