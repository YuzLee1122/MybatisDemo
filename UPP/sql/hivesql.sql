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
where dt > date_sub('dt',7)
group by user_id;
