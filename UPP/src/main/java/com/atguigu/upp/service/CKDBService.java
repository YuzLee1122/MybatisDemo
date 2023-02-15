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

    public  void dropWideTable(String table){
        MysqlDBService.validParams(table);
        clickhouseMapper.dropWideTable(table);

    }

    public void insertBitmapTable( String sourceTable,String tag,
                                 String target,String dt){
        MysqlDBService.validParams(sourceTable,tag,target,dt);
        clickhouseMapper.insertBitmapTable(sourceTable,tag,target,dt);
    }

    public void dropBitmapByDate(String dt,String table){
        MysqlDBService.validParams(dt,table);
        clickhouseMapper.dropBitmapByDate(dt,table);
    }
}
