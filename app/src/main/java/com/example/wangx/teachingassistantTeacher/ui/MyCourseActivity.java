package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 我的成绩界面
 */
public class MyCourseActivity extends AppCompatActivity {
    private String course_name;
    private Course course;

    private String objectId;


    private Button bt_update_radio;
    private Button bt_update_info;

    private LinearLayout layout_radio;
    private EditText et_workratio;
    private EditText et_testratio;
    private EditText et_examratio;
    private EditText et_kaoqinratio;
    private TextView tv_course_name;
    private TextView tv_course_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_course);
        course_name = getIntent().getStringExtra("courseName");
        final BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("courseName",course_name);
        query.findObjects(MyCourseActivity.this, new FindListener<Course>(){
            @Override
            public void onSuccess(List<Course> list) {
                course = list.get(0);
                objectId = list.get(0).getObjectId();
                tv_course_name.setText("课程名称："+course_name);
                tv_course_info.setText(course.courseInfo);
                et_examratio.setText(course.examgRadio+"");
                et_testratio.setText(course.testRadio+"");
                et_workratio.setText(course.workRadio+"");
                et_kaoqinratio.setText(course.kaoqinRadio+"");
                }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyCourseActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });


        //获取id

        bt_update_radio = (Button) findViewById(R.id.bt_update_radio);
        bt_update_info = (Button) findViewById(R.id.bt_update_info);

        et_workratio = (EditText) findViewById(R.id.et_workratio);
        et_testratio = (EditText) findViewById(R.id.et_testratio);
        et_examratio = (EditText) findViewById(R.id.et_examratio);
        et_kaoqinratio = (EditText) findViewById(R.id.et_kaoqinratio);
        layout_radio = (LinearLayout) findViewById(R.id.layout_radio);
        tv_course_info = (TextView) findViewById(R.id.tv_course_info);
        tv_course_name = (TextView) findViewById(R.id.tv_course_name);

        //设置输入法类型
        et_workratio.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_testratio.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_examratio.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_kaoqinratio.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        // 更新课程介绍
        bt_update_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCourseActivity.this,ChangeCourseInfoActivity.class);
                intent.putExtra("courseName",course_name);
                startActivity(intent);
            }
        });
        // 修改课程权重
        bt_update_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCourseActivity.this,ChangeCourseRadioActivity.class);
                intent.putExtra("courseName",course_name);
                startActivity(intent);
            }
        });
    }

}
