package com.es.proxy;

import org.junit.Test;

/**
 * Created by FangYan on 2017/10/5 0005.
 */
public class SaleAction {
    @Test
    public void saleByBoss(){
        IBoss iBoss = new IBosses();
        System.out.println("老板自营");
        int xxl = iBoss.yifu("xxl");
        System.out.println("衣服成交价"+xxl);
    }

}