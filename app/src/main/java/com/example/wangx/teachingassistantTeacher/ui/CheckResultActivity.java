package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.adapter.CheckListAdapter;
import com.example.wangx.teachingassistantTeacher.bean.Check;
import com.example.wangx.teachingassistantTeacher.bean.Student;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 签到结果查看界面
 */
public class CheckResultActivity extends AppCompatActivity {
    private String code;
    private TextView tv_code;
    private TextView tv_result;
    private static RecyclerView recyclerview;
    private LinearLayoutManager layoutManager;
    private CheckListAdapter adapter;

    private List<Check> checks;
    private String[] names;
    private int all_student;
    private int all_check_student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_result);
        // 获取从上个界面传递过来的值
        code = getIntent().getStringExtra("code");
        all_student = getIntent().getIntExtra("all_student",0);
        // 初始化组件
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_result = (TextView) findViewById(R.id.tv_result);
        recyclerview=(RecyclerView)findViewById(R.id.check_recyclerview);
        // 创建布局管理器
        layoutManager=new LinearLayoutManager(CheckResultActivity.this,LinearLayoutManager.VERTICAL,false);
        // 设置布局管理器
        recyclerview.setLayoutManager(layoutManager);
        if (!"".equals(code)){
            // 界面设值
            tv_code.setText(code);
            // 查询点名信息
            BmobQuery<Check> query = new BmobQuery<>();
            query.addWhereEqualTo("code",code);
            // 按签到时间排序
            query.order("-createdAt");
            query.findObjects(CheckResultActivity.this, new FindListener<Check>() {
                @Override
                public void onSuccess(final List<Check> list) {
                    checks = list;
                    names = new String[list.size()];
                    int i=0;
                    for (Check check:list){
                        names[i++] = check.getStudentId();
                    }
                    BmobQuery<Student> query1 = new BmobQuery<Student>();
                    query1.addWhereContainedIn("studentId", Arrays.asList(names));
                    query1.findObjects(CheckResultActivity.this, new FindListener<Student>() {
                        @Override
                        public void onSuccess(List<Student> list1) {
                            for (int j=0;j<list1.size();j++){
                                names[j] = list1.get(j).name;
                            }
                            all_check_student = list.size();
                            tv_result.setText(all_check_student+"/"+all_student);
                            recyclerview.setAdapter(adapter = new CheckListAdapter(checks,names));
                        }

                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(CheckResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onError(int i, String s) {
                    Toast.makeText(CheckResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            Toast.makeText(CheckResultActivity.this,"Error null",Toast.LENGTH_SHORT).show();
        }
    }
}
