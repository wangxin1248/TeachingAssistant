package com.example.wangx.teachingassistantTeacher.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.adapter.GradeListAdapter;
import com.example.wangx.teachingassistantTeacher.bean.Grade;
import com.example.wangx.teachingassistantTeacher.bean.Student;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.example.wangx.teachingassistantTeacher.util.ExcelUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class CheckGradeActivity extends FragmentActivity {
    private static RecyclerView recyclerview;
    public String[] stu_names;
    public String[] stu_ids;
    ContextUtil app;
    private User user;
    private List<Grade> grades;
    private List<Student> students;
    private LinearLayoutManager layoutManager;
    private GradeListAdapter gradeListAdapter;
    private Button export_grade;
    private File file;
    private String courseName;
    private String[] title = {"姓名", "课程", "作业成绩", "实验成绩", "考勤成绩", "考试成绩", "总成绩"};
    private ArrayList<ArrayList<String>> gradeList;

    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_grade);
        export_grade = (Button) findViewById(R.id.export_grade);
        gradeList = new ArrayList<ArrayList<String>>();
        // 查看当前登陆用户
        app = (ContextUtil) getApplication();
        user = app.getUser();
        courseName = getIntent().getStringExtra("courseName");
        BmobQuery<Grade> query = new BmobQuery<>();
        query.addWhereEqualTo("courseName", courseName);
        query.order("-grade");
        query.findObjects(CheckGradeActivity.this, new FindListener<Grade>() {
            @Override
            public void onSuccess(List<Grade> list) {
                if (list.size() > 0) {
                    grades = list;
                    stu_ids = new String[grades.size()];
                    int k = 0;
                    for (Grade grade : grades) {
                        stu_ids[k] = grade.stuId;
                        k++;
                    }

                    BmobQuery<Student> query = new BmobQuery<>();
                    query.addWhereContainedIn("studentId", Arrays.asList(stu_ids));
                    query.findObjects(CheckGradeActivity.this, new FindListener<Student>() {
                        @Override
                        public void onSuccess(List<Student> list) {
                            if (list.size() > 0) {
                                students = list;
                                stu_names = new String[grades.size()];
                                int k = 0;
                                for (Student student : students) {
                                    stu_names[k] = student.name;
                                    k++;
                                }
                            }
                            recyclerview = (RecyclerView) findViewById(R.id.list);
                            // 创建布局管理器
                            layoutManager = new LinearLayoutManager(CheckGradeActivity.this, LinearLayoutManager.VERTICAL, false);
                            // 设置布局管理器
                            recyclerview.setLayoutManager(layoutManager);
                            // 设置数据适配器
                            recyclerview.setAdapter(gradeListAdapter = new GradeListAdapter(grades, user, stu_names));
                        }

                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(CheckGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CheckGradeActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
            }
        });
        export_grade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }

    public void initData() {
        file = new File(getSDPath() + "/teachingassistant");
        String filename = file.toString() + "/"+courseName+"课程学生成绩.xls";
        makeDir(file);
        ExcelUtil.initExcel(filename, title);
        int i =ExcelUtil.writeObjListToExcel(getGradeData(), filename, this);

        // 发布通知提示导出完成
        if (1==i){
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(new File(filename));
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            PendingIntent pi = PendingIntent.getActivities(CheckGradeActivity.this, 0, new Intent[]{intent}, 0);
            // 使用通知来提示用户下载完成
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new NotificationCompat.Builder(CheckGradeActivity.this)
                    .setContentTitle("导出完成")
                    .setContentText("点击查看")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setContentIntent(pi)
                    .build();
            manager.notify(1, notification);
        }else {
            Toast.makeText(CheckGradeActivity.this,"导出失败",Toast.LENGTH_SHORT).show();
        }
    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;

    }

    /**
     * 将学生成绩写入到excel
     * @return
     */
    private ArrayList<ArrayList<String>> getGradeData() {
        ArrayList<String> beanList = new ArrayList<String>();
        for(Grade grade:grades){
            beanList.add(grade.getStuId());
            beanList.add(grade.getCourseName());
            beanList.add(grade.getWorkgrade().toString());
            beanList.add(grade.getTestgrade().toString());
            beanList.add(grade.getKaoqingrade().toString());
            beanList.add(grade.getExamgrade().toString());
            beanList.add(grade.getGrade().toString());
            gradeList.add(beanList);
        }
        return gradeList;
    }
}

