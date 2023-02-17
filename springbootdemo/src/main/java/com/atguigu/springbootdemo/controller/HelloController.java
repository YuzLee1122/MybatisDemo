package com.atguigu.springbootdemo.controller;

import com.atguigu.springbootdemo.bean.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Smexy on 2023/2/17
 *      @Controller作用：
 *              ①声明当前类是控制器
 *              ②容器扫描到这个注解标注的类时，自动为这个类在容器中创建一个单例对象
 *
 *       --------------
 *        前后端分离开发:
 *              前端程序员专注于 视图
 *                  视图的功能 由后端程序员提供。
 *                      视图 ------>请求------>后端程序接受------->负责提供此功能需要的数据---->前端程序员收到---->对数据进行渲染(js)
 */
//@Controller
@RestController  //@Controller 的功能 + 为所标注的类的所有方法默认添加@ResponseBody
public class HelloController
{
    /*
         @RequestMapping 把页面发送的请求和对应的处理方法进行映射。
     */
    @RequestMapping(value = "/hello")
    public String handle1(){

        System.out.println("处理了hello请求...");

        //返回一个页面
        return "/success.html";
    }

    /*
        如何返回数据给页面?  在方法上添加@ResponseBody
            分类:
                    字面量:  从字面上就看到变量值的变量。 常见的基本数据类型及包装类和String
                                int i = 0
                                String a = "haaha"
                                 直接将数据写入响应体，返回。

                    非字面量: 从字面上看不到变量值的变量
                                Employee e = new Employee();
                                将数据转化为JSON，再入响应体，返回。

     */
    //@ResponseBody // 把方法的返回值作为数据写入响应体
    @RequestMapping(value = "/hello1")
    public String handle2(){

        System.out.println("处理了hello1请求...");

        return "/success.html";
    }

    //@ResponseBody
    @RequestMapping(value = "/hello2")
    public Object handle3(){

        System.out.println("处理了hello2请求...");

        Employee employee = new Employee(1, "jack", "a", "b");

        return employee;
    }


    /*
        如何接收页面传递的参数
            两类:
                    普通参数：  在方法的形参位置声明和参数name名字一致的参数名即可。
                                类型，只能能兼容即可。

                    JSON格式参数: 只能使用Map或Bean接收。 需要在参数位置添加注解@RequestBody
                                    使用Bean，bean中必须有和参数名同名的属性才能接收
     */
    @RequestMapping(value = "/hello3")
    public Object handle4(String name, Integer age){

        System.out.println("处理了hello3请求...name:"+name+",age:"+age);

        return "ok";
    }

    /*
        JSON格式的参数，通过普通的页面无法发送，必须通过软件发送。

        @RequestBody: 把请求体中的参数赋值给某个形参。
     */
    @RequestMapping(value = "/hello4")
    public Object handle5(
       // @RequestBody Map<String,Object> param
       @RequestBody Employee param
    ){

        System.out.println("处理了hello4请求..."+param);

        return "ok";
    }


    /*
        接收url路径上的变量。
            url路径和参数是不同的。
                url?xxx=xxx
                    ?前面的称为url路径
                    ?后面的称为携带的参数

                在接收的路径参数位置声明占位符，格式 {xxx}
     */
    @RequestMapping(value = "/hello5/{a}/{b}")
    public Object handle6(@PathVariable("a") String name, @PathVariable("b") Integer age){

        System.out.println("处理了hello3请求...name:"+name+",age:"+age);

        return "ok";
    }



}
