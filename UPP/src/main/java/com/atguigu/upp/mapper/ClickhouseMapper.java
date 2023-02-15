package com.atguigu.upp.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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


    /*
  insert into user_tag_value_string
select
    tag.1 tagCode,if(tag.2 == '','0',tag.2) tagValue,groupBitmapState(toUInt64(uid)), '2020-06-14'
from
    (select
         uid,
         arrayJoin([
             ('tag_population_attribute_nature_gender', tag_population_attribute_nature_gender),
             ('tag_population_attribute_nature_period', tag_population_attribute_nature_period),
             ('tag_consumer_behavior_order_amount7d', tag_consumer_behavior_order_amount7d)
             ]) tag
     from  up_tag_merge_2020_06_14
     where length(uid) > 0
        ) t1
group by tag.1,tag.2;

     */
    @Insert(" insert into ${target} " +
        "     select  tag.1 tagCode,if(tag.2 == '','0',tag.2) tagValue,groupBitmapState(toUInt64(uid)), #{dt} " +
        "     from  ( select uid, arrayJoin( [  ${tag}  ] )  tag from  ${source}  where length(uid) > 0 ) t1 " +
        "     group by tag.1,tag.2  ")
    void insertBitmapTable(@Param("source") String sourceTable,@Param("tag")String tag,
                           @Param("target")String target,@Param("dt")String dt);

    //手动实现幂等性  提供删除当天分区数据的方法
    @Delete(" alter table ${target} delete where dt = #{dt} ")
    void dropBitmapByDate(@Param("dt")String dt,@Param("target")String target);
}
