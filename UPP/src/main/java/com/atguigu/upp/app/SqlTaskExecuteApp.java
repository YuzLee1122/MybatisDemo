package com.atguigu.upp.app;

import com.atguigu.upp.bean.TagInfo;
import com.atguigu.upp.bean.TaskInfo;
import com.atguigu.upp.service.MysqlDBService;
import com.atguigu.upp.utils.UPPUtil;
import com.sun.deploy.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;

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
        System.out.println(taskInfo);
        System.out.println(tagInfo);

        //2.获取Sparksession
        SparkSession sparkSession = UPPUtil.createSparkSession("SqlTaskExecuteApp");
        //sparkSession.sql()


    }
}
