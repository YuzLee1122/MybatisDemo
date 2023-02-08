package com.atguigu.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Smexy on 2023/2/8
 *
 *  连接池：  在一个池子中，提前创建好连接。
 *          优势： 节省连接频繁创建和关闭的时间和性能开销。
 *
 *          获取连接： 从池中借连接
 *          关闭连接： 把连接还入池中
 */
public class JedisPoolDemo
{
    public static void main(String[] args) {

        //默认配置的池子
        JedisPool jedisPool = new JedisPool("hadoop104", 6379);

        //从池中借连接
        Jedis jedis = jedisPool.getResource();

        System.out.println(jedis.ping());

        //把连接还入池中
        jedis.close();

    }
}
