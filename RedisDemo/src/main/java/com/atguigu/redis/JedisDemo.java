package com.atguigu.redis;

import redis.clients.jedis.Jedis;

/**
 * Created by Smexy on 2023/2/8
 *
 *     连接数据库:
 *        JDBC:
 *              ①获取Connection
 *              ②准备String，使用PreparedStatement预编译sql
 *              ③填充占位符
 *              ④执行命令
 *                      读
 *                      写
 *              ⑤读，解析返回值
 *              ⑥关系连接，释放资源
 *
 *        不支持JDBC:
 *              ①准备客户端,获取Connection
 *              ②使用客户端发送命令
 *              ③如果是读，解析结果
 *              ④关闭连接
 */
public class JedisDemo
{
    public static void main(String[] args) {
        //①准备客户端,获取Connection
        Jedis jedis = new Jedis("hadoop104", 6379);

        //②使用客户端发送命令
        String res = jedis.ping();
        //③如果是读，解析结果
        System.out.println(res);

        // ④关闭连接
        jedis.close();


    }
}
