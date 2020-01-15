package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Select;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.bean.Vote;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
/**
 * 投票查看界面
 */
public class ViewVoteActivity extends AppCompatActivity {
    private LinearLayout layout_1;
    private LinearLayout layout_2;
    private LinearLayout layout_3;
    private LinearLayout layout_4;
    private LinearLayout layout_5;
    private LinearLayout layout_6;
    private LinearLayout layout_7;
    private LinearLayout layout_8;
    private CheckBox cb_1;
    private CheckBox cb_2;
    private CheckBox cb_3;
    private CheckBox cb_4;
    private CheckBox cb_5;
    private CheckBox cb_6;
    private CheckBox cb_7;
    private CheckBox cb_8;
    private TextView tv_name;
    private TextView tv_time_user;
    private TextView tv_type;
    private TextView tv_courseName;
    private Button bt_vote;
    private Button bt_show_result;
    private int type;
    private String[] types={"单选","多选，最多2项","多选，最多3项","多选，最多4项","多选，无限制"};
    private Vote vote;

    ContextUtil app;
    private User user;
    private int select_number = 0;
    private List<CheckBox> checkBoxes;
    private String[] selects;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vote);
        // 查看当前登陆用户
        app = (ContextUtil) getApplication();
        user = app.getUser();
        initView();

        vote = (Vote) getIntent().getSerializableExtra("vote");
        tv_name.setText(vote.getName());
        tv_type.setText("类型："+types[vote.getType()]);
        tv_courseName.setText("发布课程："+vote.getCourseName());
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",vote.getUserId());
        query.findObjects(ViewVoteActivity.this, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list.size()>0) {
                    tv_time_user.setText(vote.getCreatedAt()+" 由"+list.get(0).getUserName()+"创建");
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(ViewVoteActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });
        // 根据用户不同执行不同的界面
        if (user.userId.equals(vote.getUserId())){
            bt_vote.setEnabled(false);
            bt_show_result.setEnabled(true);
        }else {
            BmobQuery<Select> query1 = new BmobQuery<>();
            query1.addWhereEqualTo("userId",user.userId);
            query1.addWhereEqualTo("id",vote.getObjectId());
            query1.findObjects(ViewVoteActivity.this, new FindListener<Select>() {
                @Override
                public void onSuccess(List<Select> list) {
                    // 已经投过票
                    if (list.size()>0){
                        bt_vote.setEnabled(false);
                        bt_show_result.setEnabled(true);
                    }else {
                        bt_vote.setEnabled(true);
                        bt_show_result.setEnabled(false);
                    }
                }

                @Override
                public void onError(int i, String s) {

                }
            });
        }

        // 显示选项
        showSelects();

        // 判断用户选择选项的数量
        switch (vote.getType()){
            case 0:
                select_number = 1;
                break;
            case 1:
                select_number = 2;
                break;
            case 2:
                select_number = 3;
                break;
            case 3:
                select_number = 4;
                break;
            case 4:
                select_number = selects.length;
                break;
            default:
                break;
        }
        setListener();

        // 投票按钮
        bt_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Select select = new Select();
                select.setId(vote.getObjectId());
                select.setUserId(user.getUserId());
                String select_result = "";
                for (CheckBox cb:checkBoxes){
                    select_result+=cb.getText().subSequence(0,1);
                }
                select.setSelect(select_result);
                select.save(ViewVoteActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(ViewVoteActivity.this,"投票成功",Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(ViewVoteActivity.this,VoteResultActivity.class);
                        intent.putExtra("vote",vote);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(ViewVoteActivity.this,"投票失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // 投票结果按钮
        bt_show_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewVoteActivity.this,VoteResultActivity.class);
                intent.putExtra("vote",vote);
                startActivity(intent);
            }
        });
    }

    /**
     * 给checkbox设置事件相应器
     */
    private void setListener() {
        checkBoxes = new ArrayList<>();
        cb_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkBoxes.size()>=select_number){
                        cb_1.setChecked(false);
                        Toast.makeText(ViewVoteActivity.this,"无法继续选择！",Toast.LENGTH_SHORT).show();
                    }else {
                        checkBoxes.add(cb_1);
                    }
                }else {
                    checkBoxes.remove(cb_1);
                }
            }
        });

        cb_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkBoxes.size()>=select_number){
                        cb_2.setChecked(false);
                        Toast.makeText(ViewVoteActivity.this,"无法继续选择！",Toast.LENGTH_SHORT).show();
                    }else {
                        checkBoxes.add(cb_2);
                    }
                }else {
                    checkBoxes.remove(cb_2);
                }
            }
        });

        cb_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkBoxes.size()>=select_number){
                        cb_3.setChecked(false);
                        Toast.makeText(ViewVoteActivity.this,"无法继续选择！",Toast.LENGTH_SHORT).show();
                    }else {
                        checkBoxes.add(cb_3);
                    }
                }else {
                    checkBoxes.remove(cb_3);
                }
            }
        });

        cb_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkBoxes.size()>=select_number){
                        cb_4.setChecked(false);
                        Toast.makeText(ViewVoteActivity.this,"无法继续选择！",Toast.LENGTH_SHORT).show();
                    }else {
                        checkBoxes.add(cb_4);
                    }
                }else {
                    checkBoxes.remove(cb_4);
                }
            }
        });

        cb_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkBoxes.size()>=select_number){
                        cb_5.setChecked(false);
                        Toast.makeText(ViewVoteActivity.this,"无法继续选择！",Toast.LENGTH_SHORT).show();
                    }else {
                        checkBoxes.add(cb_5);
                    }
                }else {
                    checkBoxes.remove(cb_5);
                }
            }
        });

        cb_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkBoxes.size()>=select_number){
                        cb_6.setChecked(false);
                        Toast.makeText(ViewVoteActivity.this,"无法继续选择！",Toast.LENGTH_SHORT).show();
                    }else {
                        checkBoxes.add(cb_6);
                    }
                }else {
                    checkBoxes.remove(cb_6);
                }
            }
        });

        cb_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkBoxes.size()>=select_number){
                        cb_7.setChecked(false);
                        Toast.makeText(ViewVoteActivity.this,"无法继续选择！",Toast.LENGTH_SHORT).show();
                    }else {
                        checkBoxes.add(cb_7);
                    }
                }else {
                    checkBoxes.remove(cb_7);
                }
            }
        });

        cb_8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (checkBoxes.size()>=select_number){
                        cb_8.setChecked(false);
                        Toast.makeText(ViewVoteActivity.this,"无法继续选择！",Toast.LENGTH_SHORT).show();
                    }else {
                        checkBoxes.add(cb_8);
                    }
                }else {
                    checkBoxes.remove(cb_8);
                }
            }
        });
    }

    /**
     * 显示选项
     */
    private void showSelects() {
        selects = vote.getSelects().split("&&-&&");
        for (int i=0;i<selects.length;i++){
            switch (i){
                case 0:
                    cb_1.setText("A."+selects[i]);
                    break;
                case 1:
                    cb_2.setText("B."+selects[i]);
                    break;
                case 2:
                    layout_3.setVisibility(View.VISIBLE);
                    cb_3.setText("C."+selects[i]);
                    break;
                case 3:
                    layout_4.setVisibility(View.VISIBLE);
                    cb_4.setText("D."+selects[i]);
                    break;
                case 4:
                    layout_5.setVisibility(View.VISIBLE);
                    cb_5.setText("E."+selects[i]);
                    break;
                case 5:
                    layout_6.setVisibility(View.VISIBLE);
                    cb_6.setText("F."+selects[i]);
                    break;
                case 6:
                    layout_7.setVisibility(View.VISIBLE);
                    cb_7.setText("G."+selects[i]);
                    break;
                case 7:
                    layout_8.setVisibility(View.VISIBLE);
                    cb_8.setText("H."+selects[i]);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 绑定界面试图
     */
    private void initView() {
        layout_1 = (LinearLayout) findViewById(R.id.layout_1);
        layout_2 = (LinearLayout) findViewById(R.id.layout_2);
        layout_3 = (LinearLayout) findViewById(R.id.layout_3);
        layout_4 = (LinearLayout) findViewById(R.id.layout_4);
        layout_5 = (LinearLayout) findViewById(R.id.layout_5);
        layout_6 = (LinearLayout) findViewById(R.id.layout_6);
        layout_7 = (LinearLayout) findViewById(R.id.layout_7);
        layout_8 = (LinearLayout) findViewById(R.id.layout_8);
        cb_1 = (CheckBox) findViewById(R.id.cb_1);
        cb_2 = (CheckBox) findViewById(R.id.cb_2);
        cb_3 = (CheckBox) findViewById(R.id.cb_3);
        cb_4 = (CheckBox) findViewById(R.id.cb_4);
        cb_5 = (CheckBox) findViewById(R.id.cb_5);
        cb_6 = (CheckBox) findViewById(R.id.cb_6);
        cb_7 = (CheckBox) findViewById(R.id.cb_7);
        cb_8 = (CheckBox) findViewById(R.id.cb_8);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time_user = (TextView) findViewById(R.id.tv_time_user);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_courseName = (TextView) findViewById(R.id.tv_courseName);
        bt_vote = (Button) findViewById(R.id.bt_vote);
        bt_show_result = (Button) findViewById(R.id.bt_show_result);

        bt_vote.setEnabled(false);
        bt_show_result.setEnabled(false);
    }
}
