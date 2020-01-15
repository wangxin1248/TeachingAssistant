package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import cn.bmob.v3.Bmob;

/**
 * 欢迎界面
 */
public class WelcomeActivity extends AppCompatActivity {

    private User user;
    ContextUtil app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        app = (ContextUtil) getApplication();
        // 初始化Bmob云
        Bmob.initialize(WelcomeActivity.this,"551cb1a09ea696a94f107d9c002b72f1");

        //判断当前用户是否登陆
        if (null!=app.getUser()){
            finish();
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        }else {
            finish();
            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
        }
//        BmobQuery<User> query = new BmobQuery<>();
//        query.addWhereEqualTo("isLogin",true);
//        query.findObjects(WelcomeActivity.this, new FindListener<User>() {
//            @Override
//            public void onSuccess(List<User> list) {
//                if (list.size()>0){
//                    int type = list.get(0).type;
//                    if (type!=0){
//                        app.setUser(list.get(0));
//                        finish();
//                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
//                    }
//                }else {
//                    finish();
//                   startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
//                }
//
//            }
//            @Override
//            public void onError(int i, String s) {
//                Toast.makeText(WelcomeActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}
