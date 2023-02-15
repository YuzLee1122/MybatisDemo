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


----------------------------

select * from user_tag_merge order by uid;

/*
        输入:              1,M
                          2,M
                          3,M

        处理过程：  1.拼接当前列列名
                  2.  1,gender:M,favor:sm,ageGroup:90后
                            调用 UDTF，才能把1行 uid=1，变成3行
                        出:  gender,M, [1,xxx]
                             favor,sm, [1,xxx]
                            ageGroup,90后,[1,xxx]
                  3.最终的计算思路一定是聚合

        清楚输出的格式:    gender,M ,[1,2,3]
 */
select
    tagCode,tagValue,collect_list(uid) us
from user_tag_merge
lateral view explode(str_to_map(concat('gender:',gender,',agegroup:',agegroup,',favor:',favor))) tmp as tagCode,tagValue
group by tagCode,tagValue;

-- explode(a) - separates the elements of array a into multiple rows,
-- or the elements of a map into multiple rows and columns
-- 炸裂的是数组，将数组炸为 N行1列。
-- 炸裂的是Map<K,V>，将Map炸为 N行2列。
desc  function extended explode;

/*
    str_to_map(text, delimiter1, delimiter2) - Creates a map by parsing text
Split text into key-value pairs using two delimiters.
    The first delimiter separates pairs, and the second delimiter sperates key and value.
    If only one parameter is given, default delimiters are used: ',' as delimiter1 and ':' as delimiter2.

 */
desc  function extended str_to_map;
                                                                                                                                                                                                                                     id uid,`if`(isnull(gender),'U',gender) tagValue
                                                                                                                                                                                                                                 from gmall.dim_user_zip
                                                                                                                                                                                                                                 where dt='9999-12-31' )tmp

