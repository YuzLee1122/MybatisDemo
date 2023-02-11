package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.Employee;

import java.util.List;

/**
 * Created by Smexy on 2023/2/11
 *
 *      接口无法直接实例化。
 *          必须编写实现类，实例化实现类。
 *
 *          如果使用Mybatis，提供了动态代理技术，帮你在不编写实现类的情况下，返回一个接口的实例！
 */
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
