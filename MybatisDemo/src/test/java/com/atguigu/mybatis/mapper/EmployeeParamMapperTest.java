package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Smexy on 2023/2/11

 */
public class EmployeeParamMapperTest
{

    private SqlSessionFactory sqlSessionFactory;

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


    /*
        BindingException:  参数无法正确传入sql或 sql和调用的方法无法匹配。
                Parameter 'xxdeio;afha;woieufhgaw ;oi' not found.
                   Available parameters are [arg1, arg0, param1, param2]

              接口的方法时多个参数，那么Mybatis会使用Map去封装参数。
                key: 以 arg0---argN
                        或 param1---paramN
                { arg0=4,arg1="tom", param1=4,param2="tom"  }

                此时 #{xxx}: xxx应该写Map中的key，这样的话 #{xxx}可以从参数Map中取出对应的value!

          select * from employee where id = #{xxdeio;afha;woieufhgaw ;oi}
     */
    @Test
    public void getEmployeeById() {

        SqlSession session = sqlSessionFactory.openSession();

        try {

            EmployeeParamMapper mapper = session.getMapper(EmployeeParamMapper.class);

            System.out.println(mapper.getEmployeeById(1,"tom",1,1,1,1));


        } finally {
            session.close();
        }
    }

    @Test
    public void getAll() {

        SqlSession session = sqlSessionFactory.openSession();

        try {

            EmployeeParamMapper mapper = session.getMapper(EmployeeParamMapper.class);

            System.out.println(mapper.getAll("employee"));


        } finally {
            session.close();
        }
    }


}