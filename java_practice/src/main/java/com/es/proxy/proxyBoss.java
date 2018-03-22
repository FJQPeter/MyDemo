package com.es.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by FangYan on 2017/10/6 0006.
 */
public class proxyBoss {
    public static <T> T getProxy(final int discount,final Class<?> interfaceClass,final Class<T> implementClass){
       return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),new Class[] {interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Integer invoke = (Integer) method.invoke(implementClass.newInstance(), args);
                        return invoke-discount;
                    }
                });
    }

}
