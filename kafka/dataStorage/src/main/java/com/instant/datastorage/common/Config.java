package com.instant.datastorage.common;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Created by nijie on 2016/7/29.
 */
public class Config {
    private static final Logger logger = Logger.getLogger(Config.class);
    private static Properties properties = new Properties();

    public static void init(){
        logger.info("---------- load config start ---------");
        InputStream in = null;
        try{
            in = Config.class.getResourceAsStream("/config.properties");
            properties.load(in);

            for(Map.Entry entry : properties.entrySet()){
                logger.info(entry.getKey().toString() + " : " + entry.getValue().toString());
            }
            logger.info("---------- load config end ---------");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static String getString(String key){
        return properties.getProperty(key);
    }

    public static Integer getInteger(String key){
        String str = properties.getProperty(key);
        if(str != null && str.length() >0)
            return Integer.valueOf(str);
        return null;
    }
}
