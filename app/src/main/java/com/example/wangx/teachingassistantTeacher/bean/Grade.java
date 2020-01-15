package com.example.wangx.teachingassistantTeacher.bean;


import cn.bmob.v3.BmobObject;
/**
 * 成绩实体类
 */
public class Grade extends BmobObject{

    // 表主键id,int型
    public Integer Id;

    // 课程id
    public String courseName;
    // 学生id
    public String stuId;
    // 作业成绩
    public Double workgrade;
    // 实验成绩
    public Double testgrade;
    // 考试成绩
    public Double examgrade;
    // 考勤成绩
    public Double kaoqingrade;

    public Double getKaoqingrade() {
        return kaoqingrade;
    }

    public void setKaoqingrade(Double kaoqingrade) {
        this.kaoqingrade = kaoqingrade;
    }

    // 总成绩
    public Double grade;

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

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public Double getWorkgrade() {
        return workgrade;
    }

    public void setWorkgrade(Double workgrade) {
        this.workgrade = workgrade;
    }

    public Double getTestgrade() {
        return testgrade;
    }

    public void setTestgrade(Double testgrade) {
        this.testgrade = testgrade;
    }

    public Double getExamgrade() {
        return examgrade;
    }

    public void setExamgrade(Double examgrade) {
        this.examgrade = examgrade;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
}
