package com.atguigu.upp.app;

import com.atguigu.upp.bean.TagInfo;
import com.atguigu.upp.bean.TaskInfo;
import com.atguigu.upp.service.CKDBService;
import com.atguigu.upp.service.MysqlDBService;
import com.atguigu.upp.utils.UPPUtil;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
 *    ----------------------------
 *      任务流程:
 *              ①查询今天要计算哪些任务，以及这些任务对应哪些标签
 *                      查询Mysql的 task_info表中 task_status = 1 的任务
 *              ②使用pivot拼接查询语句，查询合并的宽表
 *              ③在ck中创建宽表
 *                  order by针对某列进行排序，排序后，可以创建索引。无排序，不索引。
 *                  索引是加速查询。而我们查询时，只使用uid进行过滤，因此需要为uid创建主索引，必须要排序。
 *
 *                      选择引擎: mergeTree
 *                      order by : uid
 *                      是否要分区? 无法分区。每天表的列是动态变化的。只是每天一张宽表，无需分区。
 *
 *                      分区的前提： 每天写入的数据的列都是一样的，只是分区字段不同。
 *                                  t1
 *                                  a ,b, c  dt
 *                                  1  2  3  6-14
 *                                  2  3  4  6-15
 *
 *                                  每天表的列是动态变化的。
 *                                  6-14：要计算3个标签  a,b,c
 *                                  6-15：要计算5个标签  b,c,d,e,f
 *
 *
 *
 *              ④将查询结果写入ck
 *
 *
 */
public class MergeWideTableApp
{
    public static void main(String[] args) throws IOException {

        //约定平台会自动传入以下参数
        /*String taskId = args[0];
        String doDate = args[1];*/

        String taskId = "1";
        String doDate = "2020-06-14";

        //①查询今天要计算的任务的标签信息
        SqlSessionFactory sqlSessionFactory = UPPUtil.createSSF("mysql_db.xml");
        SqlSessionFactory ckSqlSessionFactory = UPPUtil.createSSF("ck_db.xml");
        MysqlDBService mysqlDBService = new MysqlDBService(sqlSessionFactory.openSession());
        CKDBService ckDBService = new CKDBService(ckSqlSessionFactory.openSession());

        List<TagInfo> tags = mysqlDBService.getTaskInfoTodayNeedToExecute();

        //②使用pivot拼接查询语句，查询合并的宽表
        pivotSql(tags,doDate);


    }

    /*
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
     */
    private static String pivotSql(List<TagInfo> tags,String doDate){

        String template = " select * from ( %s )t pivot(  max(tagValue)  for tagCode in (%s) )  ";

        String dbName = UPPUtil.getProperty("updbname");
        String singleRowTemplate = " select uid,`tagValue`,'%s' tagCode from %s.%s where dt='%s' ";
        //生成当前要合并的数据源
        String sourceSql = tags.stream()
                             .map(t -> String.format(singleRowTemplate, t.getTagCode().toLowerCase(), dbName, t.getTagCode().toLowerCase(), doDate))
                             .collect(Collectors.joining(" union all "));

        String pivotValueSql = tags.stream()
                             .map(t -> "'" + t.getTagCode().toLowerCase() + "'")
                             .collect(Collectors.joining(","));

        //最终格式化
        String sql = String.format(template, sourceSql, pivotValueSql);
        System.out.println(sql);
        return sql;


    }
}
