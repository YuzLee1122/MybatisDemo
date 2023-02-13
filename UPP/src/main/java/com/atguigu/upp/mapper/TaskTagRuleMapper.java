package com.atguigu.upp.mapper;

import com.atguigu.upp.bean.TaskTagRule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Smexy on 2023/2/13
 */
public interface TaskTagRuleMapper
{
    //根据计算任务的id查询所计算标签的四级标签的映射规则
    @Select("SELECT" +
        " t2.*,t1.tag_name" +
        " FROM" +
        " (SELECT id,tag_name FROM tag_info ) t1" +
        " JOIN" +
        " (SELECT * FROM `task_tag_rule` WHERE task_id = '#{taskId}') t2" +
        " ON t1.id = t2.sub_tag_id")
    List<TaskTagRule> getTaskTagRuleByTaskId(@Param("taskId") String taskId);
}
