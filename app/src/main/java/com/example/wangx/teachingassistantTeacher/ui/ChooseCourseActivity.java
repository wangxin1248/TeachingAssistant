package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 选择课程界面
 */
public class ChooseCourseActivity extends AppCompatActivity {
    private Button bt_goChat;
    ContextUtil app;
    private User user;
    private Spinner sort_by_course;
    private List<Course> courses;
    private List<Courselist> courselists;
    private String[]c_names;
    private String c_name;
    private ArrayAdapter<String> c_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_course);

        sort_by_course = (Spinner) findViewById(R.id.sort_by_course);
        bt_goChat = (Button) findViewById(R.id.bt_goChat);

        // 查看当前登陆用户
        app = (ContextUtil) getApplication();
        user = app.getUser();
        // 登陆用户为教师
        if (1==user.type){
            // 班级信息获取
            BmobQuery<Course> query = new BmobQuery<>();
            query.addWhereEqualTo("teacherId",user.userId);
            query.findObjects(ChooseCourseActivity.this, new FindListener<Course>() {
                @Override
                public void onSuccess(List<Course> list) {
                    courses = list;
                    if (null!=courses) {
                        c_names = new String[courses.size()];
                        //将获取到的课程信息名称存放在数组中
                        int i = 0;
                        for (Course course : courses) {
                            c_names[i] = course.courseName;
                            i++;
                        }
                    }else {
                        Toast.makeText(ChooseCourseActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                        c_names = new String[0];
                    }
                    //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                    c_adapter = new ArrayAdapter<String>(ChooseCourseActivity.this, android.R.layout.simple_spinner_item, c_names);
                    //为适配器设置下拉列表下拉时的菜单样式
                    c_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //将可选内容添加到下拉列表中
                    sort_by_course.setAdapter(c_adapter);

                    sort_by_course.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            //c_name得到了选中的值
                            c_name = c_names[arg2];
                            //设置显示当前选项的项
                            arg0.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            arg0.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(ChooseCourseActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
        // 登陆用户为学生
        if (2==user.type){
            // 班级信息获取
            BmobQuery<Courselist> query = new BmobQuery<>();
            query.addWhereEqualTo("studentId",user.userId);
            query.findObjects(ChooseCourseActivity.this, new FindListener<Courselist>() {
                @Override
                public void onSuccess(List<Courselist> list) {
                    courselists = list;
                    if (null!=courselists) {
                        c_names = new String[courselists.size()];
                        //将获取到的课程信息名称存放在数组中
                        int i = 0;
                        for (Courselist courselist : courselists) {
                            c_names[i] = courselist.courseName;
                            i++;
                        }
                    }else {
                        Toast.makeText(ChooseCourseActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                        c_names = new String[0];
                    }
                    //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                    c_adapter = new ArrayAdapter<String>(ChooseCourseActivity.this, android.R.layout.simple_spinner_item, c_names);
                    //为适配器设置下拉列表下拉时的菜单样式
                    c_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //将可选内容添加到下拉列表中
                    sort_by_course.setAdapter(c_adapter);

                    sort_by_course.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            //c_name得到了选中的值
                            c_name = c_names[arg2];
                            //设置显示当前选项的项
                            arg0.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            arg0.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(ChooseCourseActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }


        // 进入对应聊天室
        bt_goChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseCourseActivity.this,TalkActivity.class);
                intent.putExtra("courseName",c_name);
                startActivity(intent);
            }
        });
    }
}
