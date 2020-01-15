package com.example.wangx.teachingassistantTeacher.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 投票实体类
 */
public class Vote extends BmobObject implements Serializable{
    private String name;// 投票题目
    private String courseName;//课程名称
    private Integer type;//投票类型
    private String time;// 结束时间
    private String userId;// 发布用户id
    private String selects;//选项，以&&-&&结束

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSelects() {
        return selects;
    }

    public void setSelects(String selects) {
        this.selects = selects;
    }
}
