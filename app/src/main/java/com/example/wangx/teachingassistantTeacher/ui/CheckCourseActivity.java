package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 添加课程界面
 */
public class CheckCourseActivity extends AppCompatActivity {
    private TextView tv_cource_1;
    private TextView tv_cource_2;
    private TextView tv_cource_3;
    private TextView tv_cource_4;
    private TextView tv_cource_5;
    private TextView tv_cource_6;
    private TextView tv_cource_7;
    private TextView tv_cource_8;
    private TextView tv_cource_9;
    private Button bt_newCourse;
    private LinearLayout layout_1;
    private LinearLayout layout_2;
    private LinearLayout layout_3;
    private LinearLayout layout_4;
    private LinearLayout layout_5;
    private LinearLayout layout_6;
    private LinearLayout layout_7;
    private LinearLayout layout_8;
    private LinearLayout layout_9;
    private User user;
    ContextUtil app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_cource);

        // 查找界面组件
        bt_newCourse=(Button)findViewById(R.id.bt_newCourse);
        tv_cource_1 = (TextView) findViewById(R.id.tv_cource_1);
        tv_cource_2 = (TextView) findViewById(R.id.tv_cource_2);
        tv_cource_3 = (TextView) findViewById(R.id.tv_cource_3);
        tv_cource_4 = (TextView) findViewById(R.id.tv_cource_4);
        tv_cource_5 = (TextView) findViewById(R.id.tv_cource_5);
        tv_cource_6 = (TextView) findViewById(R.id.tv_cource_6);
        tv_cource_7 = (TextView) findViewById(R.id.tv_cource_7);
        tv_cource_8 = (TextView) findViewById(R.id.tv_cource_8);
        tv_cource_9 = (TextView) findViewById(R.id.tv_cource_9);

        layout_1 = (LinearLayout) findViewById(R.id.layout_1);
        layout_2 = (LinearLayout) findViewById(R.id.layout_2);
        layout_3 = (LinearLayout) findViewById(R.id.layout_3);
        layout_4 = (LinearLayout) findViewById(R.id.layout_4);
        layout_5 = (LinearLayout) findViewById(R.id.layout_5);
        layout_6 = (LinearLayout) findViewById(R.id.layout_6);
        layout_7 = (LinearLayout) findViewById(R.id.layout_7);
        layout_8 = (LinearLayout) findViewById(R.id.layout_8);
        layout_9 = (LinearLayout) findViewById(R.id.layout_9);


        // 查看当前登陆用户
        app = (ContextUtil) getApplication();
        user = app.getUser();
        // 查询学生所选课
        BmobQuery<Courselist> query = new BmobQuery<>();
        query.addWhereEqualTo("studentId",user.userId);
        query.findObjects(CheckCourseActivity.this, new FindListener<Courselist>() {
            @Override
            public void onSuccess(List<Courselist> list) {
                if (null!=list){
                    for (int i=0;i<list.size();i++){
                        switch (i){
                            case 0:
                                layout_1.setVisibility(View.VISIBLE);
                                tv_cource_1.setText(list.get(i).courseName);
                                break;
                            case 1:
                                layout_2.setVisibility(View.VISIBLE);
                                tv_cource_2.setText(list.get(i).courseName);
                                break;
                            case 2:
                                layout_3.setVisibility(View.VISIBLE);
                                tv_cource_3.setText(list.get(i).courseName);
                                break;
                            case 3:
                                layout_4.setVisibility(View.VISIBLE);
                                tv_cource_4.setText(list.get(i).courseName);
                                break;
                            case 4:
                                layout_5.setVisibility(View.VISIBLE);
                                tv_cource_5.setText(list.get(i).courseName);
                                break;
                            case 5:
                                layout_6.setVisibility(View.VISIBLE);
                                tv_cource_6.setText(list.get(i).courseName);
                                break;
                            case 6:
                                layout_7.setVisibility(View.VISIBLE);
                                tv_cource_7.setText(list.get(i).courseName);
                                break;
                            case 7:
                                layout_8.setVisibility(View.VISIBLE);
                                tv_cource_8.setText(list.get(i).courseName);
                                break;
                            case 8:
                                layout_9.setVisibility(View.VISIBLE);
                                tv_cource_9.setText(list.get(i).courseName);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(CheckCourseActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });
        /**
         * 添加课程
         */
        bt_newCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckCourseActivity.this,AddCourseActivity.class));
            }
        });
    }
}



