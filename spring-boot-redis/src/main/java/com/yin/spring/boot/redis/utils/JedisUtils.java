package com.yin.spring.boot.redis.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date:2019/6/9
 * Time: 12:37
 * To change this template use File | Settings | File Templates.
 */
@Component
public class JedisUtils implements ApplicationContextAware {


    public static Jedis JEDIS;

    //这样好像有点问题
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JedisPool jedisPool = (JedisPool) applicationContext.getBean("redisPoolFactory");
        JEDIS = ((JedisPool) applicationContext.getBean("redisPoolFactory")).getResource();
    }


}
