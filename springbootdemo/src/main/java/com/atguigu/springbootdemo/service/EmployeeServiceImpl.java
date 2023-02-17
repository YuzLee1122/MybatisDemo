package com.atguigu.springbootdemo.service;

import com.atguigu.springbootdemo.bean.Employee;
import com.atguigu.springbootdemo.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Smexy on 2023/2/17
 *      @Service作用：
 *              1.给你自己或同事看，说明当前这个类是一个业务模型
 *              2.容器扫描到后，会为标注了这个注解的类，在容器中创建一个单例对象
 */
@Service
public class EmployeeServiceImpl implements EmployeeService
{
    @Autowired
    private EmployeeMapper employeeMapper;

    /*
        业务过程的编写，参考业务规范
     */
    @Override
    public Employee getEmployeeById(Integer id) {

        System.out.println("查询之前....");

        Employee employee = employeeMapper.getEmployeeById(id);

        System.out.println("查询之后...");

        return employee;
    }

    @Override
    public List<Employee> getAll() {
        System.out.println("查询之前....");

        List<Employee> all = employeeMapper.getAll();

        System.out.println("查询之后...");
        return all;
    }

    @Override
    public void insertEmployee(Employee employee) {
        System.out.println("写入之前....");
        employeeMapper.insertEmployee(employee);
        System.out.println("写入之后....");
    }

    @Override
    public void updateEmployee(Employee employee) {
        System.out.println("更新之前....");
        employeeMapper.updateEmployee(employee);
        System.out.println("更新之后....");

    }

    @Override
    public void deleteEmployeeById(Integer id) {
        System.out.println("删除之前....");
        employeeMapper.deleteEmployeeById(id);
        System.out.println("删除之后....");

    }
}
