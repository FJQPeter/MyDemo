package com.instant.datastorage;

import com.instant.datastorage.rule.RuleLoadTool;

import java.io.File;
import java.util.List;

/**
 * Created by nijie on 2016/8/3.
 */
public class TestThread extends Thread {
    @Override
    public void run() {
        try{
            while (true){
                System.out.println("-------------------------------");
                List<File> files = RuleLoadTool.getRulesFiles();
                for(File file : files){
                    System.out.println(file.getAbsolutePath());
                }
                Thread.sleep(5000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new TestThread().start();
    }
}
