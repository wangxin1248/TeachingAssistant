package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.bean.Work;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.example.wangx.teachingassistantTeacher.util.MyDate;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 发布作业界面
 */
public class AddWorkActivity extends AppCompatActivity {
    ContextUtil app;
    private User user;
    private Spinner sort_by_course;
    private EditText et_work_title;
    private EditText et_work_context;
    private Button bt_work_send;
    private CheckBox ck_work_type;
    private String work_title;
    private String work_context;
    private ArrayAdapter<String> c_adapter;
    private Work work;
    private List<Course> courses;
    private List<Courselist> courselists;
    private String[] c_names;
    private String c_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work);
        //初始化数据库对象
        work=new Work();

        //查找id
        et_work_title = (EditText) findViewById(R.id.et_work_title);
        et_work_context = (EditText) findViewById(R.id.et_work_context);
        bt_work_send = (Button) findViewById(R.id.bt_work_send);
        sort_by_course=(Spinner)findViewById(R.id.sort_by_course);
        ck_work_type=(CheckBox)findViewById(R.id.ck_work_type);

        //获取当前系统的时间
        String format = "yyyy-MM-dd HH:mm:ss";
        final String str= MyDate.getCurrentDate(format);

        app = (ContextUtil) getApplication();
        user = app.getUser();

        //给下拉列表填充数据,用户为教师
        if(1==user.type){
            BmobQuery<Course> query = new BmobQuery<>();
            query.addWhereEqualTo("teacherId",user.userId);
            query.findObjects(AddWorkActivity.this, new FindListener<Course>() {
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
                        //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                        c_adapter = new ArrayAdapter<String>(AddWorkActivity.this, android.R.layout.simple_spinner_item, c_names);
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
                    }else {
                        Toast.makeText(AddWorkActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                        c_names = new String[0];
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(AddWorkActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
        //给下拉列表填充数据，用户为学生
        if (2==user.type){
            // 班级信息获取
            BmobQuery<Courselist> query = new BmobQuery<>();
            query.addWhereEqualTo("studentId",user.userId);
            query.findObjects(AddWorkActivity.this, new FindListener<Courselist>() {
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
                        Toast.makeText(AddWorkActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                        c_names = new String[0];
                    }
                    //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                    c_adapter = new ArrayAdapter<String>(AddWorkActivity.this, android.R.layout.simple_spinner_item, c_names);
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
                    Toast.makeText(AddWorkActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }

        bt_work_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //获取输入的值
                work_title = et_work_title.getText().toString();
                work_context = et_work_context.getText().toString();
                //点击发送按钮后，给数据库对象赋值

                //复选框
                ck_work_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked){
                            work.type=false;
                        }else {
                            work.type=true;
                        }
                    }
                });
                work.title=work_title;
                work.content=work_context;
                work.author=user.userId;
                work.courseName=c_name;
                if ((!work.title.equals(""))&&(!work.content.equals(""))&&(!c_name.equals(""))) {
                    work.save(AddWorkActivity.this,new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(AddWorkActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AddWorkActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                        }
                    });
//
                }else {
                    Toast.makeText(AddWorkActivity.this, "请输入相应的内容再点击发布", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
