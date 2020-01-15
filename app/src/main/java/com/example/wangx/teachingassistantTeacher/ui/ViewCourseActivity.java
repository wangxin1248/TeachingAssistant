package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 查看成绩界面
 */
public class ViewCourseActivity extends AppCompatActivity {
    private TextView tv_cource_1;
    private TextView tv_cource_2;
    private TextView tv_cource_3;
    private TextView tv_cource_4;
    private TextView tv_cource_5;
    private TextView tv_cource_6;
    private TextView tv_cource_7;
    private TextView tv_cource_8;
    private TextView tv_cource_9;
    private TextView tv_cource_10;
    private LinearLayout layout_1;
    private LinearLayout layout_2;
    private LinearLayout layout_3;
    private LinearLayout layout_4;
    private LinearLayout layout_5;
    private LinearLayout layout_6;
    private LinearLayout layout_7;
    private LinearLayout layout_8;
    private LinearLayout layout_9;
    private LinearLayout layout_10;
    private Course course = new Course();
    private List<Course> courses;
    private User user;
    ContextUtil app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        tv_cource_1 = (TextView) findViewById(R.id.tv_cource_1);
        tv_cource_2 = (TextView) findViewById(R.id.tv_cource_2);
        tv_cource_3 = (TextView) findViewById(R.id.tv_cource_3);
        tv_cource_4 = (TextView) findViewById(R.id.tv_cource_4);
        tv_cource_5 = (TextView) findViewById(R.id.tv_cource_5);
        tv_cource_6 = (TextView) findViewById(R.id.tv_cource_6);
        tv_cource_7 = (TextView) findViewById(R.id.tv_cource_7);
        tv_cource_8 = (TextView) findViewById(R.id.tv_cource_8);
        tv_cource_9 = (TextView) findViewById(R.id.tv_cource_9);
        tv_cource_10 = (TextView) findViewById(R.id.tv_cource_10);
        layout_1 = (LinearLayout) findViewById(R.id.layout_1);
        layout_2 = (LinearLayout) findViewById(R.id.layout_2);
        layout_3 = (LinearLayout) findViewById(R.id.layout_3);
        layout_4 = (LinearLayout) findViewById(R.id.layout_4);
        layout_5 = (LinearLayout) findViewById(R.id.layout_5);
        layout_6 = (LinearLayout) findViewById(R.id.layout_6);
        layout_7 = (LinearLayout) findViewById(R.id.layout_7);
        layout_8 = (LinearLayout) findViewById(R.id.layout_8);
        layout_9 = (LinearLayout) findViewById(R.id.layout_9);
        layout_10 = (LinearLayout) findViewById(R.id.layout_10);
        app = (ContextUtil) getApplication();
        user = app.getUser();
        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherId",user.userId);
        query.findObjects(ViewCourseActivity.this, new FindListener<Course>(){
            @Override
            public void onSuccess(List<Course> list) {
                courses=list;
                final Intent intent = new Intent(ViewCourseActivity.this,MyCourseActivity.class);
                if (null!=courses){
                    for (int i=0;i<courses.size();i++){
                        switch (i){
                            case 0:
                                layout_1.setVisibility(View.VISIBLE);
                                tv_cource_1.setText(courses.get(i).courseName);
                                final int finalI = i;
                                tv_cource_1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case 1:
                                layout_2.setVisibility(View.VISIBLE);
                                tv_cource_2.setText(courses.get(i).courseName);
                                final int finalI1 = i;
                                tv_cource_2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI1).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case 2:
                                layout_3.setVisibility(View.VISIBLE);
                                tv_cource_3.setText(courses.get(i).courseName);
                                final int finalI2 = i;
                                tv_cource_3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI2).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case 3:
                                layout_4.setVisibility(View.VISIBLE);
                                tv_cource_4.setText(courses.get(i).courseName);
                                final int finalI3 = i;
                                tv_cource_4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI3).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case 4:
                                layout_5.setVisibility(View.VISIBLE);
                                tv_cource_5.setText(courses.get(i).courseName);
                                final int finalI4 = i;
                                tv_cource_5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI4).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case 5:
                                layout_6.setVisibility(View.VISIBLE);
                                tv_cource_6.setText(courses.get(i).courseName);
                                final int finalI5 = i;
                                tv_cource_6.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI5).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case 6:
                                layout_7.setVisibility(View.VISIBLE);
                                tv_cource_7.setText(courses.get(i).courseName);
                                final int finalI6 = i;
                                tv_cource_7.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI6).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case 7:
                                layout_8.setVisibility(View.VISIBLE);
                                tv_cource_8.setText(courses.get(i).courseName);
                                final int finalI7 = i;
                                tv_cource_8.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI7).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case 8:
                                layout_9.setVisibility(View.VISIBLE);
                                tv_cource_9.setText(courses.get(i).courseName);
                                final int finalI8 = i;
                                tv_cource_9.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI8).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            case 9:
                                layout_10.setVisibility(View.VISIBLE);
                                tv_cource_10.setText(courses.get(i).courseName);
                                final int finalI9 = i;
                                tv_cource_10.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        intent.putExtra("courseName",courses.get(finalI9).courseName);
                                        startActivity(intent);
                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    }
                }

            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(ViewCourseActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
