package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.wangx.teachingassistantTeacher.R;

/**
 * 成绩界面
 */
public class GradeActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton ib_grade_addgrade;
    private ImageButton ib_grade_checkgrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
        //获取id
        ib_grade_addgrade=(ImageButton)findViewById(R.id.ib_grade_addgrade);
        ib_grade_checkgrade=(ImageButton)findViewById(R.id.ib_grade_checkgrade);

        //设置监听事件
        ib_grade_addgrade.setOnClickListener(this);
        ib_grade_checkgrade.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_grade_addgrade:
                Intent intent = new Intent(GradeActivity.this, AddGradeActivity.class);
                startActivity(intent);
                break;
            //跳转至CheckGradeFragment
            case R.id.ib_grade_checkgrade:
                Intent intent1 = new Intent(GradeActivity.this, CheckGradeCourseActivity.class);
                startActivity(intent1);
                break;

        }

    }
}
