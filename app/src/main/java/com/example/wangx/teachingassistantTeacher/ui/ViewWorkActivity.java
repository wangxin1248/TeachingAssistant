package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.bean.Work;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 作业查看界面
 */
public class ViewWorkActivity extends AppCompatActivity {
    private TextView tv_work_title;
    private TextView tv_work_author;
    private TextView tv_work_time;
    private TextView tv_work_course;
    private TextView tv_work_content;
    private List<Work> works;
    private Work work;
    private String author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_work);

        tv_work_title=(TextView)findViewById(R.id.tv_work_title);
        tv_work_author=(TextView)findViewById(R.id.tv_work_author);
        tv_work_time=(TextView)findViewById(R.id.tv_work_time);
        tv_work_course=(TextView)findViewById(R.id.tv_work_course);
        tv_work_content=(TextView)findViewById(R.id.tv_work_content);

        int position = getIntent().getIntExtra("position",0);
        works = (List<Work>) getIntent().getSerializableExtra("works");
        work=works.get(position);
        BmobQuery<User>query=new BmobQuery<>();
        query.addWhereEqualTo("userId",work.author);
        query.findObjects(ViewWorkActivity.this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                author=list.get(0).userName;
                tv_work_author.setText(author);
            }

            @Override
            public void onError(int i, String s) {

            }
        });

        tv_work_title.setText(work.getTitle());
        tv_work_time.setText(work.getCreatedAt());
        tv_work_course.setText(work.getCourseName());
        tv_work_content.setText(work.getContent());
    }
}
