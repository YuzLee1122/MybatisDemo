package com.atguigu.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Smexy on 2023/2/8
 *
 *  连接池：  在一个池子中，提前创建好连接。
 *          优势： 节省连接频繁创建和关闭的时间和性能开销。
 *
 *          获取连接： 从池中借连接
 *          关闭连接： 把连接还入池中
 */
public class JedisPoolConfigDemo
{
    public static void main(String[] args) {

        //自定义池子的规格
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);  //最大容量
        jedisPoolConfig.setMaxIdle(10);  //如果当前没有客户端来借连接，池子处于空闲状态。最多维持 5-10个可用的连接。
        jedisPoolConfig.setMinIdle(5);
        jedisPoolConfig.setTestOnBorrow(true);  //借连接之前，先测试下，好使再借出去
        jedisPoolConfig.setTestOnReturn(true);  //还连接之前，先测试下，好使再放入池中
        jedisPoolConfig.setBlockWhenExhausted(true); //当客户端来接连接，池中的连接耗尽了，此时客户端是否阻塞
        jedisPoolConfig.setMaxWaitMillis(60000);   //阻塞的最大等待时间

        JedisPool jedisPool = new JedisPool(jedisPoolConfig,"hadoop104", 6379);

        //从池中借连接
        Jedis jedis = jedisPool.getResource();

        System.out.println(jedis.ping());

        //把连接还入池中
        jedis.close();

    }
}
