package com.instant.datastorage.common;

import com.instant.datastorage.entity.DataMappingInfo;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息与数据库映射关系配置
 * Created by nijie on 2016/7/29.
 */
@Deprecated
public class DataMappingConfig {

    private static final Logger logger = Logger.getLogger(DataMappingConfig.class);

    //数据库表字段映射类型
    public enum fieldMatchType{
        ;
        //全部需要映射，消息json中未匹配中的属性将忽略
        public static final String FULL_MATCH = "FULL_MATCH";
        //部分匹配，消息json中未匹配的属性将被认为数据库表中有同名字段，不需转换直接保存字段值
        public static final String PARTLY_MATCH = "PARTLY_MATCH";
        //不需要映射（消息json中属性与数据库字段一一对应）
        public static final String NO_MATCHING = "NO_MATCHING";
    }
    //数据库类型
    public enum databaseType{
        ;
        public static final String ELASTIC_SEARCH = "elasticsearch";
        public static final String CASSANDRA = "cassandra";
    }

    private static long lastFileModifyTime = -1;

    private static Map<String,DataMappingInfo> databaseMappings;

    public static void loadConfigFile() throws IOException, DocumentException {
        logger.info("start to load database-mapping config file");
        URL url = DataMappingConfig.class.getResource("/database-mapping.xml");
        String filePath = url.getFile();
        File file = new File(filePath);
        //判断文件修改时间是否与记录的时间相同
        long modifyTime = file.lastModified();
        if(lastFileModifyTime == modifyTime){
            logger.info("database-mapping config file not modify.stop load.");
            return;
        }else{
            lastFileModifyTime = modifyTime;
        }
        SAXReader reader = new SAXReader();
        Document doc = reader.read(file);
        List<Element> mappings = doc.selectNodes("/mappings/mapping");
        Map<String,DataMappingInfo> map = new HashMap<String, DataMappingInfo>();
        for(Element mappingEle : mappings){
            String dbType = mappingEle.elementText("database");
            String tableName = mappingEle.elementText("tableName");
            String matchType = mappingEle.elementText("matchType");
            Map<String,String> fieldMap = null;
            if(!fieldMatchType.NO_MATCHING.equals(matchType)) {
                fieldMap = new HashMap();
                List<Element> fieldEles = mappingEle.element("fields").elements("value");
                for (Element fieldEle : fieldEles) {
                    String[] strArr = fieldEle.getText().split(":");
                    fieldMap.put(strArr[0],strArr[1]);
                }
            }
            DataMappingInfo mappingInfo = new DataMappingInfo();
            mappingInfo.setTableName(tableName);
            mappingInfo.setMatchType(matchType);
            mappingInfo.setFieldMapping(fieldMap);
            String key = buildConfigMapKey(dbType,tableName);
            map.put(key,mappingInfo);
        }
        databaseMappings = map;
        logger.info("load database-mapping config file finish.");
    }

    public static DataMappingInfo getESConfig(String index,String type){
        String tableName = index + "_" + type;
        return databaseMappings.get(buildConfigMapKey(databaseType.ELASTIC_SEARCH,tableName));
    }

    public static DataMappingInfo getCassandraConfig(String tableName){
        return databaseMappings.get(buildConfigMapKey(databaseType.CASSANDRA,tableName));
    }

    private static String buildConfigMapKey(String dbType,String tableName){
        return dbType + "_" + tableName;
    }

    public static void main(String[] args) throws IOException, DocumentException {
//        URL url = DataMappingConfig.class.getResource("/database-mapping.xml");
//        String filePath = url.getFile();
//        System.out.println(url.getFile());
//        System.out.println(new File(filePath).lastModified());
        DataMappingConfig.loadConfigFile();
    }
}
