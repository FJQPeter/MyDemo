package com.es.proxy;

import org.junit.Test;

/**
 * Created by FangYan on 2017/10/6 0006.
 */
public class ProxySaleAction {
    @Test
        public void saleByproxy(){
            IBoss proxy = proxyBoss.getProxy(10,IBoss.class,IBosses.class);
            System.out.println("代理经营");
            int count = proxy.yifu("xxl");
            System.out.println("成交价"+count);
        }

}
