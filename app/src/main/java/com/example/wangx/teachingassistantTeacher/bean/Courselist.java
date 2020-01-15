package com.example.wangx.teachingassistantTeacher.bean;

import cn.bmob.v3.BmobObject;

/**
 * 选课实体类
 */
public class Courselist extends BmobObject{

    // 表主键id,int型
    public Integer Id;

    // 学号
    public String studentId;

    // 课程名称
    public String courseName;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}

