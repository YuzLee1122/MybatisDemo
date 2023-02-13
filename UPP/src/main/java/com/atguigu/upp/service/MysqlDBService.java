package com.atguigu.upp.service;

import com.atguigu.upp.bean.TagInfo;
import com.atguigu.upp.bean.TaskInfo;
import com.atguigu.upp.mapper.TagInfoMapper;
import com.atguigu.upp.mapper.TaskInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;

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

    public MysqlDBService(SqlSession sqlSession){

        taskInfoMapper = sqlSession.getMapper(TaskInfoMapper.class);
        tagInfoMapper = sqlSession.getMapper(TagInfoMapper.class);
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
