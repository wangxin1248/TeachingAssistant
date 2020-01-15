package com.example.wangx.teachingassistantTeacher.bean;

import cn.bmob.v3.BmobObject;

/**
 * 学生实体类
 */
public class Student extends BmobObject{
    // 学生id
    public Integer id;

    // 学号
    public String studentId;

    // 姓名
    public String name;

    // 班级
    public String cla;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCla() {
        return cla;
    }

    public void setCla(String cla) {
        this.cla = cla;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    // 学院
    public String college;

}
