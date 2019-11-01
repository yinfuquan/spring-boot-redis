package com.yin.spring.boot.redis.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date:2019/6/9
 * Time: 12:37
 * To change this template use File | Settings | File Templates.
 */

@Slf4j
@Component
public class JedisUtils2 {


    @Resource
    private JedisPool jedisPool;

    private JedisUtils2() {

    }

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String set = jedis.set(key, value);
            return set;
        } catch (Exception e) {

            log.error(e.getMessage());
            return "0";
        } finally {
            returnResource(jedis);
        }

    }

    public Long evalsha(String sha1, List<String> keys, List<String> args) {

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            Long evalsha = (Long) jedis.evalsha(sha1, keys, args);
            return evalsha;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0L;
        } finally {
            returnResource(jedis);
        }

    }

    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }


}
