package com.example.wangx.teachingassistantTeacher.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 公告实体类
 */


public class Notice extends BmobObject implements Serializable{

    // 表主键id,int型
    public Integer Id;

    // 公告标题
    public String title;

    // 公告内容
    public String context;

    //公告时间
    public String time;

    //公告发布教师
    public String t_name;

    // 公告发布班级
    public String course_name;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }
}

