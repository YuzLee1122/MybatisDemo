package com.atguigu.upp.service;

import com.atguigu.upp.mapper.ClickhouseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.SqlSession;

/**
 * Created by Smexy on 2023/2/14
 */
public class CKDBService
{
    private ClickhouseMapper clickhouseMapper;

    public CKDBService(SqlSession sqlSession){
        clickhouseMapper = sqlSession.getMapper(ClickhouseMapper.class);
    }

    public void createWideTable( String tableName,  String column){

        MysqlDBService.validParams(tableName,column);

        clickhouseMapper.createWideTable(tableName,column);

    }
}
