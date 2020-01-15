package com.example.wangx.teachingassistantTeacher.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 投票实体类
 */
public class Select extends BmobObject implements Serializable{
    private String id;// vote表对应的objectId
    private String userId;// 投票用户id
    private String select;// 用户的选择

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }
}
