package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Grade;
import com.example.wangx.teachingassistantTeacher.bean.Student;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 学生成绩查看界面
 */
public class CheckStuGradeActivity extends AppCompatActivity {

    ContextUtil app;
    private User user;
    private Grade grade;
    private Student student;
    private String coursename;

    private TextView tv_grade_stuName;
    private TextView tv_grade_stuId;
    private TextView tv_grade_courseName;
    private TextView tv_grade_work;
    private TextView tv_grade_test;
    private TextView tv_grade_exam;
    private TextView tv_stugrade_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_stu_grade);

        // 查看当前登陆用户
        app = (ContextUtil) getApplication();
        user = app.getUser();
        String courseName = getIntent().getStringExtra("courseName");
        coursename=courseName;

        tv_grade_stuName = (TextView) findViewById(R.id.tv_grade_stuName);
        tv_grade_stuId = (TextView) findViewById(R.id.tv_grade_stuId);
        tv_grade_courseName = (TextView) findViewById(R.id.tv_grade_courseName);
        tv_grade_work = (TextView) findViewById(R.id.tv_grade_work);
        tv_grade_test = (TextView) findViewById(R.id.tv_grade_test);
        tv_grade_exam = (TextView) findViewById(R.id.tv_grade_exam);
        tv_stugrade_total = (TextView) findViewById(R.id.tv_stugrade_total);


        BmobQuery<Grade> query = new BmobQuery<>();
        query.addWhereEqualTo("courseName", courseName);
        query.addWhereEqualTo("stuId", user.userId);
        query.findObjects(CheckStuGradeActivity.this, new FindListener<Grade>() {
            @Override
            public void onSuccess(List<Grade> list) {
                if (list.size() > 0) {
                    grade = list.get(0);

                    //从Student表中查询学生的姓名
                    BmobQuery<Student> query = new BmobQuery<>();
                    query.addWhereEqualTo("studentId", user.userId);
                    query.findObjects(CheckStuGradeActivity.this, new FindListener<Student>() {
                        @Override
                        public void onSuccess(List<Student> list) {
                            if (list.size() > 0) {
                                student = list.get(0);
                                tv_grade_stuName.setText(student.name);
                                tv_grade_stuId.setText(user.userId);
                                tv_grade_courseName.setText(coursename);
                                tv_grade_work.setText(grade.workgrade + "");
                                tv_grade_test.setText(grade.testgrade + "");
                                tv_grade_exam.setText(grade.examgrade + "");
                                tv_stugrade_total.setText(grade.grade + "");
                            } else {
                                Toast.makeText(CheckStuGradeActivity.this, "数据库没有该信息", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(CheckStuGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(CheckStuGradeActivity.this, "数据库没有该信息", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CheckStuGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
