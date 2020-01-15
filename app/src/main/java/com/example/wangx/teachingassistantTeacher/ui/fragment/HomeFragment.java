package com.example.wangx.teachingassistantTeacher.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Student;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.ui.LoginActivity;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
/**
 * 我界面
 */
public class HomeFragment extends Fragment {

    private User user;
    private Student student;

    private Button bt_home_logoff;
    private TextView tv_user_name;
    private ImageView im_myhome_image;
    private TextView tv_user_id;
    private TextView tv_user_type;
    private TextView tv_user_cla;
    private TextView tv_user_college;

    private LinearLayout layout_cla;
    private LinearLayout layout_college;
    ContextUtil app;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        tv_user_name = (TextView) homeView.findViewById(R.id.tv_user_name);
        im_myhome_image = (ImageView) homeView.findViewById(R.id.im_myhome_image);
        tv_user_id = (TextView) homeView.findViewById(R.id.tv_user_id);
        tv_user_type = (TextView) homeView.findViewById(R.id.tv_user_type);
        tv_user_college = (TextView) homeView.findViewById(R.id.tv_user_college);
        tv_user_cla = (TextView) homeView.findViewById(R.id.tv_user_cla);

        layout_college = (LinearLayout) homeView.findViewById(R.id.layout_college);
        layout_cla = (LinearLayout) homeView.findViewById(R.id.layout_cla);

        student = new Student();
        app = (ContextUtil) getActivity().getApplication();
        user = app.getUser();
        // 初始化Bmob云
        Bmob.initialize(getActivity(),"551cb1a09ea696a94f107d9c002b72f1");


        // 点击查看个人信息详情界面
        bt_home_logoff=(Button)homeView.findViewById(R.id.bt_home_logoff);

        // 注销功能
        bt_home_logoff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                user.setValue("isLogin",false);
                user.update(getActivity(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        getActivity().finish();
                        app.setUser(null);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getContext(),"注销失败"+s,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // 设置用户信息
        tv_user_name.setText(user.userName);
        tv_user_id.setText(user.userId);
        if (1==user.type){
            tv_user_type.setText("教师");
        }else if (2==user.type){
            tv_user_type.setText("学生");
            layout_college.setVisibility(View.VISIBLE);
            layout_cla.setVisibility(View.VISIBLE);
            BmobQuery<Student> query = new BmobQuery<>();
            query.addWhereEqualTo("studentId",user.userId);
            query.findObjects(getActivity(), new FindListener<Student>() {
                @Override
                public void onSuccess(List<Student> list) {
                    student = list.get(0);
                    tv_user_cla.setText(student.cla);
                    tv_user_college.setText(student.college);
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(getActivity(),"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
        Picasso.with(getActivity()).load(user.image).resize(60,60).centerCrop().into(im_myhome_image);
        return homeView;
    }
}
