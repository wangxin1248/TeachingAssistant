package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Notice;

import java.util.List;

/**
 * 查看提醒界面
 */
public class ViewNoticeActivity extends AppCompatActivity {

    private TextView tv_notice_time;
    private TextView tv_notice_teacher;
    private TextView tv_notice_title;
    private TextView tv_notice_content;
    private TextView tv_notice_course;

    private List<Notice> notices;
    private Notice notice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notice);

        tv_notice_content = (TextView) findViewById(R.id.tv_notice_content);
        tv_notice_title = (TextView) findViewById(R.id.tv_notice_title);
        tv_notice_teacher = (TextView) findViewById(R.id.tv_notice_teacher);
        tv_notice_time = (TextView) findViewById(R.id.tv_notice_time);
        tv_notice_course = (TextView) findViewById(R.id.tv_notice_course);

        int position = getIntent().getIntExtra("position",0);
        notices = (List<Notice>) getIntent().getSerializableExtra("notices");
        notice = notices.get(position);

        tv_notice_content.setText(notice.context);
        tv_notice_teacher.setText("教师："+notice.t_name);
        tv_notice_time.setText(notice.time);
        tv_notice_title.setText(notice.title);
        tv_notice_course.setText("发布班级："+notice.course_name);
    }
}
