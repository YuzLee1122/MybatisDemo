package com.atguigu.upp.mapper;

import com.atguigu.upp.bean.TagInfo;
import com.atguigu.upp.bean.TaskInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Smexy on 2023/2/13
 */
public interface TagInfoMapper
{
    @Select("select * from tag_info where tag_task_id = #{id}")
    TagInfo getTagInfoByTaskId(@Param("id") String taskId);

    @Select("SELECT * FROM tag_info " +
        "WHERE `tag_task_id` " +
        "IN (SELECT id FROM task_info WHERE task_status = '1'  ) ")
    List<TagInfo> getTaskInfoTodayNeedToExecute();
}
