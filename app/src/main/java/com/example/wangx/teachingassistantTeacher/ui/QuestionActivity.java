package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.adapter.VoteListAdapter;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.bean.Vote;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
/**
 * 问卷投票界面
 */
public class QuestionActivity extends AppCompatActivity {
    private RecyclerView recycler_question;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private VoteListAdapter adapter;
    ContextUtil app;
    private User user;
    private List<Course> courses;
    private List<Vote> votes;
    private List<Courselist> courselists;
    private String[]c_names;
    // 悬浮按钮
    private FloatingActionsMenu question_fab;
    // 投票
    private com.getbase.floatingactionbutton.FloatingActionButton question_fab_add_vote;
    // 问卷
//    private com.getbase.floatingactionbutton.FloatingActionButton question_fab_add_question;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initView();
        app = (ContextUtil) getApplication();
        user = app.getUser();
        // 学生端不可见发布按钮
        if (2==user.type){
            question_fab.setVisibility(View.INVISIBLE);
        }

        // 教师登陆
        if (1==user.type){
            // 查询教师所带课程
            BmobQuery<Course> query = new BmobQuery<>();
            query.addWhereEqualTo("teacherId",user.userId);
            query.findObjects(QuestionActivity.this, new FindListener<Course>() {
                @Override
                public void onSuccess(List<Course> list) {
                    courses = list;
                    if (null!=courses) {
                        c_names = new String[courses.size()];
                        //将获取到的课程信息名称存放在数组中
                        int i = 0;
                        for (Course course : courses) {
                            c_names[i] = course.courseName;
                            i++;
                        }
                        BmobQuery<Vote> query1 = new BmobQuery<Vote>();
                        query1.addWhereContainedIn("courseName", Arrays.asList(c_names));
                        query1.order("-createdAt");
                        query1.findObjects(QuestionActivity.this, new FindListener<Vote>() {
                            @Override
                            public void onSuccess(List<Vote> list) {
                                votes = list;
                                recycler_question.setAdapter(adapter = new VoteListAdapter(votes,user));
                                // 数据适配器设置点击事件响应
                                adapter.setOnItemClickLitener(new VoteListAdapter.OnItemClickLitener() {
                                    // 点击事件处理
                                    @Override
                                    public void onItemClick(View view, final int position) {
                                        Intent intent = new Intent(QuestionActivity.this,ViewVoteActivity.class);
                                        intent.putExtra("vote",votes.get(position));
                                        startActivity(intent);
                                    }

                                    // 长按事件处理
                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        Toast.makeText(QuestionActivity.this, position + " longclick",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(QuestionActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(QuestionActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                        c_names = new String[0];
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(QuestionActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (2==user.type){
            // 班级信息获取
            BmobQuery<Courselist> query = new BmobQuery<>();
            query.addWhereEqualTo("studentId",user.userId);
            query.findObjects(QuestionActivity.this, new FindListener<Courselist>() {
                @Override
                public void onSuccess(List<Courselist> list) {
                    courselists = list;
                    if (null!=courselists) {
                        c_names = new String[courselists.size()];
                        //将获取到的课程信息名称存放在数组中
                        int i = 0;
                        for (Courselist courselist : courselists) {
                            c_names[i] = courselist.courseName;
                            i++;
                        }
                        BmobQuery<Vote> query1 = new BmobQuery<Vote>();
                        query1.addWhereContainedIn("courseName", Arrays.asList(c_names));
                        query1.order("-createdAt");
                        query1.findObjects(QuestionActivity.this, new FindListener<Vote>() {
                            @Override
                            public void onSuccess(List<Vote> list) {
                                votes = list;
                                recycler_question.setAdapter(adapter = new VoteListAdapter(votes,user));
                                // 数据适配器设置点击事件响应
                                adapter.setOnItemClickLitener(new VoteListAdapter.OnItemClickLitener() {
                                    // 点击事件处理
                                    @Override
                                    public void onItemClick(View view, final int position) {
                                        Intent intent = new Intent(QuestionActivity.this,ViewVoteActivity.class);
                                        intent.putExtra("vote",votes.get(position));
                                        startActivity(intent);
                                    }

                                    // 长按事件处理
                                    @Override
                                    public void onItemLongClick(View view, int position) {
                                        Toast.makeText(QuestionActivity.this, position + " longclick",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(QuestionActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(QuestionActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                        c_names = new String[0];
                    }
                }
                @Override
                public void onError(int i, String s) {
                    Toast.makeText(QuestionActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 初始化界面试图
     */
    private void initView() {
        recycler_question=(RecyclerView)findViewById(R.id.recycler_question);
        // 创建布局管理器
        layoutManager=new LinearLayoutManager(QuestionActivity.this,LinearLayoutManager.VERTICAL,false);
        // 设置布局管理器
        recycler_question.setLayoutManager(layoutManager);

        swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.linearlayout_swipe_refresh) ;
        question_fab = (FloatingActionsMenu) findViewById(R.id.question_fab);
        question_fab_add_vote = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.question_fab_add_vote);
        // 发布投票界面
        question_fab_add_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuestionActivity.this,AddVoteActivity.class));
            }
        });
        //调整SwipeRefreshLayout的位置
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        setListener();//设置监听事件
    }

    /**
     * 下拉刷新
     */
    private void setListener() {
        //swipeRefreshLayout刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            // 刷新操作响应
            @Override
            public void onRefresh() {
                if (1==user.type){
                    // 查询教师所带课程
                    BmobQuery<Course> query = new BmobQuery<>();
                    query.addWhereEqualTo("teacherId",user.userId);
                    query.findObjects(QuestionActivity.this, new FindListener<Course>() {
                        @Override
                        public void onSuccess(List<Course> list) {
                            courses = list;
                            if (null!=courses) {
                                c_names = new String[courses.size()];
                                //将获取到的课程信息名称存放在数组中
                                int i = 0;
                                for (Course course : courses) {
                                    c_names[i] = course.courseName;
                                    i++;
                                }
                                BmobQuery<Vote> query1 = new BmobQuery<Vote>();
                                query1.addWhereContainedIn("courseName", Arrays.asList(c_names));
                                query1.order("-createdAt");
                                query1.findObjects(QuestionActivity.this, new FindListener<Vote>() {
                                    @Override
                                    public void onSuccess(List<Vote> list) {
                                        votes = list;
                                        votes = list;
                                        adapter.replaceItem(votes);
                                        // 不显示刷新控件
                                        swipeRefreshLayout.setRefreshing(false);
                                        Toast.makeText(QuestionActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        Toast.makeText(QuestionActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Toast.makeText(QuestionActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                                c_names = new String[0];
                            }
                        }

                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(QuestionActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (2==user.type){
                    // 班级信息获取
                    BmobQuery<Courselist> query = new BmobQuery<>();
                    query.addWhereEqualTo("studentId",user.userId);
                    query.findObjects(QuestionActivity.this, new FindListener<Courselist>() {
                        @Override
                        public void onSuccess(List<Courselist> list) {
                            courselists = list;
                            if (null!=courselists) {
                                c_names = new String[courselists.size()];
                                //将获取到的课程信息名称存放在数组中
                                int i = 0;
                                for (Courselist courselist : courselists) {
                                    c_names[i] = courselist.courseName;
                                    i++;
                                }
                                BmobQuery<Vote> query1 = new BmobQuery<Vote>();
                                query1.addWhereContainedIn("courseName", Arrays.asList(c_names));
                                query1.order("-createdAt");
                                query1.findObjects(QuestionActivity.this, new FindListener<Vote>() {
                                    @Override
                                    public void onSuccess(List<Vote> list) {
                                        votes = list;
                                        adapter.replaceItem(votes);
                                        // 不显示刷新控件
                                        swipeRefreshLayout.setRefreshing(false);
                                        Toast.makeText(QuestionActivity.this,"刷新成功",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(int i, String s) {
                                        Toast.makeText(QuestionActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Toast.makeText(QuestionActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                                c_names = new String[0];
                            }
                        }
                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(QuestionActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
