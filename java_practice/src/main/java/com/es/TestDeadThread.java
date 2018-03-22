package com.es;

/**
 * Created by FangYan on 2017/10/4 0004.
 */
public class TestDeadThread implements Runnable {
    int a, b;

    public TestDeadThread(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        System.out.println("createDeadThread");
        synchronized (Integer.valueOf(a)) {  //其中的代码必须获得对象 syncObject
            synchronized (Integer.valueOf(b)) {
                System.out.println(a + b);
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new TestDeadThread(1, 2)).start();
            new Thread(new TestDeadThread(2, 1)).start();
        }
    }
}
