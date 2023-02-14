package com.atguigu.upp.mapper;

import com.atguigu.upp.bean.TaskInfo;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Smexy on 2023/2/13
 *
 * Dao: 只负责读取数据库。
 *      在执行某个操作时，例如根据参数去读取数据库，步骤可能有很多，例如：
 *              ①校验参数是否合法
 *              ②做日志记录  xxxip,时间,访问数据库
 *              ③访问数据库   Dao负责
 *              ④对结果进行验证，做对应的处理
 */
public interface TaskInfoMapper
{
    //根据taskId查询任务的基本信息
    @Select("select * from task_info where id= #{id}")
    TaskInfo getTaskInfoByTaskId(@Param("id") String taskId);


}
