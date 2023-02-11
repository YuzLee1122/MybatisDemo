package com.atguigu.mybatis.mapper;

import com.atguigu.mybatis.bean.Employee;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by Smexy on 2023/2/11
 *
 *  为了提高开发效率，一些公司会把一些不需要去升级维护的SQL直接卸载接口中。
 *
 *  切记：写完后，也需要在全局的配置中注册(让mybaits知道这里面有sql)。
 */
public interface EmployeeAnotationMapper
{
    @Select("select * from employee where id = #{xxdeio;afha;woieufhgaw ;oi}")
    Employee getEmployeeById(Integer id);

    @Select("select * from employee")
    List<Employee> getAll();

    @Insert("insert into employee(last_name,gender,email) values(#{lastName},#{gender},#{email})")
    void insertEmployee(Employee employee);

    @Update("update employee set last_name=#{lastName} , gender = #{gender } , email=#{email} " +
        "        where id = #{id}")
    void updateEmployee(Employee employee);

    @Delete("delete  from employee where id = #{xx}")
    void deleteEmployeeById(Integer id);
}
