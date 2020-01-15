package com.example.wangx.teachingassistantTeacher.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
/**
 * 资源实体类
 */
public class Resource extends BmobObject implements Serializable{
    // 资源id，主键
    public Integer id;

    // 资源标题
    public String title;


    //资源发布教师
    public String t_name;

    // 资源类型 图片，视频，文档
    public String resource_type;

    // 资源路径，指视频和文档资源
    public String path;

    // 视频文档类型缩略图路径
    public String url;

    // 资源面向课程名称
    public String courseName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
