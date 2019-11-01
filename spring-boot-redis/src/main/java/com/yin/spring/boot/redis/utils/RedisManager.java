package com.yin.spring.boot.redis.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {

    private static JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPool = new JedisPool(jedisPoolConfig,"192.168.252.162");
    }

    public static Jedis getJedis() throws Exception{
        if(null!=jedisPool){
            return jedisPool.getResource();
        }
        throw new Exception("Jedispool was not init !!!");
    }


}