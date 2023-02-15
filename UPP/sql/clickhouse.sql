/*
    宽表的粒度： 一个用户是一行。

    往往是查询一个群体。
        查询性别标签为男性的用户有哪些?
        查询年代标签为00后的用户有哪些?
        查询年代标签为00后及性别标签为男性的用户有哪些?

    对用户进行初步分群。
        按照标签值，将用户划分为一个个群体。
            性别 男性 [1,2,3,4]
            性别 女性 [5,6,7,8]

        需要创建表存储初步分群的结果。
            有3列。
                tagName String,
                tagValue String|decimal|Uint64|DateTime
                userGroup array[Uint32]

    ---------------------
        bit:  位。 计算机中计算的最基本的单位。在计算中是二进制的，要么是0，要么是1
                    bitmap: 由若干连续的bit组成的存储空间。
                            bit组成的集合。
                            redis和ck提供了bitmap结构。

        byte: 字节。 计算机中存储的最基本的单位。  1kb = 1024字节
                1byte = 8bit
 */
select
    t1.uid
from
    (select uid from up_tag_merge_2020_06_14 where tag_population_attribute_nature_gender='男性') t1
        join

    (select uid from up_tag_merge_2020_06_14 where tag_population_attribute_nature_period='00后') t2
    on t1.uid = t2.uid;


select uid from up_tag_merge_2020_06_14
where tag_population_attribute_nature_gender='男性'
  and
        tag_population_attribute_nature_period='00后';

------------------------------------------
create table user_tag_merge
(   uid UInt64,
    gender String,
    agegroup String,
    favor String
)engine=MergeTree()
     order by (uid);

insert into user_tag_merge values(1,'M','90后','sm');
insert into user_tag_merge values(2,'M','70后','sj');
insert into user_tag_merge values(3,'M','90后','ms');
insert into user_tag_merge values(4,'F','80后','sj');
insert into user_tag_merge values(5,'F','90后','ms');

create table user_tag_value_string
(
    tag_code String,
    tag_value String ,
    us AggregateFunction(groupBitmap,UInt64)
)engine=AggregatingMergeTree()
     partition by  (tag_code)
     order by (tag_value);

-- 使用普通的数组 array[UInt64]如何对用户进行分群?
-- hive
-- 输出:  gender,M ,[1,2,3]




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

        ---Map 其实就是 array[Entry]

        groupBitmapState: 把N行一列整数，组合为一行一列的bitmap
 */
insert into user_tag_value_string
select
    tag.1 tagCode,tag.2 tagValue,groupBitmapState(uid)
from
    (select
         uid,
         arrayJoin([
             ('gender', gender),
                 ('agegroup', agegroup),
                 ('favor', favor)
                     ]) tag
     from  user_tag_merge
    ) t1
group by tag.1,tag.2;

select tag_code,tag_value,bitmapToArray(us) from user_tag_value_string;

select [1,2,3];

select (1,2);

-- 集合间操作
--  求bitmap中元素的个数
select tag_code,tag_value,bitmapToArray(us),bitmapCardinality(us) from user_tag_value_string;

-- 求bitmap的交集
-- 4，5
select bitmapToArray(
               bitmapAnd(
                       bitmapAnd(
                               (select us from user_tag_value_string where tag_code = 'gender' and tag_value = 'F'),
-- 1，3，5
                               (select us from user_tag_value_string where tag_code = 'agegroup' and tag_value = '90后')
                           ),
                       (select us from user_tag_value_string where tag_code = 'favor' and tag_value = 'ms')
                   )
           );

-- 求bitmap的并集
select
    bitmapToArray(bitmapOr(
            (select us from user_tag_value_string where tag_code = 'gender' and tag_value = 'F'),
        -- 1，3，5
            (select us from user_tag_value_string where tag_code = 'agegroup' and tag_value = '90后')
        )
        );

-- 求bitmap的差集
select
    bitmapToArray(bitmapAndnot(
            (select us from user_tag_value_string where tag_code = 'gender' and tag_value = 'F'),
        -- 1，3，5
            (select us from user_tag_value_string where tag_code = 'agegroup' and tag_value = '90后')
        )
        );

--求bitmap的异或运算    (a 差集 b) 并集 (b 差集 a)
select
    bitmapToArray(bitmapXor(
            (select us from user_tag_value_string where tag_code = 'gender' and tag_value = 'F'),
        -- 1，3，5
            (select us from user_tag_value_string where tag_code = 'agegroup' and tag_value = '90后')
        )
        );

-- 求多行的并集    前面的所有函数都是 scalar function(标量函数):  输入 1行 输出1行
-- groupBitmapMergeState: 把N行1列的 bitmap 合并为1行1列
select
    bitmapToArray(bitmapOr(
            (select groupBitmapMergeState(us) from user_tag_value_string where tag_code = 'gender' ),
        -- 1，3，5
            (select us from user_tag_value_string where tag_code = 'agegroup' and tag_value = '90后')
        )
        );








