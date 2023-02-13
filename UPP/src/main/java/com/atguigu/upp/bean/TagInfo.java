package com.atguigu.upp.bean;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class TagInfo {

    Long id;
    String tagCode;
    String tagName;
    Long tagLevel;
    Long parentTagId;
    String tagType;
    String tagValueType;
    Long tagTaskId;
    String tagComment;
    Timestamp createTime;
    Timestamp updateTime;
    
}