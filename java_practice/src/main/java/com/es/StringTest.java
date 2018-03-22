package com.es;

import javafx.application.Application;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Created by FangYan on 2017/10/4 0004.
 */
public class StringTest {
    public static void main(String[] args) {
        boolean empty = StringUtils.isEmpty(" ");
        int length = " ".trim().length();
        System.out.println(empty);
        System.out.println(length);
        ApplicationContext ac = new FileSystemXmlApplicationContext("classpath:applicationContext.xml");
//        Person person = SpringApplicationContext.getBean("person");
        Person person = (Person) ac.getBean("person");
        String school = person.school();
        System.out.println(school);
    }

}
