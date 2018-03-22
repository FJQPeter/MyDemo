package com.es;

import java.util.ArrayList;

/**
 * 我们通过下面的一段代码体验一下它的监视功能。
 * 运行时设置的虚拟机参数为：-Xms100m -Xmx100m -XX:+UseSerialGC，
 * 这段代码的作用是以64kb/50毫秒的速度往java堆内存中填充数据。
 * Created by FangYan on 2017/10/4 0004.
 */
public class TestMemory {
    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws Exception {
        ArrayList<OOMObject> list = new ArrayList<OOMObject>();
        for (int i = 0; i < num; i++) {
            Thread.sleep(50);
            list.add(new OOMObject());
        }

    }

    public static void main(String[] args) throws Exception {
        fillHeap(1000);
        System.gc();
        Thread.sleep(500000);
    }
}