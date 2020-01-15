package com.example.wangx.teachingassistantTeacher.ui;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Notice;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 发布通知界面
 */
public class AddNoticeActivity extends AppCompatActivity {
    private Spinner sort_by_course;
    private EditText et_notice_title;
    private EditText et_notice_context;
    private Button bt_notice_send;
    private String notice_title;
    private String notice_context;
    private ArrayAdapter<String> c_adapter;

    private Notice notice;
    private User user;
    private List<Course>courses;
    private String[]c_names;
    private String c_name;
    ContextUtil app;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notice);

        sort_by_course = (Spinner) findViewById(R.id.sort_by_course);

        /**
         *初始化数据库对象*/
        notice = new Notice();

        //查找id
        et_notice_title = (EditText) findViewById(R.id.et_notice_title);
        et_notice_context = (EditText) findViewById(R.id.et_notice_context);
        bt_notice_send = (Button) findViewById(R.id.bt_notice_send);
        sort_by_course=(Spinner)findViewById(R.id.sort_by_course);

        //获取当前系统的时间
        SimpleDateFormat formater =new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate=new Date(System.currentTimeMillis());
        final String str=formater.format(curDate);

        app = (ContextUtil) getApplication();
        user = app.getUser();

        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherId",user.userId);
        query.findObjects(AddNoticeActivity.this, new FindListener<Course>() {
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
                    Toast.makeText(AddNoticeActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                    c_names = new String[0];
                }
                //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                c_adapter = new ArrayAdapter<String>(AddNoticeActivity.this, android.R.layout.simple_spinner_item, c_names);
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
                Toast.makeText(AddNoticeActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });

        //设置点击事件
        bt_notice_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取输入的值
                notice_title = et_notice_title.getText().toString();
                notice_context = et_notice_context.getText().toString();
                //点击发送按钮后，给数据库对象赋值
                notice.title = notice_title;
                notice.context = notice_context;
                notice.t_name = user.userName;
                notice.time=str;
                notice.course_name = c_name;
                if ((!notice.title.equals(""))&&(!notice.context.equals(""))&&(!c_name.equals(""))) {
//                    long flag = noticeDAO.insert(notice);
                    notice.save(AddNoticeActivity.this,new SaveListener() {
                        @Override
                        public void onSuccess() {
                            // 添加Bmob云推送

                            Toast.makeText(AddNoticeActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AddNoticeActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                        }
                    });
//                    if (flag > 0) {
//                        Toast.makeText(AddNoticeActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        Toast.makeText(AddNoticeActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
//                    }
                }else {
                    Toast.makeText(AddNoticeActivity.this, "请输入相应的内容在点击发布", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
