package com.yin.spring.boot.redis.ctl;

import com.yin.spring.boot.redis.utils.JedisUtils;
import com.yin.spring.boot.redis.utils.JedisUtils2;
import com.yin.spring.boot.redis.utils.RedisManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ：yfq
 * @date ：Created in 2019/11/1 14:05
 * @description：
 * @modified By：
 */
@RestController
public class TestCtl {

    @Autowired
    private  JedisUtils2 jedisUtils;

    @RequestMapping("/redis")
    public  String redis() throws Exception {
        Jedis jedis = RedisManager.getJedis();
        return  jedis.set("name","yin");
    }

    @RequestMapping("/test")
    public  String test() throws InterruptedException, BrokenBarrierException {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(49);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for(int i=0;i<50;i++){
            new Thread((()->{
                int i1 = atomicInteger.addAndGet(1);
                System.out.println(i1);
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                JedisUtils.JEDIS.setex("test:key"+i1,20, LocalDateTime.now().toString());
            })).start();


          //  Thread.sleep(1000L);
        }

        System.out.println("结束了");
        return JedisUtils.JEDIS.psetex("test:key",4000L, LocalDateTime.now().toString());
    }
    @RequestMapping("/lua")
    public  String lua(){
        final CountDownLatch latch = new CountDownLatch(1);

        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                        System.out.println("请求是否被执行："+accquire());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }

        latch.countDown();

        return "ppppp";
    }




    public  boolean accquire() throws IOException, URISyntaxException {

//        JedisPool jedisPool = new JedisPool("192.168.252.162", 6379);
//        Jedis jedis = jedisPool.getResource();

        String lua =
                "local key = KEYS[1] " +
                        " local limit = tonumber(ARGV[1]) " +
                        " local current = tonumber(redis.call('get', key) or '0')" +
                        " if current + 1 > limit " +
                        " then  return 0 " +
                        " else "+
                        " redis.call('INCRBY', key,'1')" +
                        " redis.call('expire', key,'50') " +
                        " end return 1 ";

        String key = "ip:" + System.currentTimeMillis()/1000; // 当前秒
        String limit = "3"; // 最大限制
        List<String> keys = new ArrayList<String>();
        keys.add(key);
        List<String> args = new ArrayList<String>();
        args.add(limit);
        String luaScript = jedisUtils.getJedis().scriptLoad(lua);
        Long result = jedisUtils.evalsha(luaScript, keys, args);
        return result == 1;
    }
}
