package com.atguigu.mybatis.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Smexy on 2023/2/11
 *
 *  所有的属性都不要使用基本数据类型，要用包装类型。
 *
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee
{
    private Integer id;
    private String lastName;
    private String gender;
    private String email;

    /*public void setLast_name(String a){
        lastName = a;
    }*/
}
