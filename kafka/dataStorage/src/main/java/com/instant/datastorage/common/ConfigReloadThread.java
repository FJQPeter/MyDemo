package com.instant.datastorage.common;

import com.instant.datastorage.rule.RuleLoadTool;
import org.apache.log4j.Logger;

/**
 * 配置文件重新加载线程
 * Created by nijie on 2016/8/2.
 */
public class ConfigReloadThread extends Thread {

    private static final Logger logger = Logger.getLogger(ConfigReloadThread.class);
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(20 * 1000);
//                DataMappingConfig.loadConfigFile();
                RuleLoadTool.loadRules();
            }catch (Exception e){
                logger.error("ConfigReloadThread error."+e.getMessage(),e);
            }
        }
    }
}
