package com.es.proxy;

/**
 * Created by FangYan on 2017/10/5 0005.
 */
public class IBosses implements IBoss {
    @Override
    public int yifu(String size) {
        System.out.println("衣服型号"+size);
        return  50;
    }

    public void  kuzi(){
        System.out.println("kuzi");
    }
}
