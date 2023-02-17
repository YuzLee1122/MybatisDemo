package com.atguigu.springbootdemo.mapper;

import com.atguigu.springbootdemo.bean.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Smexy on 2023/2/11
 *
 *      接口无法直接实例化。
 *          必须编写实现类，实例化实现类。
 *
 *          如果使用Mybatis，提供了动态代理技术，帮你在不编写实现类的情况下，返回一个接口的实例！
 *
 *          @Mapper的作用：
 *              1.给自己和同事看，表明这是一个Dao或Mapper
 *              2.容器扫描到后会为标注了这个注解的类，创建一个单例对象
 *              3.容器会用Mybatis提供的动态代理技术，创建对象
 */
@Mapper
public interface EmployeeMapper
{
    //定义读写数据库的方法。每个方法都可以明确参数类型，个数，返回值类型
    Employee getEmployeeById(Integer id);

    List<Employee> getAll();

    //写操作返回值可以不要
    void insertEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployeeById(Integer id);
}
