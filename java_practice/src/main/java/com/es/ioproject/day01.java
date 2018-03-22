package com.es.ioproject;

import org.junit.Test;

import javax.imageio.stream.ImageInputStream;
import java.io.*;

/**
 * Created by FangYan on 2018/1/3 0003.
 */
public class day01 {
    /**
     * 不带缓冲的操作，每读一个字节就要写入一个字节，
     * 由于涉及磁盘的IO操作相比内存的操作要慢很多，
     * 所以不带缓冲的流效率很低。带缓冲的流，可以一次读很多字节，
     * 但不向磁盘中写入，只是先放到内存里。等凑够了缓冲区大小的时候一次性写入磁盘，
     * 这种方式可以减少磁盘操作次数，速度就会提高很多！这就是两者的区别
     * BufferedInputStream默认为8K
     */

    @Test
    public void test() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File("d/"));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
    }

    /**
     * String 是final ，final 用来修饰类，表示此类不可以被继承，也就是此类没有子类
     * 对于String来说，如果一个变量str的类型是String的，那么str.class==String.class一定是成立的
     如果不是final类，如Student继承Person类，那么 Person p=new Student()，那么显然p.class !=Person.class
     lz你改变的变量的引用或指针，并不是改变了变量所指向的那个对象的值
     */
    @Test
    public void test2() throws InterruptedException {
        String a ="123";//那么在String常量池中先生成一个String对象"123"，s是该对象的引用
        System.out.println(a.hashCode());
//        同样在String常量池中创建一个String对象"1213"，s重新指向了"1213"
        //"123"仍然是"123"，并没有被修改。
//        只是a的指向发生了变化。
        a="1213";
        System.out.println(a.hashCode());
        System.gc();
        Thread.sleep(10000);
        System.out.println("123".hashCode());
    }
}
