package com.atguigu.upp.app;

import com.atguigu.upp.bean.TagInfo;
import com.atguigu.upp.bean.TaskInfo;
import com.atguigu.upp.bean.TaskTagRule;
import com.atguigu.upp.service.MysqlDBService;
import com.atguigu.upp.utils.TagValueTypeConstant;
import com.atguigu.upp.utils.UPPUtil;
import com.sun.deploy.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.List;

/**
 * Created by Smexy on 2023/2/13
 *
 *  定义任务:  在平台上点击 添加任务 按钮，产生一个标签的计算任务。
 *              添加后的计算任务的元数据存储在Mysql中
 *
 *  计算任务:   ①如何读取任务
 *                  读取Mysql中的任务元数据即可。
 *                  使用Mybatis(JDBC)
 *
 *             ②使用SparkSQL计算任务
 *                  为什么选择Sparksql而不是hive?
 *                      a)后续需要使用一个技术pivot，hive没有，只有sparksql有
 *                      b) sparksql 效率比 hive高
 *
 *                  如何计算?
 *                       a)得知计算的任务是为了针对哪个标签计算
 *                              对tag_info表的 tag_task_id字段进行查询，获取当前任务计算的标签信息
 *
 *                  需要将标签计算的结果进行保存。
 *                          选择hive保存计算的结果。
 *                              建表
 *
 *
 *  --------------------------------------------
 *      用户画像平台 如何 调用 你自己写的SqlTaskExecuteApp?
 *             在计算某个任务时，用户画像平台，会自动将任务的id及业务日期作为main()的前两个参数传入
 *
 *
 *
 */
public class SqlTaskExecuteApp
{
    public static void main(String[] args) throws IOException {

        System.setProperty("HADOOP_HOME","E:\\Dev\\hadoop-3.1.0");

        //约定平台会自动传入以下参数
       /* String taskId = args[0];
        String doDate = args[1];*/

        String taskId = "1";
        String doDate = "2020-06-14";

        //1.读取任务的元数据
        SqlSessionFactory sqlSessionFactory = UPPUtil.createSSF("mysql_db.xml");
        MysqlDBService mysqlDBService = new MysqlDBService(sqlSessionFactory.openSession());

        TaskInfo taskInfo = mysqlDBService.getTaskInfoByTaskId(taskId);
        TagInfo tagInfo = mysqlDBService.getTagInfoByTaskId(taskId);
        List<TaskTagRule> rules = mysqlDBService.getTaskTagRulesByTaskId(taskId);

        String createTableSql = getCreateTableSql(tagInfo);

        //2.获取Sparksession
        SparkSession sparkSession = UPPUtil.createSparkSession("SqlTaskExecuteApp");
        //sparkSession.sql()


    }

    /*
insert overwrite table ?.? partition (dt='?')
select
    uid,
    ? case    tagValue
        when 'M'  then '男性'
        when 'F'  then '女性'
        when 'U'  then '未知'
    end tagValue
from
(
   ?
    ) tmp

     */
    private static String getInsertSql(){

        String template = " insert overwrite table  %s.%s partition (dt='%s') " +
                          " select uid, %s from ( %s )tmp ";

    }


    /*
        在hive中建表保存当前任务计算的结果
模版:
create table ?.?(
     uid string,
     tagValue ?
 )comment '?'
partitioned by (dt string)
location 'xxx/?'

    ?: jdbc中的占位符
    %s: java程序中字符串中的 填入 字符串类型的占位符
    %d:  java程序中字符串中的 填入 数值类型的占位符

     */
    private static String getCreateTableSql(TagInfo tagInfo){

        String template = " create table if not exists %s.%s ( uid string, tagValue %s )comment '%s' partitioned by (dt string)" +
                         "  location '%s/%s'  ";

        //获取库的名字
        String dbName = UPPUtil.getProperty("updbname");
        String tableName = tagInfo.getTagCode().toLowerCase();

        //判断标签计算的值的类型
        String tagValueType = "";
        switch (tagInfo.getTagValueType()){
            case TagValueTypeConstant.TAG_VALUE_TYPE_LONG : tagValueType = "bigint"; break;
            case TagValueTypeConstant.TAG_VALUE_TYPE_DECIMAL : tagValueType = "decimal(16,2)"; break;
            case TagValueTypeConstant.TAG_VALUE_TYPE_STRING : tagValueType = "string"; break;
            case TagValueTypeConstant.TAG_VALUE_TYPE_DATE: tagValueType = "string"; break;
        }

        //获取存储的路径前缀
        String hdfsPath = UPPUtil.getProperty("hdfsPath");

        //填充占位符
        String sql = String.format(template, dbName, tableName, tagValueType, tagInfo.getTagName(), hdfsPath, tableName);
        System.out.println(sql);
        return sql;

    }

}
