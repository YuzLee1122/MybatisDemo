show databases ;

create database test;

CREATE TABLE  test_order_info
(uname STRING, product  STRING, age INT, city string, amount decimal);

INSERT INTO test_order_info VALUES
(  'zhang3' ,  'tv' ,   22  , 'bj',   3000),
(  'li4',   'notebook',  41,  'bj',  8000),
(  'wang5',  'phone',  32,  'sh',  4000),
(  'zhao6',  'notebook', 22,  'sz',  3000),
(  'zhang3',  'phone',  22,  'bj',  3000),
(  'li4',      'tv',    41,   'sz',  4000) ;

-- pivot的运算过程： 使用旋转列值进行过滤，按照维度列分组，使用聚合列聚合。
-- 分析，哪些是旋转列，哪些是聚合列，及剩下的就是维度列。
select
    *
from (select age,product,amount,city from test_order_info) t
pivot(
   sum(amount) , avg(age)
   for city in ('bj','sz','sh')
);



-- 案例一
select
    *
from (select uname,age,product,amount from test_order_info) t
pivot(
   sum(amount)
   for product in ('tv','notebook','phone')
);


select
    *
from xx
pivot(
   聚合列,
   for 旋转列 in (旋转列值)
);

select
    uname,age,
       sum(`if`(product = 'pv',amount,0)) tv_amount,
       sum(`if`(product = 'notebook',amount,0)) notebook_amount,
       sum(`if`(product = 'phone',amount,0)) phone_amount
from test_order_info
group by uname,age;

----------------
create table test_user_tags (uid string, tag_code STRING, tag_value STRING);
INSERT INTO test_user_tags VALUES
(  '101','gender' ,'f'  ),
(  '102', 'gender', 'm' ),
(  '103', 'gender', 'm' ),
(  '104', 'gender', 'f' ),
(  '105', 'gender', 'm' ),
(  '106', 'gender', 'f' ),
(  '101','age' ,'60'  ),
(  '102', 'age', '70' ),
(  '103', 'age', '80' ),
(  '104', 'age', '70' ),
(  '105', 'age', '90' ),
(  '106', 'age', '90' ) ,
(  '101','amount' ,'422'  ),
(  '102', 'amount', '4443' ),
(  '103', 'amount', '12000' ),
(  '104', 'amount', '6664' ),
(  '105', 'amount', '900' ),
(  '106', 'amount', '2000' ) ;

/*
    uid,gender
    101  ,f
    max,min
 */
select
    *
from  test_user_tags
pivot(
    max(tag_value)
   for tag_code in ('gender','age','amount')
);


select
    *
from (
        select uid,`tagValue`,'tag_consumer_behavior_order_amount7d' tagCode from upp220926.tag_consumer_behavior_order_amount7d where dt='2020-06-14'
        union all
        select uid,tagvalue ,'tag_population_attribute_nature_gender' tagCode from upp220926.tag_population_attribute_nature_gender where dt='2020-06-14'
        union all
        select uid,`tagValue`,'tag_population_attribute_nature_period' tagCode from upp220926.tag_population_attribute_nature_period where dt='2020-06-14'
         ) t
pivot(
    max(tagValue)
   for tagCode in ('tag_consumer_behavior_order_amount7d',
                  'tag_population_attribute_nature_gender',
                   'tag_population_attribute_nature_period')
);

select
       *
from (  select uid,`tagValue`,'tag_population_attribute_nature_gender' tagCode from upp220926.tag_population_attribute_nature_gender where dt='2020-06-14'
        union all
       select uid,`tagValue`,'tag_population_attribute_nature_period' tagCode from upp220926.tag_population_attribute_nature_period where dt='2020-06-14'
       union all
      select uid,`tagValue`,'tag_consumer_behavior_order_amount7d' tagCode from upp220926.tag_consumer_behavior_order_amount7d where dt='2020-06-14'  )t
pivot(
 max(tagValue)
 for tagCode in ('TAG_POPULATION_ATTRIBUTE_NATURE_GENDER','TAG_POPULATION_ATTRIBUTE_NATURE_PERIOD','TAG_CONSUMER_BEHAVIOR_ORDER_AMOUNT7D') )


select * from (  select uid,`tagValue`,'tag_population_attribute_nature_gender' tagCode from upp220926.tag_population_attribute_nature_gender where dt='2020-06-14'  union all  select uid,`tagValue`,'tag_population_attribute_nature_period' tagCode from upp220926.tag_population_attribute_nature_period where dt='2020-06-14'  union all  select uid,`tagValue`,'tag_consumer_behavior_order_amount7d' tagCode from upp220926.tag_consumer_behavior_order_amount7d where dt='2020-06-14'  )t pivot(  max(tagValue)  for tagCode in ('tag_population_attribute_nature_gender','tag_population_attribute_nature_period','tag_consumer_behavior_order_amount7d') )

