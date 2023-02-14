package com.atguigu.upp.app;

/**
 * Created by Smexy on 2023/2/14
 *
 * 外部程序需要查询1号用户的全部画像信息。
 *      select
 *              collect_list(tagValue)
 *          from
 *      性别: (select tagValue from tag_xxxx_gender where dt='2020-06-14' and uid = '1'
 *              union
 *      年代: select tagValue from tag_xxxx_period where dt='2020-06-14' and uid = '1'
 *      .....  union
 *      最近7天消费金额: select tagValue from tag_xxxx_7d where dt='2020-06-14' and uid = '1'
 *              ) tmp
 *
 *    --------------
 *      以上做法麻烦！
 *
 *      简单： 将用户的所有的画像信息，统一合并到一张表中。
 *             有固定的一列uid,一个标签是一列
 *           uid, gender, period, 7d
 *            1 ,  男性,   00后  ， 2000
 *
 *            select * from xx where uid = '1'
 *
 */
public class MergeWideTableApp
{
}
