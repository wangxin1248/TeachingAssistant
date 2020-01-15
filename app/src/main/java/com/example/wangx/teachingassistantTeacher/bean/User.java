package com.example.wangx.teachingassistantTeacher.bean;

import cn.bmob.v3.BmobObject;

/**
 * 用户实体表
 */
public class User extends BmobObject {

    // 表主键id,int型
    public Integer Id;

    // 账号
    public String userId;

    // 用户名
    public String userName;

    // 密码
    public String password;

    // 用户类型 1：教师 2：学生
    public Integer type;

    // 头像
    public String image;

    // 是否登陆
    public Boolean isLogin;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getLogin() {
        return isLogin;
    }

    public void setLogin(Boolean login) {
        isLogin = login;
    }
}
