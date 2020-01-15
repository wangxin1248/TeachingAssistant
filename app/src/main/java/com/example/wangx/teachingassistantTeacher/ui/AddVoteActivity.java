package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.bean.Vote;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.example.wangx.teachingassistantTeacher.util.DataCallBack;
import com.example.wangx.teachingassistantTeacher.util.DatePickerFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 发布投票界面
 */
public class AddVoteActivity extends AppCompatActivity implements DataCallBack {
    private Button bt_publish;// 发布按钮
    private EditText et_vote_name;// 投票名称
    private List<EditText> selects;//选项集合
    private int i;//添加布局数目
    private ImageView iv_select_add;// 添加选项
    private LinearLayout layout_check;// 选项布局
    private Spinner sp_vote_type;// 投票类型
    private TextView tv_time;// 结束时间
    private Spinner sort_by_course; //选择课程
    private int type;// 投票类型
    private String time;// 截止时间
    private String name;//投票名称
    private String[] types={"单选","多选，最多2项","多选，最多3项","多选，最多4项","多选，无限制"};
    // 选择课程组件对应变量声明
    private List<Course> courses;
    private List<Courselist> courselists;
    private String[]c_names;
    private String c_name;
    ContextUtil app;
    private User user;
    private ArrayAdapter<String> c_adapter;
    private ArrayAdapter<String> c_adapter1;
    private boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vote);

        // 查看当前登陆用户
        app = (ContextUtil) getApplication();
        user = app.getUser();
        // 初始化界面
        initView();
        // 选择课程
        chooseCourse();
        // 选择投票类型
        chooseType();
        // 选择结束时间
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化对象
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                //调用show方法弹出对话框
                // 第一个参数为FragmentManager对象
                // 第二个为调用该方法的fragment的标签
                datePickerFragment.show(getFragmentManager(), "date_picker");
            }
        });

        // 发布投票
        bt_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String select = "";
                name = et_vote_name.getText().toString();
                if ((!"".equals(name))&&flag&&(!"".equals(type))){
                    for (EditText editText:selects){
                        select+=editText.getText().toString()+"&&-&&";
                    }
                    Vote vote = new Vote();
                    vote.setCourseName(c_name);
                    vote.setName(name);
                    vote.setType(type);
                    vote.setTime(time);
                    vote.setUserId(user.userId);
                    vote.setSelects(select);
                    // 保存当前投票
                    vote.save(AddVoteActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            finish();
                            Toast.makeText(AddVoteActivity.this,"发布成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AddVoteActivity.this,"发布失败",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(AddVoteActivity.this,"请输入必选项",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 新建选项
        iv_select_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i < 7) {
                    LayoutInflater inflater = LayoutInflater.from(AddVoteActivity.this);
                    final View contenView = inflater.inflate(R.layout.vote_select_item, null);
                    final EditText et_select = (EditText) contenView.findViewById(R.id.et_select);
                    selects.add(et_select);
                    i++;
                    et_select.setHint("选项" + (i + 1));
                    ImageView iv_remove = (ImageView) contenView.findViewById(R.id.iv_remove);
                    iv_remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout_check.removeView(contenView);
                            i--;
                            selects.remove(et_select);
                        }
                    });
                    layout_check.addView(contenView);
                }
            }
        });

    }

    /**
     * 选择投票类型
     */
    private void chooseType() {
        //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
        c_adapter1 = new ArrayAdapter<String>(AddVoteActivity.this, android.R.layout.simple_spinner_item, types);
        //为适配器设置下拉列表下拉时的菜单样式
        c_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将可选内容添加到下拉列表中
        sp_vote_type.setAdapter(c_adapter1);
        sp_vote_type.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //type得到了选中的值
                type = position;
                //设置显示当前选项的项
                parent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                parent.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 初始化界面
     */
    private void initView() {
        selects = new ArrayList<>();
        selects.add((EditText) findViewById(R.id.et_select_1));
        selects.add((EditText) findViewById(R.id.et_select_2));
        i = 1;
        sort_by_course = (Spinner) findViewById(R.id.sort_by_course);
        bt_publish = (Button) findViewById(R.id.bt_publish);
        et_vote_name = (EditText) findViewById(R.id.et_vote_name);
        layout_check = (LinearLayout) findViewById(R.id.layout_check);
        sp_vote_type = (Spinner) findViewById(R.id.sp_vote_type);
        tv_time = (TextView) findViewById(R.id.tv_time);
        iv_select_add = (ImageView) findViewById(R.id.iv_select_add);
    }

    /**
     * 选择对应的课程
     */
    private void chooseCourse() {
        // 登陆用户为教师
        if (1==user.type){
            // 班级信息获取
            BmobQuery<Course> query = new BmobQuery<>();
            query.addWhereEqualTo("teacherId",user.userId);
            query.findObjects(AddVoteActivity.this, new FindListener<Course>() {
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
                    }else {
                        Toast.makeText(AddVoteActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                        c_names = new String[0];
                    }
                    //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                    c_adapter = new ArrayAdapter<String>(AddVoteActivity.this, android.R.layout.simple_spinner_item, c_names);
                    //为适配器设置下拉列表下拉时的菜单样式
                    c_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //将可选内容添加到下拉列表中
                    sort_by_course.setAdapter(c_adapter);

                    sort_by_course.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            //c_name得到了选中的值
                            c_name = c_names[arg2];
                            //设置显示当前选项的项
                            arg0.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            arg0.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(AddVoteActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
        // 登陆用户为学生
        if (2==user.type){
            // 班级信息获取
            BmobQuery<Courselist> query = new BmobQuery<>();
            query.addWhereEqualTo("studentId",user.userId);
            query.findObjects(AddVoteActivity.this, new FindListener<Courselist>() {
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
                    }else {
                        Toast.makeText(AddVoteActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                        c_names = new String[0];
                    }
                    //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                    c_adapter = new ArrayAdapter<String>(AddVoteActivity.this, android.R.layout.simple_spinner_item, c_names);
                    //为适配器设置下拉列表下拉时的菜单样式
                    c_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    //将可选内容添加到下拉列表中
                    sort_by_course.setAdapter(c_adapter);

                    sort_by_course.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                            //c_name得到了选中的值
                            c_name = c_names[arg2];
                            //设置显示当前选项的项
                            arg0.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                            arg0.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(AddVoteActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //实现DataCallBack的getData方法
    @Override
    public void getData(String data) {
        //data即为fragment调用该函数传回的日期时间
        tv_time.setText(data);
        time = data;
        flag = true;
    }
}