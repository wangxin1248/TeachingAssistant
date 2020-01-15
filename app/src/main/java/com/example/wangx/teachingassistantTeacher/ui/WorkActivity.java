package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.adapter.WorkListAdapter;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.bean.Work;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 作业界面
 */
public class WorkActivity extends AppCompatActivity {
    private static RecyclerView recyclerview;
    ContextUtil app;
    private User user;
    private LinearLayoutManager layoutManager;
    private WorkListAdapter workListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton work_fab;
    private List<Work> works1;
    private List<Work> works2;
    private List<Work> works;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        works = new ArrayList<Work>();

        app = (ContextUtil) getApplication();
        user = app.getUser();
        work_fab = (FloatingActionButton) findViewById(R.id.work_fab);

        //条件查询
        BmobQuery<Work> query = new BmobQuery<>();
        query.addWhereEqualTo("type", true);
        query.findObjects(WorkActivity.this, new FindListener<Work>() {
            @Override
            public void onSuccess(List<Work> list) {
                if (list.size() > 0) {
                    works1 = list;
                    BmobQuery<Work> query1 = new BmobQuery<>();
                    query1.addWhereEqualTo("author", user.userId);
                    query1.findObjects(WorkActivity.this, new FindListener<Work>() {
                        @Override
                        public void onSuccess(List<Work> list) {
                            if (list.size() > 0) {
                                works1 = list;
                            }
                            // 逆序集合
                            Collections.reverse(works1);
                            Context context = WorkActivity.this;
                            recyclerview = (RecyclerView) findViewById(R.id.linearlayout_recycler);
                            // 创建布局管理器
                            layoutManager = new LinearLayoutManager(WorkActivity.this, LinearLayoutManager.VERTICAL, false);
                            // 设置布局管理器
                            recyclerview.setLayoutManager(layoutManager);
                            // 设置数据适配器
                            recyclerview.setAdapter(workListAdapter = new WorkListAdapter(works1,context));

                            // 数据适配器设置点击事件响应
                            workListAdapter.setOnItemClickLitener(new WorkListAdapter.OnItemClickLitener() {
                                // 点击事件处理
                                @Override
                                public void onItemClick(View view, int position) {

                                    Intent intent = new Intent(WorkActivity.this, ViewWorkActivity.class);
                                    intent.putExtra("works", (Serializable) works1);
                                    intent.putExtra("position", position);
                                    startActivity(intent);
                                }
                                // 长按事件处理
                                @Override
                                public void onItemLongClick(View view, int position) {
                                    Toast.makeText(WorkActivity.this, position + " longclick",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(WorkActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(WorkActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
            }
        });
        initView();//初始化布局
        setListener();//设置监听事件
        work_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WorkActivity.this, AddWorkActivity.class));
            }
        });

    }

    /**
     * 初始化布局
     */
    private void initView() {

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.linearlayout_swipe_refresh);
        //调整SwipeRefreshLayout的位置
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
    }

    /**
     * 设置监听事件
     */
    private void setListener() {
        //swipeRefreshLayout刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            // 刷新操作响应
            @Override
            public void onRefresh() {
                //刷新操作，重新查询一遍并更新
                BmobQuery<Work> query = new BmobQuery<>();
                query.addWhereEqualTo("type", true);
                query.findObjects(WorkActivity.this, new FindListener<Work>() {
                    @Override
                    public void onSuccess(List<Work> list) {
                        if (list.size() > 0) {
                            works1 = list;
                            BmobQuery<Work> query1 = new BmobQuery<>();
                            query1.addWhereEqualTo("author", user.userId);
                            query1.findObjects(WorkActivity.this, new FindListener<Work>() {
                                @Override
                                public void onSuccess(List<Work> list) {
                                    if (list.size() > 0) {
                                        works1 = list;
                                        Collections.reverse(works1);
                                        workListAdapter.replaceItem(works1);
                                        // 不显示刷新控件
                                        swipeRefreshLayout.setRefreshing(false);
                                        Toast.makeText(WorkActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(WorkActivity.this, "您未发布过笔记", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onError(int i, String s) {
                                    Toast.makeText(WorkActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();

                                }
                            });
                        } else {
                            Toast.makeText(WorkActivity.this, "没有其他公开的笔记", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(WorkActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
