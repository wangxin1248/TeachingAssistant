package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Check;
import com.example.wangx.teachingassistantTeacher.bean.Code;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.Grade;
import com.example.wangx.teachingassistantTeacher.bean.Student;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 添加成绩界面
 */
public class AddGradeActivity extends AppCompatActivity {

    public String[] stu_names;
    ContextUtil app;
    private Button bt_agrade_add;
    private Spinner sort_the_course;
    private Spinner sort_by_student;
    private EditText et_workgrade;
    private EditText et_testgrade;
    private EditText et_examgrade;
    private User user;
    private String[] c_names;
    private String c_name;
    private String stu_name;
    private String studentId;
    private String courseName;
    private Course course;
    private String[] stu_ids;
    private String objectId;

    private List<Student> students;
    private List<Course> courses;
    private List<Courselist> courselists;

    //定义数组
    private ArrayAdapter<String> c_adapter;
    private ArrayAdapter<String> stu_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);

        app = (ContextUtil) getApplication();
        user = app.getUser();
        //获取id
        bt_agrade_add = (Button) findViewById(R.id.bt_agrade_add);
        sort_the_course = (Spinner) findViewById(R.id.sort_the_course);
        sort_by_student = (Spinner) findViewById(R.id.sort_by_student);
        et_workgrade = (EditText) findViewById(R.id.et_workgrade);
        et_testgrade = (EditText) findViewById(R.id.et_testgrade);
        et_examgrade = (EditText) findViewById(R.id.et_examgrade);

        //设置输入类型
        et_workgrade.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_testgrade.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        et_examgrade.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherId", user.userId);
        query.findObjects(AddGradeActivity.this, new FindListener<Course>() {
            @Override
            public void onSuccess(List<Course> list) {
                courses = list;
                if (null != courses) {
                    c_names = new String[courses.size()];
                    //将获取到的课程信息名称存放在数组中
                    int i = 0;
                    for (Course course : courses) {
                        c_names[i] = course.courseName;
                        i++;
                    }
                }
                //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                c_adapter = new ArrayAdapter<String>(AddGradeActivity.this, android.R.layout.simple_spinner_item, c_names);
                //为适配器设置下拉列表下拉时的菜单样式
                c_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //将可选内容添加到下拉列表中
                sort_the_course.setAdapter(c_adapter);

            }

            @Override
            public void onError(int i, String s) {
                //没查到教师所带的课程
                Toast.makeText(AddGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();

            }
        });

        //设置添加成绩选课的spinner的事件监听
        sort_the_course.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //c_name得到了选中的值
                c_name = c_names[arg2];
                courseName = c_name;
                BmobQuery<Courselist> query1 = new BmobQuery<Courselist>();
                query1.addWhereEqualTo("courseName", c_name);
                query1.findObjects(AddGradeActivity.this, new FindListener<Courselist>() {
                    @Override
                    public void onSuccess(List<Courselist> list) {
                        //从Courselist表中获取所有的选课信息中获取学生的id
                        if (null != list) {
                            courselists = list;
                            stu_ids = new String[courselists.size()];
                            //将获取到的学号信息存放在数组中
                            int j = 0;
                            for (Courselist courselist : list) {
                                stu_ids[j] = courselist.studentId;
                                j++;
                            }

                            BmobQuery<Student> query2 = new BmobQuery<Student>();
                            query2.addWhereContainedIn("studentId", Arrays.asList(stu_ids));
                            query2.findObjects(AddGradeActivity.this, new FindListener<Student>() {
                                @Override
                                public void onSuccess(List<Student> list) {
                                    if (list != null) {
                                        students = list;
                                        stu_names = new String[students.size()];
                                        int k = 0;
                                        for (Student student : students) {
                                            stu_names[k] = student.name;
                                            k++;//wxu 4.24 am
                                        }
                                        //为下拉列表定义一个适配器，将可选学生姓名内容与ArrayAdapter连接
                                        stu_adapter = new ArrayAdapter<String>(AddGradeActivity.this, android.R.layout.simple_spinner_item, stu_names);
                                        //为适配器设置下拉列表下拉时的菜单样式
                                        stu_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        //将可选内容添加到下拉列表中
                                        sort_by_student.setAdapter(stu_adapter);
                                    }
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Toast.makeText(AddGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();

                                }
                            });
//
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        //没有查询到选课表信息
                        Toast.makeText(AddGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();

                    }
                });

                BmobQuery<Course> query2 = new BmobQuery<>();
                query2.addWhereEqualTo("courseName", courseName);
                query2.findObjects(AddGradeActivity.this, new FindListener<Course>() {
                    @Override
                    public void onSuccess(List<Course> list) {
                        course = list.get(0);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(AddGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
                //没有选中课程
            }
        });

        //事件监听
        sort_by_student.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            private Grade grade;

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                stu_name = stu_names[arg2];
                // 显示控件清空
                et_examgrade.setText("");
                et_testgrade.setText("");
                et_workgrade.setText("");
                BmobQuery<Student> query3 = new BmobQuery<Student>();
                query3.addWhereEqualTo("name", stu_name);
                query3.findObjects(AddGradeActivity.this, new FindListener<Student>() {
                    @Override
                    public void onSuccess(List<Student> list) {
                        studentId = list.get(0).studentId;
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(AddGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
                    }
                });

                arg0.setVisibility(View.VISIBLE);

                BmobQuery<Grade> query1 = new BmobQuery<Grade>();
                query1.addWhereEqualTo("courseName", courseName);
                query1.addWhereEqualTo("stuId", studentId);
                query1.findObjects(AddGradeActivity.this, new FindListener<Grade>() {
                    @Override
                    public void onSuccess(List<Grade> list) {
                        if (list.size() > 0) {
                            grade = list.get(0);
                            objectId = list.get(0).getObjectId();
                            et_examgrade.setText(grade.examgrade + "");
                            et_testgrade.setText(grade.testgrade + "");
                            et_workgrade.setText(grade.workgrade + "");
                            bt_agrade_add.setText("修改成绩");

                            bt_agrade_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!TextUtils.isEmpty(et_workgrade.getText()) && !TextUtils.isEmpty(et_testgrade.getText()) && !TextUtils.isEmpty(et_examgrade.getText())) {
                                        double workgrade = Double.parseDouble(et_workgrade.getText().toString());
                                        double testgrade = Double.parseDouble(et_testgrade.getText().toString());
                                        double examgrade = Double.parseDouble(et_examgrade.getText().toString());
                                        grade.setCourseName(courseName);
                                        grade.setStuId(studentId);
                                        grade.setExamgrade(examgrade);
                                        grade.setTestgrade(testgrade);
                                        grade.setWorkgrade(workgrade);
                                        grade.setGrade(course.examgRadio * examgrade + course.testRadio * testgrade + course.workRadio * workgrade);
                                        grade.update(AddGradeActivity.this, objectId, new UpdateListener() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(AddGradeActivity.this, "修改成绩成功", Toast.LENGTH_SHORT).show();

                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                                Toast.makeText(AddGradeActivity.this, "修改成绩失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            bt_agrade_add.setText("录入成绩");
                            et_examgrade.setText("");
                            et_testgrade.setText("");
                            et_workgrade.setText("");
                            bt_agrade_add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(AddGradeActivity.this, "输入新成绩", Toast.LENGTH_SHORT).show();
                                    if (!TextUtils.isEmpty(et_workgrade.getText()) && !TextUtils.isEmpty(et_testgrade.getText()) && !TextUtils.isEmpty(et_examgrade.getText())) {
                                        final double workgrade = Double.parseDouble(et_workgrade.getText().toString());
                                        final double testgrade = Double.parseDouble(et_testgrade.getText().toString());
                                        final double examgrade = Double.parseDouble(et_examgrade.getText().toString());
                                        // 计算学生考勤成绩
                                        BmobQuery<Code> query2 = new BmobQuery<Code>();
                                        query2.addWhereEqualTo("courseName", c_name);
                                        query2.findObjects(AddGradeActivity.this, new FindListener<Code>() {
                                            @Override
                                            public void onSuccess(List<Code> list) {
                                                final int all_code = list.size();
                                                BmobQuery<Check> query4 = new BmobQuery<Check>();
                                                query4.addWhereEqualTo("courseName", c_name);
                                                query4.addWhereEqualTo("studentId", studentId);
                                                query4.findObjects(AddGradeActivity.this, new FindListener<Check>() {
                                                    @Override
                                                    public void onSuccess(List<Check> list) {
                                                        int all_student = list.size();

                                                        // 计算考勤成绩
                                                        double kaoqingrade = all_student / all_code;

                                                        // 录入成绩
                                                        Grade grade = new Grade();
                                                        grade.courseName = courseName;
                                                        grade.stuId = studentId;
                                                        grade.examgrade = examgrade;
                                                        grade.workgrade = workgrade;
                                                        grade.testgrade = testgrade;
                                                        grade.kaoqingrade = kaoqingrade;
                                                        grade.grade = course.examgRadio * examgrade + course.testRadio * testgrade + course.workRadio * workgrade+course.kaoqinRadio*kaoqingrade;
                                                        grade.save(AddGradeActivity.this, new SaveListener() {
                                                            @Override
                                                            public void onSuccess() {
                                                                Toast.makeText(AddGradeActivity.this, "插入成绩成功", Toast.LENGTH_SHORT).show();
                                                            }

                                                            @Override
                                                            public void onFailure(int i, String s) {
                                                                Toast.makeText(AddGradeActivity.this, "插入成绩不成功", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onError(int i, String s) {
                                                        Toast.makeText(AddGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(int i, String s) {
                                                Toast.makeText(AddGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                            });


                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(AddGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
                //没有选中学生
            }
        });

    }
}
