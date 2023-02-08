package com.atguigu.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by Smexy on 2023/2/8
 *
 *
 */
public class JedisSetDemo
{
    public static void main(String[] args) {
        Jedis jedis = new Jedis("hadoop104", 6379);

        jedis.sadd("myset","a","b","c");
        System.out.println(jedis.smembers("myset"));
        //如果set类型的key不存在，返回[](空集合，不是null)
        System.out.println(jedis.smembers("myset1"));

        jedis.close();


    }
}
