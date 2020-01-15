package com.example.wangx.teachingassistantTeacher.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.example.wangx.teachingassistantTeacher.bean.User;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;
/**
 * 获取context工具类
 */
public class ContextUtil extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    // 记录用户登陆账号
    private String user_id;
    private User user;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        instance = this;
        //设置BmobConfig
        BmobConfig config =new BmobConfig.Builder()
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setBlockSize(500*1024)
                .build();
        Bmob.getInstance().initConfig(config);

    }
}
