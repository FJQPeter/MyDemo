package com.instant.datastorage.store.redis;

import com.instant.datastorage.common.Config;
import com.instant.datastorage.entity.RedisDataEntity;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-8-16
 * Time: 下午5:20
 * To change this template use File | Settings | File Templates.
 */
public class RedisPoolClient {
    private static JedisPool jedisPool = null;

    public static Jedis getJedis() {
        Jedis jedis = null;
        boolean sw = true;
        while(sw) {
            try {
                if(jedisPool == null) {
                    initPool();
                }
                jedis = jedisPool.getResource();
                sw = false;
            }catch(Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                initPool();
            }
        }
        return jedis;

    }

    public static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(5);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(false);
        jedisPool = new JedisPool(config, Config.getString("redis.host"), Config.getInteger("redis.port"));
    }

    public static String getHash(String key,String field) throws Exception {
        Jedis jedis = null;
        try{
            jedis = getJedis();
            jedis.select(0);
            String value = jedis.hget(key,field);
            return value;
        }catch (Exception e){
            throw e;
        }finally {
            jedis.close();
        }
    }

    public static void putHash(List<RedisDataEntity> dataEntities) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.select(0);
            for (RedisDataEntity entity : dataEntities) {
                jedis.hset(entity.getKey(), entity.getField(), entity.getValue());
            }
        }catch (Exception e){
            throw e;
        }finally {
            jedis.close();
        }
    }

//    redis_host=192.168.1.10
//    redis_port=6379
//    redis_max_active=20
//    redis_max_idle=5
//    redis_max_wait=1000
//    redis_test_on_borrow=false
}
