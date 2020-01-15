package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.adapter.KaoqinListAdapter;
import com.example.wangx.teachingassistantTeacher.bean.Code;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 考勤结果查看界面
 */
public class KaoqinResultActivity extends AppCompatActivity {

    private TextView tv_number;
    private TextView tv_course;
    private static RecyclerView recyclerview;
    private LinearLayoutManager layoutManager;
    private String courseName;

    private KaoqinListAdapter adapter;
    private List<Code> codes;

    private int all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaoqin_result);
        courseName = getIntent().getStringExtra("courseName");

        // 查询所以的点名信息
        BmobQuery<Code> query = new BmobQuery<>();
        query.addWhereEqualTo("courseName",courseName);
        query.order("-createdAt");
        query.findObjects(KaoqinResultActivity.this, new FindListener<Code>() {
            @Override
            public void onSuccess(List<Code> list) {
                codes = list;
                all = list.size();
                // 设置数据适配器
                recyclerview.setAdapter(adapter = new KaoqinListAdapter(codes));

                // 给控件设值
                tv_course = (TextView) findViewById(R.id.tv_course);
                tv_number = (TextView) findViewById(R.id.tv_number);
                tv_course.setText(courseName);
                tv_number.setText("当前课程点名次数："+all);

                // 数据适配器设置点击事件响应
                adapter.setOnItemClickLitener(new KaoqinListAdapter.OnItemClickLitener() {
                    // 点击事件处理
                    @Override
                    public void onItemClick(View view, final int position) {
                        BmobQuery<Courselist> query1 = new BmobQuery<Courselist>();
                        query1.addWhereEqualTo("courseName",courseName);
                        query1.findObjects(KaoqinResultActivity.this, new FindListener<Courselist>() {
                            @Override
                            public void onSuccess(List<Courselist> list) {
                                Intent intent = new Intent(KaoqinResultActivity.this,CheckResultActivity.class);
                                intent.putExtra("code",codes.get(position).getCode());
                                intent.putExtra("all_student",list.size());
                                startActivity(intent);
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(KaoqinResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    // 长按事件处理
                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(KaoqinResultActivity.this, position + " longclick",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(KaoqinResultActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });
        recyclerview=(RecyclerView)findViewById(R.id.recyclerview);
        // 创建布局管理器
        layoutManager=new LinearLayoutManager(KaoqinResultActivity.this,LinearLayoutManager.VERTICAL,false);
        // 设置布局管理器
        recyclerview.setLayoutManager(layoutManager);
    }
}
