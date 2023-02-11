package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by Smexy on 2023/2/11
 *
 *      SqlSession 不是线程安全的，因此不能共享。每个方法应该有自己的SqlSession。
 *                  不能作为静态属性，或普通属性
 */
public class EmployeeMapperTest
{

    private SqlSessionFactory sqlSessionFactory;
    //错误示范
    //private SqlSession session = sqlSessionFactory.openSession();

    //对象创建后执行
    {
        String resource = "mybatis_config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void getAll() {

        SqlSession session = sqlSessionFactory.openSession();

        try {

            // class com.sun.proxy.$Proxy5 implements EmployeeMapper
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            // com.sun.proxy.$Proxy5
            System.out.println(mapper.getClass().getName());

            Class<?>[] interfaces = mapper.getClass().getInterfaces();
            for (Class<?> anInterface : interfaces) {
                //interface com.atguigu.mybatis.mapper.EmployeeMapper
                System.out.println(anInterface);
            }

            System.out.println(mapper.getAll());


        } finally {
            session.close();
        }
    }

    @Test
    public void getEmployeeById() {

        SqlSession session = sqlSessionFactory.openSession();

        try {

            // class com.sun.proxy.$Proxy5 implements EmployeeMapper
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

            System.out.println(mapper.getEmployeeById(3));


        } finally {
            session.close();
        }
    }

    /*
            查询和事务无关。
            写(增删改)操作，都必须提交事务！
     */
    @Test
    public void delEmployeeById() {

        //自动提交
        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            // class com.sun.proxy.$Proxy5 implements EmployeeMapper
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

            mapper.deleteEmployeeById(2);

            //手动提交  commit
            //session.commit();

        } finally {
            session.close();
        }
    }

    @Test
    public void updateEmp() {

        //自动提交
        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            // class com.sun.proxy.$Proxy5 implements EmployeeMapper
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

            Employee employee = mapper.getEmployeeById(1);

            employee.setLastName("ponyma");

            mapper.updateEmployee(employee);

        } finally {
            session.close();
        }
    }

    @Test
    public void insertEmp() {

        //自动提交
        SqlSession session = sqlSessionFactory.openSession(true);

        try {

            // class com.sun.proxy.$Proxy5 implements EmployeeMapper
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);

            Employee employee = new Employee(null, "jackma", "male", "abc");

            mapper.insertEmployee(employee);

        } finally {
            session.close();
        }
    }
}