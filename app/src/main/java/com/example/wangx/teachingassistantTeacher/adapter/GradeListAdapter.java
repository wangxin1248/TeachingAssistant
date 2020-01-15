package com.example.wangx.teachingassistantTeacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Grade;
import com.example.wangx.teachingassistantTeacher.bean.User;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 成绩结果adapter
 */

public class GradeListAdapter extends RecyclerView.Adapter<GradeListAdapter.MyViewHolder> {

    public String[] stu_names;
    private User user;
    //加一个全局的List对象存查出来的结果
    private List<Grade> grades;

    // 自定义一个构造函数传入List对象
    public GradeListAdapter(List<Grade> grades,User user,String[] stu_names) {
        this.grades = grades;
        this.user=user;
        this.stu_names=stu_names;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_grade;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new GradeListAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.mItem = grades.get(position);

        // 保留小数点后两位
        DecimalFormat format = new DecimalFormat("#.0");

//        给控件设值
        holder.tv_grade_id.setText(grades.get(position).stuId);
        holder.tv_grade_name.setText(stu_names[position]);
        holder.tv_grade_work.setText(format.format(grades.get(position).workgrade)+"");
        holder.tv_grade_test.setText(format.format(grades.get(position).testgrade)+"");
        holder.tv_grade_exam.setText(format.format(grades.get(position).examgrade)+"");
        holder.tv_grade_kaoqin.setText(format.format(grades.get(position).kaoqingrade)+"");
        holder.tv_grade_total.setText(format.format(grades.get(position).grade)+"");
        holder.tv_grade_rank.setText((position++)+"");

    }

    @Override
    public int getItemCount() {
        return grades.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public TextView tv_grade_id;
        public  TextView tv_grade_name;
        public  TextView tv_grade_work;
        public  TextView tv_grade_test;
        public  TextView tv_grade_exam;
        public  TextView tv_grade_kaoqin;
        public  TextView tv_grade_total;
        public  TextView tv_grade_rank;
        public Grade mItem;

        public MyViewHolder(View view) {
            super(view);
            mView = view;
            tv_grade_id = (TextView) view.findViewById(R.id.tv_grade_id);
            tv_grade_name = (TextView) view.findViewById(R.id.tv_grade_name);
            tv_grade_work = (TextView) view.findViewById(R.id.tv_grade_work);
            tv_grade_test = (TextView) view.findViewById(R.id.tv_grade_test);
            tv_grade_exam = (TextView) view.findViewById(R.id.tv_grade_exam);
            tv_grade_kaoqin = (TextView) view.findViewById(R.id.tv_grade_kaoqin);
            tv_grade_total = (TextView) view.findViewById(R.id.tv_grade_total);
            tv_grade_rank = (TextView) view.findViewById(R.id.tv_grade_rank);
        }

    }

}
