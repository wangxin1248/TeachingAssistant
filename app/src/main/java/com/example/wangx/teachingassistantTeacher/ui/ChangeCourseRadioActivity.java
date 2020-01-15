package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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
 * 修改课程占比界面
 */
public class ChangeCourseRadioActivity extends AppCompatActivity {
    private Button bt_agrade_ratio;
    private String course_name;
    private Course course;
    private String objectId;

    private EditText et_workratio;
    private EditText et_testratio;
    private EditText et_examratio;
    private EditText et_kaoqinratio;
    private TextView tv_course_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_course_radio);

        tv_course_name = (TextView) findViewById(R.id.tv_course_name);
        bt_agrade_ratio = (Button) findViewById(R.id.bt_agrade_ratio);
        et_workratio = (EditText) findViewById(R.id.et_workratio);
        et_testratio = (EditText) findViewById(R.id.et_testratio);
        et_examratio = (EditText) findViewById(R.id.et_examratio);
        et_kaoqinratio = (EditText) findViewById(R.id.et_kaoqinratio);

        course_name = getIntent().getStringExtra("courseName");
        final BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("courseName",course_name);
        query.findObjects(ChangeCourseRadioActivity.this, new FindListener<Course>(){
            @Override
            public void onSuccess(List<Course> list) {
                course = list.get(0);
                objectId = list.get(0).getObjectId();
                tv_course_name.setText("课程名称："+course_name);
                et_examratio.setText(course.examgRadio+"");
                et_testratio.setText(course.testRadio+"");
                et_workratio.setText(course.workRadio+"");
                et_kaoqinratio.setText(course.kaoqinRadio+"");
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(ChangeCourseRadioActivity.this,s,Toast.LENGTH_SHORT).show();
            }
        });

        // 更新课程权重
        bt_agrade_ratio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //wxu 2017.4.23ninght 判断输入的课程比重是否为空
                if (!TextUtils.isEmpty(et_workratio.getText()) && !TextUtils.isEmpty(et_testratio.getText()) && !TextUtils.isEmpty(et_examratio.getText())&& !TextUtils.isEmpty(et_kaoqinratio.getText())) {
                    final double workratio = Double.parseDouble(et_workratio.getText().toString());
                    final double testratio = Double.parseDouble(et_testratio.getText().toString());
                    final double examratio = Double.parseDouble(et_examratio.getText().toString());
                    final double kaoqinradio = Double.parseDouble(et_kaoqinratio.getText().toString());

                    //wxu 2017.4.23 判断输入的课程比重加起来为1
                    if (workratio + testratio + examratio + kaoqinradio != 1) {
                        Toast.makeText(ChangeCourseRadioActivity.this, "权重之和应为1", Toast.LENGTH_SHORT).show();
                    } else {
                        BmobQuery<Course> query1=new BmobQuery<Course>();
                        query1.addWhereEqualTo("courseName",course_name);
                        query1.findObjects(ChangeCourseRadioActivity.this, new FindListener<Course>() {
                            @Override
                            public void onSuccess(List<Course> list) {
                                course=list.get(0);
                                if (course != null) {
                                    course.workRadio = workratio;
                                    course.testRadio = testratio;
                                    course.examgRadio = examratio;
                                    course.kaoqinRadio = kaoqinradio;
                                    course.update(ChangeCourseRadioActivity.this,list.get(0).getObjectId(), new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(ChangeCourseRadioActivity.this,"修改权重成功",Toast.LENGTH_SHORT).show();
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            Toast.makeText(ChangeCourseRadioActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(ChangeCourseRadioActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                //wxu 4.23 night
                else
                    Toast.makeText(ChangeCourseRadioActivity.this, "请输入课程的比重", Toast.LENGTH_LONG).show();
            }
        });
    }
}
