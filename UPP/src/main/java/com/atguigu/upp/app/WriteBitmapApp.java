package com.atguigu.upp.app;

import com.atguigu.upp.bean.TagInfo;
import com.atguigu.upp.service.CKDBService;
import com.atguigu.upp.service.MysqlDBService;
import com.atguigu.upp.utils.TagValueTypeConstant;
import com.atguigu.upp.utils.UPPUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.spark.sql.SparkSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Smexy on 2023/2/15
 *
 *  目标： 从宽表查询，对用户进行分群，写入分群结果表。
 */
public class WriteBitmapApp
{
    public static void main(String[] args) throws IOException {

         String taskId = args[0];
        String doDate = args[1];

       /* String taskId = "1";
        String doDate = "2020-06-14";*/

        SqlSessionFactory sqlSessionFactory = UPPUtil.createSSF("mysql_db.xml");
        SqlSessionFactory ckSqlSessionFactory = UPPUtil.createSSF("ck_db.xml");
        MysqlDBService mysqlDBService = new MysqlDBService(sqlSessionFactory.openSession());
        CKDBService ckDBService = new CKDBService(ckSqlSessionFactory.openSession());

        //查询当天的宽表中有哪些tag
        List<TagInfo> tags = mysqlDBService.getTaskInfoTodayNeedToExecute();

        //在写入最终数据时，根据tag的值类型，分类，写入到四张表中
        ArrayList<TagInfo> stringTags = new ArrayList<>();
        ArrayList<TagInfo> dateTags = new ArrayList<>();
        ArrayList<TagInfo> intTags = new ArrayList<>();
        ArrayList<TagInfo> decamalTags = new ArrayList<>();

        for (TagInfo tag : tags) {

            switch (tag.getTagValueType()){
                case TagValueTypeConstant.TAG_VALUE_TYPE_LONG : intTags.add(tag); break;
                case TagValueTypeConstant.TAG_VALUE_TYPE_DECIMAL : decamalTags.add(tag); break;
                case TagValueTypeConstant.TAG_VALUE_TYPE_STRING : stringTags.add(tag); break;
                case TagValueTypeConstant.TAG_VALUE_TYPE_DATE: dateTags.add(tag); break;
            }

        }

        //将分类后的标签，通过查询进行分群，写入对应的表中
        //tag_population_attribute_nature_gender,tag_population_attribute_nature_period
        writeData(stringTags,ckDBService,doDate,"user_tag_value_string");
        writeData(dateTags,ckDBService,doDate,"user_tag_value_date");
        writeData(intTags,ckDBService,doDate,"user_tag_value_long");
        //tag_consumer_behavior_order_amount7d
        writeData(decamalTags,ckDBService,doDate,"user_tag_value_decimal");

        //画像平台，如何提交程序，使用spark-submit.只能提交SparkApp
        //所有的Spark程序都有一个入口: SparkContext
        SparkSession sparkSession = UPPUtil.createSparkSession("WriteBitmapApp");


    }

    private static void writeData( ArrayList<TagInfo> tags,CKDBService ckDBService,String doDate,String table){

        //判断当天是否有某种类型的标签需要被计算
        if (tags.size() > 0){

            //为了保障幂等性，先删除当天已经写入的分区的数据
            ckDBService.dropBitmapByDate(doDate,table);
            //再写入
            String sourceTable = UPPUtil.getProperty("upwideprefix") + doDate.replace("-", "_");
            String tagSql = tags.stream().map(t -> String.format(" ('%s', %s ) ", t.getTagCode().toLowerCase(), t.getTagCode().toLowerCase()))
                                 .collect(Collectors.joining(","));
            ckDBService.insertBitmapTable(sourceTable,tagSql,table,doDate);

        }

    }
}
