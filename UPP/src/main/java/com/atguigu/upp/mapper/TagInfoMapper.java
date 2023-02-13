package com.atguigu.upp.mapper;

import com.atguigu.upp.bean.TagInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Smexy on 2023/2/13
 */
public interface TagInfoMapper
{
    @Select("select * from tag_info where tag_task_id = #{id}")
    TagInfo getTagInfoByTaskId(@Param("id") String taskId);
}
