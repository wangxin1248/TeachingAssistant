package com.example.wangx.teachingassistantTeacher.bean;

import cn.bmob.v3.BmobObject;

/**
 * 聊天记录实体类
 */
public class Message extends BmobObject{
    private String courseName; // 课程名称
    private String userid;// 发消息用户id
    private String userName;// 用户名称
    private String img;// 用户头像
    private String text;// 发消息内容

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
