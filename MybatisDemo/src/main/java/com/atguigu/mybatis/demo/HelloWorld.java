package com.atguigu.mybatis.demo;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Smexy on 2023/2/11
 *
 *     对比JDBC。
 *          JDBC: 需要先创建一个Connection,代表客户端和数据库的连接。
 *          Mybatis: 需要创建一个SqlSession，代表客户端和数据库的一次会话。
 *                      会话： 在一个会话中，可以向服务端发送N条sql，接收客户端返回的N次结果。
 *
 *                      SqlSessionFactory.openSession() 返回SqlSession
 *
 *
 *      ---------------------------------
 *          如何获取一个对象？
 *              直接：  调用构造器。
 *                    A a = new A();
 *           遇到构造器私有或受保护的场景，无法调用！一般情况下类会提供相关的方法(使用设计模式)
 *
 *                  建造者模式:   A a = new A.Builder().build();
 *
 *                  工厂模式:  A a = new AFactory().getInstance();
 *
 *      ---------------------------------
 *          查询结果赋值给对象属性的原理:
 *
 *           过程:
 *              a.客户端发送 select * from employee where id = 1
 *              b.服务端执行sql，返回结果
 *     id  last_name  gender  email
 * ------  ---------  ------  -------------
 *      1  Tom        male    Tom@163.com
 *              c.根据sql语句的resultType，利用反射创建一个指定的Employee()
 *                     Employee e new Employee();
 *              d.调用Employee的 setter 将列的值赋值给属性
 *                  调用 Employee的 setXxx(yyy) 去赋值。
 *                          Xxx: 是查询的列名
 *                          yyy: 是查询的列值
 *
 *                        举例:
 *                          e.setId(1);
 *                          e.setLast_name(Tom);
 *                          e.setGender(male);
 *                          e.setEmial(Tom@163.com);
 *
 *              e.结果
 *                  Employee(id=1, lastName=null, gender=male, email=Tom@163.com)
 *
 *        ----------------------
 *          解决属性无法封装:
 *                  方法一： 提供对应的setter。 不建议
 *                  方法二； 目前提供setLastName(),通过为列起别名解决
 *                  方法三： 希望写*，希望框架自动将 数据库的下划线命名的列名，修改为 java风格的驼峰式命名。
 *                              last_name  ---->  lastName
 *                              通过添加配置解决
 *
 *
 *
 *
 */
public class HelloWorld
{
    public static void main(String[] args) throws IOException {

        //声明配置文件在哪里
        String resource = "mybatis_config.xml";
        //读取配置文件为一个输入流
        InputStream inputStream = Resources.getResourceAsStream(resource);
        //基于输入流，读取配置文件，创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        //1.获取链接
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //2.准备sql sql单独准备在一个文件中和java源代码分离
        // 查询1号员工
        /*
            调用sql
                selectOne(statement,param)
                        statement: 从已经注册的sql中根据id获取一条
         */
        Object o = sqlSession.selectOne("feichangbang.sql1", 1);

        Employee employee = (Employee) o;

        System.out.println(employee);

    }
}
