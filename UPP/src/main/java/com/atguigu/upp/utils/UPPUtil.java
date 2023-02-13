package com.atguigu.upp.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

/**
 * Created by Smexy on 2023/2/13
 */
public class UPPUtil
{
    //传入properties文件的名字，不包括后缀 ResourceBundle看作是一个Map来用
    static  ResourceBundle config = ResourceBundle.getBundle("config");
    //编写方法读取config.properties的属性值
    public static String getProperty(String name){

            return config.getString(name);
    }

    public static void main(String[] args) {
        System.out.println(getProperty("aaa"));
    }

    //创建连接某个数据源的SqlSessionFactory
    public static SqlSessionFactory createSSF(String config) throws IOException {

        InputStream inputStream = Resources.getResourceAsStream(config);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        return sqlSessionFactory;

    }

    //创建SparkSql的入口 SparkSession
    public static SparkSession createSparkSession(String appName){

        SparkSession sparkSession = SparkSession.builder()
                                             .master(getProperty("masterUrl"))
                                             .appName(appName)
                                             .enableHiveSupport()
                                             .getOrCreate();

        return sparkSession;

    }
}
