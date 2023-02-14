package com.atguigu.upp.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * Created by Smexy on 2023/2/14
 */
public interface ClickhouseMapper
{
    @Update("create table if not exists ${t} ( uid String, ${c} )engine = MergeTree order by uid ")
    void createWideTable(@Param("t") String tableName,@Param("c") String column);

    //手动实现幂等性。 合并宽边的任务当天重复执行，在每次执行之前，都先删表，再建表
    @Delete("drop table if  exists ${t}")
    void dropWideTable(@Param("t") String tableName);
}
