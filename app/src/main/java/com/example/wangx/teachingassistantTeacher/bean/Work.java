package com.example.wangx.teachingassistantTeacher.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 课程笔记实体类
 */
public class Work extends BmobObject implements Serializable {
    //笔记标题
    public String title;
    //发布人
    public String author;
    //课程名
    public String courseName;
    //内容
    public String content;
    //是否公开
    public boolean type;
    //时间
    public String time;

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
