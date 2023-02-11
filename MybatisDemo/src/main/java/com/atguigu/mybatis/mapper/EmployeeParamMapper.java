package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Smexy on 2023/2/11
 *
 *
 *  介绍如何向sql中传入参数。
 *          一招鲜，吃遍天。 不管参数列表是几个参数，都使用@Param("name")，自定义参数名。
 *              自定义以后，使用#{name}获取参数的值
 *
 */
public interface EmployeeParamMapper
{
    @Select("select * from employee where id = #{e}")
    Employee getEmployeeById(@Param("a") Integer id, @Param("b") String name, @Param("c")Integer id2,
                             @Param("d")Integer id3, @Param("e")Integer id4, @Param("f")Integer id5);


    /*
        在某些特殊的位置，例如表名等位置，使用#{}占位，会自动按照方法掺入的参数类型，自动添加引号。
                举例:  传入employee ,自动添加引号变为 'employee'

                此时可以使用 ${},直接将参数拼接，不做任何多余的处理！

           在参数位置使用#{}，其他需要直接拼接的位置使用${}
     */
    //@Select("select * from #{table} ")
    @Select("select * from ${table} ")
    Employee getAll(@Param("table") String t);

}
