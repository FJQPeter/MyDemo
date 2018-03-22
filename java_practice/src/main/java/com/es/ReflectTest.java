package com.es;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by FangYan on 2017/10/5 0005.
 */
public   class ReflectTest {
    public static void main(String[] args) {
        try {
            Class<?> person = Class.forName("com.es.Person");
                Constructor<?> constructor = person.getConstructor(String.class, Integer.class);
            Object o1 = constructor.newInstance("小明", 15);
            Constructor<?> conn = person.getDeclaredConstructor(String.class);
            conn.setAccessible(true);
            Object o2 = conn.newInstance("文文");
            System.out.println(o2);
            System.out.println(person.getMethod("school").toString());
            Object school = person.getMethod("school").invoke(o1);//让o1对象执行school方法
            System.out.println(school);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
