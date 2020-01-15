package com.example.wangx.teachingassistantTeacher.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.ui.CheckActivity;
import com.example.wangx.teachingassistantTeacher.ui.CheckCourseActivity;
import com.example.wangx.teachingassistantTeacher.ui.CheckGradeCourseActivity;
import com.example.wangx.teachingassistantTeacher.ui.ChooseCourseActivity;
import com.example.wangx.teachingassistantTeacher.ui.GradeActivity;
import com.example.wangx.teachingassistantTeacher.ui.KaoqinActivity;
import com.example.wangx.teachingassistantTeacher.ui.QuestionActivity;
import com.example.wangx.teachingassistantTeacher.ui.ViewCourseActivity;
import com.example.wangx.teachingassistantTeacher.ui.WorkActivity;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
/**
 * 活动界面
 */
public class ActivityFragment extends Fragment implements View.OnClickListener {

    private ImageButton ib_activity_work; // 作业接口
    private ImageButton ib_activity_grade; // 成绩接口
    private ImageButton ib_activity_talk; // 讨论接口
    private ImageButton ib_activity_question; // 问卷投票接口
    private ImageButton ib_activity_kaoqin; // 考勤接口
    private ImageView ib_activity_cource;//课程接口
    private TextView tv_activity_cource;// 课程文字
    private TextView tv_activity_kaoqin;// 课程文字
    private LinearLayout layout_activity_kaoqin;
    private Intent intent;
    private User user;
    ContextUtil app;

    /**
     * 构造函数
     */
    public ActivityFragment() {
        // Required empty public constructor
    }

    /**
     * 初始化方法
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    /**
     * 初始化试图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View activityView = inflater.inflate(R.layout.fragment_activity, container, false);
        app = (ContextUtil) getActivity().getApplication();
        user = app.getUser();
        ib_activity_work = (ImageButton) activityView.findViewById(R.id.ib_activity_work);
        ib_activity_grade = (ImageButton) activityView.findViewById(R.id.ib_activity_grade);
        ib_activity_talk = (ImageButton) activityView.findViewById(R.id.ib_activity_talk);
        ib_activity_question = (ImageButton) activityView.findViewById(R.id.ib_activity_question);
        ib_activity_kaoqin = (ImageButton) activityView.findViewById(R.id.ib_activity_kaoqin);
        ib_activity_cource = (ImageView) activityView.findViewById(R.id.ib_activity_cource);
        layout_activity_kaoqin = (LinearLayout) activityView.findViewById(R.id.layout_activity_kaoqin);
        tv_activity_cource = (TextView) activityView.findViewById(R.id.tv_activity_cource);
        tv_activity_kaoqin = (TextView) activityView.findViewById(R.id.tv_activity_kaoqin);
        // 向老师用户暴露考勤接口和显示课程入口
        if (1==user.type){
            tv_activity_cource.setText("课程");
        }
        if (2==user.type){
            tv_activity_kaoqin.setText("点名");
            tv_activity_cource.setText("选课");
        }
        // 作业点击事件跳转
        ib_activity_work.setOnClickListener(this);
        ib_activity_grade.setOnClickListener(this);
        ib_activity_talk.setOnClickListener(this);
        ib_activity_question.setOnClickListener(this);
        ib_activity_kaoqin.setOnClickListener(this);
        ib_activity_cource.setOnClickListener(this);
        return activityView;
    }

    /**
     * 按钮点击事件实现
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // 作业界面跳转
            case R.id.ib_activity_work:
                intent = new Intent(getActivity(),WorkActivity.class);
                startActivity(intent);
                break;
            // 成绩界面跳转
            case R.id.ib_activity_grade:
                if(1==user.type){
                    Intent intent = new Intent(getActivity(), GradeActivity.class);
                    startActivity(intent);
                }
                if (2==user.type){
                    Intent intent1=new Intent(getActivity(),CheckGradeCourseActivity.class);
                    startActivity(intent1);
                }
                break;
            // 讨论界面跳转
            case R.id.ib_activity_talk:
                intent = new Intent(getActivity(),ChooseCourseActivity.class);
                startActivity(intent);
                break;
            // 问卷调查界面跳转
            case R.id.ib_activity_question:
                intent = new Intent(getActivity(),QuestionActivity.class);
                startActivity(intent);
                break;
            // 考勤界面跳转
            case R.id.ib_activity_kaoqin:
                // 教师进入考勤界面
                if (1==user.type){
                    intent = new Intent(getActivity(),KaoqinActivity.class);
                    startActivity(intent);
                }
                // 学生进入点名界面
                if (2==user.type){
                    intent = new Intent(getActivity(),CheckActivity.class);
                    startActivity(intent);
                }
                break;
            // 选课界面跳转
            case R.id.ib_activity_cource:
                // 教师进入课程界面
                if (1==user.type){
                    intent = new Intent(getActivity(),ViewCourseActivity.class);
                    startActivity(intent);
                }
                // 学生进入选课界面
                if (2==user.type){
                    intent = new Intent(getActivity(),CheckCourseActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
