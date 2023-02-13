package com.atguigu.upp.bean;

import lombok.Data;

/*
    通常情况下，一张表对应一个类。
        一个类中可以封装多个表的数据！
 */
@Data
public class TaskTagRule {

    //从task_tag_rule中获取
    Long id;
    Long tagId;
    Long taskId;
    String queryValue;
    Long subTagId;
    //从tag_info获取 tag_name
    String subTagValue;
    
}