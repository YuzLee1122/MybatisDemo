package com.atguigu.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by Smexy on 2023/2/8
 *
 *
 */
public class JedisStringDemo
{
    public static void main(String[] args) {
        Jedis jedis = new Jedis("hadoop104", 6379);

        jedis.set("hello","hi");
        System.out.println(jedis.get("hello"));
        //如果key不存在，返回null
        System.out.println(jedis.get("hello1"));

        jedis.close();


    }
}
