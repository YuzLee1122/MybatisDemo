-- 计算性别
-- 哪一层? dim 哪张表？ dim_user_zip 哪个分区? 9999-12-31
-- 写sql要加上库名
-- 针对查询结果复杂的场景，可以使用简单的标记来标识结果
-- 为了计算方便，所有的用户画像标签计算的结果统一使用 uid作为用户id，tagValue作为计算的结果
-- 数仓中为用户画像提供数据的有:  用户维度表， 和用户维护相关的各种业务的聚合表 dws_业务_user_xx
select
    id uid,`if`(isnull(gender),'U',gender) tagValue
from gmall.dim_user_zip
where dt='9999-12-31';


--计算年代
select
    id uid,substr(birthday,3,1) tagValue
from gmall.dim_user_zip
where dt='9999-12-31';

--计算最近7天下单消费金额
-- 哪一层? dws 哪张表？ dws_trade_order_user_1d 哪个分区?
select
    user_id uid,sum(order_total_amount_1d) tagValue
from gmall.dws_trade_user_order_1d
where dt > date_sub('$dt',7)
group by user_id;

create database upp220926;

/*
    1.是否是外部表
        无关紧要。 当前hive中保存的标签信息，只是一个临时结果。


    2.是否是分区表？如果是以什么分区?

        是，以业务日期分区。 画像从数仓中读取数据计算，数仓中的数据以天为单位更新，计算的画像信息也要每天一更新。

    3.列的设置
            uid string
            tagValue  (文本|日期|浮点|整数)  最终要看计算的标签才能确定

    4.表中文件的类型，是否要压缩?
            很灵活。需要就加上。

    5.命名
            以计算的标签名(tag_code)作为表名


 */

 create table upp220926.TAG_POPULATION_ATTRIBUTE_NATURE_GENDER(
     uid string,
     tagValue string
 )comment '性别'
partitioned by (dt string)
location 'xxx/TAG_POPULATION_ATTRIBUTE_NATURE_GENDER';

create table if not exists upp220926.tag_population_attribute_nature_gender ( uid string, tagValue string )
comment '性别' partitioned by (dt string)
    location 'hdfs://hadoop102:9820/user_profile/tag_population_attribute_nature_gender'


/*
    如果当前任务计算的是没有四级标签的三级标签，可以直接把sql取出，拼接在insert后面。

    如果当前任务计算的是有四级标签的三级标签，例如性别，那么还需要根据sql计算的值，去映射四级标签值。

 */
insert overwrite table upp220926.tag_population_attribute_nature_gender partition (dt='2020-06-14')
select
    uid,
    case    tagValue
        when 'M'  then '男性'
        when 'F'  then '女性'
        when 'U'  then '未知'
    end tagValue
from
(
    select
        id uid,`if`(isnull(gender),'U',gender) tagValue
    from gmall.dim_user_zip
    where dt='9999-12-31'
    ) tmp;


insert overwrite table  upp220926.tag_population_attribute_nature_gender partition (dt='2020-06-14')
 select uid,
        case    tagValue
            when 'M'  then 'null'
            when 'F'  then 'null'
            when 'U'  then 'null'
        end tagValue
from ( select
                                                                                                                                                                                                                                     id uid,`if`(isnull(gender),'U',gender) tagValue
                                                                                                                                                                                                                                 from gmall.dim_user_zip
                                                                                                                                                                                                                                 where dt='9999-12-31' )tmp

