package com.atguigu.upp.bean;

import lombok.Data;

import java.sql.Timestamp;

/*
        类型的对应关系:
                java string ------ mysql varchar
                java long   -----  mysql bigint
                java Timestamp ---- mysql date,timestamp

        封装的话，可以只封装自己需要的列。
                Mysql task_info表有20列，程序中只需要10列，只封装这10列为bean的属性
 */
@Data
public class TaskInfo {

    Long id;
    String taskName;
    String taskStatus;
    String taskComment;
    String taskType;
    String execType;
    String mainClass;
    Long fileId;
    String taskArgs;
    String taskSql;
    Long taskExecLevel;
    Timestamp createTime;
}