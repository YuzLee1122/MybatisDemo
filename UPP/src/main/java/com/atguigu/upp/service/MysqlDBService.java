package com.atguigu.upp.service;

import com.atguigu.upp.bean.TagInfo;
import com.atguigu.upp.bean.TaskInfo;
import com.atguigu.upp.bean.TaskTagRule;
import com.atguigu.upp.mapper.TagInfoMapper;
import com.atguigu.upp.mapper.TaskInfoMapper;
import com.atguigu.upp.mapper.TaskTagRuleMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * Created by Smexy on 2023/2/13
 *
 *      完成某项操作的全部逻辑。
 *      在Service去调用Dao
 */
public class MysqlDBService
{
    private TaskInfoMapper taskInfoMapper;
    private TagInfoMapper tagInfoMapper;
    private TaskTagRuleMapper taskTagRuleMapper;

    public MysqlDBService(SqlSession sqlSession){

        taskInfoMapper = sqlSession.getMapper(TaskInfoMapper.class);
        tagInfoMapper = sqlSession.getMapper(TagInfoMapper.class);
        taskTagRuleMapper = sqlSession.getMapper(TaskTagRuleMapper.class);
    }

    public List<TaskTagRule> getTaskTagRulesByTaskId(String taskId){
        //校验参数
        validParams(taskId);
        return taskTagRuleMapper.getTaskTagRuleByTaskId(taskId);
    }

    public TagInfo getTagInfoByTaskId(String taskId){
        //校验参数
        validParams(taskId);
        return tagInfoMapper.getTagInfoByTaskId(taskId);
    }

    public TaskInfo getTaskInfoByTaskId(String taskId){

        //校验参数
        validParams(taskId);

        return taskInfoMapper.getTaskInfoByTaskId(taskId);

    }

    public void validParams(String... args){

        for (String arg : args) {
            //isBlank(str): 判断str是否是 白字符(空格，回车，tag) 或 null
            if (StringUtils.isBlank(arg)){
                throw new RuntimeException("参数非法!");
            }
        }

    }
}
