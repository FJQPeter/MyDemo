package com.instant.datastorage.util;

import org.apache.log4j.Logger;

/**
 * Created by nijie on 2016/9/7.
 */
public class LogUtil {
    private static final Logger logger = Logger.getLogger(LogUtil.class);

    public static void info(String content){
        logger.info(content);
    }


}
