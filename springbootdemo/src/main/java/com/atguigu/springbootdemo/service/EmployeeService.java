package com.atguigu.springbootdemo.service;

import com.atguigu.springbootdemo.bean.Employee;

import java.util.List;

/**
 * Created by Smexy on 2023/2/17
 */
public interface EmployeeService
{
    Employee getEmployeeById(Integer id);

    List<Employee> getAll();

    //写操作返回值可以不要
    void insertEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployeeById(Integer id);
}
