package com.instant.datastorage.store.cassandra;

import com.datastax.driver.core.*;
import com.instant.datastorage.common.Config;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 带连接池的cassandra客户端
 * Created by nijie on 2016/5/23.
 */
public class CassandraPooledClient {

    private static Logger log = Logger.getLogger(CassandraPooledClient.class);
    private static Session session;

    private static Map<String,PreparedStatement> statementMap = new HashMap();

    public static void init(){
        String clusterUrls = Config.getString("cassandra.cluster.url");
        if(StringUtils.isEmpty(clusterUrls)){
            log.info("CassandraPooledClient.init. cassandra.cluster.url is empty,skip init...");
            return;
        }
        String[] hosts = clusterUrls.split(",");
        String keyspace = Config.getString("cassandra.keyspaces");
        PoolingOptions poolingOptions = new PoolingOptions();
        //每个连接允许的并发请求数
//        poolingOptions.setMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL,25);
        poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL,25);
        //和集群中每个机器的最小连接数
        poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL,2);
        //和集群中每个机器的最大连接数
        poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL,4);

        Cluster cluster = Cluster.builder()
                .addContactPoints(hosts)
                .withPoolingOptions(poolingOptions)
                .build();
//        cluster.connect("");

        session = cluster.connect(keyspace);
    }

    private static String getInsertCql(String[] keys, String table) {
        String cql = null;
        String cql1 = null;
        String cql2 = null;

        for (int i = 0; i < keys.length; i++) {

            cql1 = (null == cql1) ? "INSERT INTO " + table + "(" + keys[i] : cql1 + "," + keys[i];
            cql2 = (null == cql2) ? " VALUES (?" : cql2 + ",?";
        }

        cql = cql1 + ")" + cql2 + ")";
        /*if("true".equalsIgnoreCase(GlobalTools.ifdebug)){
            System.out.println(cql);
        }*/

        return cql;
    }

    private static PreparedStatement getPreparedStatement(String table, String[] keys ){
        PreparedStatement prepStatement = statementMap.get(table);
        if(prepStatement == null){
            String cql = getInsertCql(keys, table);
            prepStatement = session.prepare(cql);
            statementMap.put(table,prepStatement);
            log.info("create preparedStatement. cql:" + cql);
        }
        return 	prepStatement;
    }


    public static int batchInsert(List<Map<String, Object>> records, String table) {

        if(CollectionUtils.isEmpty(records))
            return 0;
        String[] columns = records.get(0).keySet().toArray(new String[]{});
        BatchStatement batch = new BatchStatement();
        PreparedStatement prepStatement = getPreparedStatement(table,columns);

        for (Map<String, Object> record : records) {
            Object[] objs = new Object[columns.length];
            for (int i = 0; i < columns.length; i++) {
                objs[i] = record.get(columns[i]);

            }
            batch.add(prepStatement.bind(objs));
        }
        session.execute(batch);

        return records.size();

    }

    public static void main(String[] args){
        PoolingOptions poolingOptions = new PoolingOptions();
        //每个连接允许的并发请求数
//        poolingOptions.setMaxSimultaneousRequestsPerConnectionThreshold(HostDistance.LOCAL,25);
        poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL,25);
        //和集群中每个机器的最小连接数
        poolingOptions.setCoreConnectionsPerHost(HostDistance.LOCAL,2);
        //和集群中每个机器的最大连接数
        poolingOptions.setMaxConnectionsPerHost(HostDistance.LOCAL,4);

        Cluster cluster = Cluster.builder()
                .addContactPoints(new String[]{"127.0.0.1"})
                .withPoolingOptions(poolingOptions)
                .build();
//        cluster.connect("");

        session = cluster.connect();
        session.execute("CREATE KEYSPACE demospace REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };");
        session.close();
    }

}
