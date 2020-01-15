package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 修改课程介绍界面
 */
public class ChangeCourseInfoActivity extends AppCompatActivity {
    private String course_name;
    private Course course;

    private String objectId;
    private Button bt_saveInfo;
    private EditText et_course_info;
    private TextView tv_course_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_course_info);
        course_name = getIntent().getStringExtra("courseName");

        bt_saveInfo = (Button) findViewById(R.id.bt_saveInfo);
        et_course_info = (EditText) findViewById(R.id.et_course_info);
        tv_course_name = (TextView) findViewById(R.id.tv_course_name);

        final BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("courseName",course_name);
        query.findObjects(ChangeCourseInfoActivity.this, new FindListener<Course>(){
            @Override
            public void onSuccess(List<Course> list) {
                course = list.get(0);
                objectId = list.get(0).getObjectId();
                tv_course_name.setText("课程名称："+course_name);
                et_course_info.setText(course.courseInfo);
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(ChangeCourseInfoActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });

        // 开始更新课程介绍
        bt_saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = et_course_info.getText().toString();
                if (!"".equals(info)){
                    course.setCourseInfo(info);
                    course.update(ChangeCourseInfoActivity.this,objectId, new UpdateListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(ChangeCourseInfoActivity.this,"修改课程介绍成功",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(ChangeCourseInfoActivity.this,"Error"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
