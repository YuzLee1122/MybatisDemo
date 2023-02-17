package com.atguigu.springbootdemo.controller;

import com.atguigu.springbootdemo.bean.Employee;
import com.atguigu.springbootdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Smexy on 2023/2/17
 */
@RestController
public class EmployeeController
{

    //  employeeService = new EmployeeServiceImpl();
    // 会自动从容器中找当前注解标注的类型，找到就赋值。
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/getAllEmp")
    public Object handle1(){
        List<Employee> all = employeeService.getAll();
        return all;
    }

    @RequestMapping(value = "/emp")
    public Object handle2(String op,Integer id,String lastname,String gender,String email){

        //封装前台传入的参数为数据模型
        Employee e = new Employee(id, lastname, gender, email);

        switch (op){
            case "select" :  if (id == null){
                return "id非法";
            }else {
                Employee employee = employeeService.getEmployeeById(id);
                return employee == null ? "查无此人" : employee;
            }

            case "delete": if (id == null){
                return "id非法";
            }else {
                employeeService.deleteEmployeeById(id);
                return "ok";
            }

            case "insert": {
                employeeService.insertEmployee(e);
                return "ok";
            }

            case "update": if (id == null){
                return "id非法";
            }else {
                employeeService.updateEmployee(e);
                return "ok";
            }

            default: return "ok";
        }

    }


}
