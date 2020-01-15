package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
/**
 * 学生选课界面
 */
public class AddCourseActivity extends AppCompatActivity {
    private Button bt_add_course;
    private Courselist courselist;
    private String[] names;
    private String[] infos;
    private List<Course> courses;
    //定义数组
    private ArrayAdapter<String> adapter;
    private List<Courselist> courselists;
    private String[]c_names;
    private String c_name;
    private Spinner sort_course;
    private TextView tv_course_info;
    ContextUtil app;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        // 查询界面组件
        bt_add_course=(Button)findViewById(R.id.bt_add_course);
        sort_course = (Spinner) findViewById(R.id.sort_course);
        tv_course_info=(TextView)findViewById(R.id.tv_course_info);

        // 获取当前登陆对象
        app = (ContextUtil) getApplication();
        user = app.getUser();

        //从Course表中获取所有的课程信息
        BmobQuery<Courselist> query = new BmobQuery<>();
        query.addWhereEqualTo("studentId",user.userId);
        query.findObjects(AddCourseActivity.this, new FindListener<Courselist>() {
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
                    // 查询学生可选的课程
                    BmobQuery<Course> query1 = new BmobQuery<Course>();
                    query1.addWhereNotContainedIn("courseName", Arrays.asList(c_names));
                    query1.findObjects(AddCourseActivity.this, new FindListener<Course>() {
                        @Override
                        public void onSuccess(List<Course> list) {
                            courses = list;
                            if(courses!=null) {
                                names = new String[courses.size()];
                                infos = new String[courses.size()];
                                //将获取到的课程信息名称存放在数组中
                                int i = 0;
                                for (Course course : courses) {
                                    names[i] = course.courseName;
                                    infos[i] = course.courseInfo;
                                    i++;
                                }
                                //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                                adapter = new ArrayAdapter<String>(AddCourseActivity.this, android.R.layout.simple_spinner_item, names);
                                //为适配器设置下拉列表下拉时的菜单样式
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                //将可选内容添加到下拉列表中
                                sort_course.setAdapter(adapter);
                                //添加Spinner事件监听
                                sort_course.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                                        //name得到了选中的值
                                        c_name = names[arg2];
                                        //将所选的课程信息显示
                                        tv_course_info.setText("课程简介："+infos[arg2]);
                                        //设置显示当前选项的项
                                        arg0.setVisibility(View.VISIBLE);
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> arg0) {
                                        arg0.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(int i, String s) {

                        }
                    });
                }else {
                    Toast.makeText(AddCourseActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                    c_names = new String[0];
                }
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(AddCourseActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });

        bt_add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courselist = new Courselist();
                if (!"".equals(c_name)){
                    courselist.courseName = c_name;
                    courselist.studentId = user.getUserId();
                    courselist.save(AddCourseActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(AddCourseActivity.this,"该门课程学习成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AddCourseActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(AddCourseActivity.this,"Error:请选择课程",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
