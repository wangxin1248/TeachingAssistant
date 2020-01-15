package com.example.wangx.teachingassistantTeacher.bean;

import cn.bmob.v3.BmobObject;

/**
 * 课程实体类
 */
public class Course extends BmobObject{

    // 表主键id,int型
    public Integer Id;

    // 课程名称
    public String courseName;

    // 课程介绍
    public String courseInfo;

    // 教师id
    public String teacherId;

    // 作业成绩比重
    public Double workRadio;


    // 实验成绩比重
    public Double testRadio;

    // 考试成绩比重
    public Double examgRadio;

    // 考勤成绩比重
    public Double kaoqinRadio;

    public Double getKaoqinRadio() {
        return kaoqinRadio;
    }

    public void setKaoqinRadio(Double kaoqinRadio) {
        this.kaoqinRadio = kaoqinRadio;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(String courseInfo) {
        this.courseInfo = courseInfo;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public Double getWorkRadio() {
        return workRadio;
    }

    public void setWorkRadio(Double workRadio) {
        this.workRadio = workRadio;
    }

    public Double getTestRadio() {
        return testRadio;
    }

    public void setTestRadio(Double testRadio) {
        this.testRadio = testRadio;
    }

    public Double getExamgRadio() {
        return examgRadio;
    }

    public void setExamgRadio(Double examgRadio) {
        this.examgRadio = examgRadio;
    }
}
