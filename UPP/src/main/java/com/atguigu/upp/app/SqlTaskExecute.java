package com.atguigu.upp.app;

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
 *  --------------------------------------------
 *      用户画像平台 如何 调用 你自己写的SqlTaskExecuteApp?
 *             在计算某个任务时，用户画像平台，会自动将任务的id及业务日期作为main()的前两个参数传入
 *
 *
 *
 */
public class SqlTaskExecute
{
    public static void main(String[] args) {

        //约定平台会自动传入以下参数
       /* String taskId = args[0];
        String doDate = args[1];*/

        String taskId = "1";
        String doDate = "2020-06-14";

    }
}
