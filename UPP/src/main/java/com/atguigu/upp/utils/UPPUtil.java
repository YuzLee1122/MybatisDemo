package com.atguigu.upp.utils;

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
}
