package com.example.wangx.teachingassistantTeacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Notice;
import com.example.wangx.teachingassistantTeacher.ui.ViewNoticeActivity;
import com.example.wangx.teachingassistantTeacher.ui.fragment.NoticeFragment.OnListFragmentInteractionListener;

import java.io.Serializable;
import java.util.List;

/**
 * 投票结果adapter
 */
public class MyNoticeRecyclerViewAdapter extends RecyclerView.Adapter<MyNoticeRecyclerViewAdapter.ViewHolder> {

    // 所需显示数据列表
    private List<Notice> notices;
    private Context context;
    private final OnListFragmentInteractionListener mListener;


    /**
     * 构造函数
     * @param notices
     * @param mListener
     * @param context
     */
    public MyNoticeRecyclerViewAdapter(List<Notice> notices, OnListFragmentInteractionListener mListener, Context context) {
        this.notices = notices;
        this.mListener = mListener;
        this.context = context;
    }

    /**
     * 创建视图
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notice, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 数据视图绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = notices.get(position);

        // 为视图设置相应的值
        holder.tc_notice_timeview.setText(notices.get(position).time);
        holder.tv_notice_name.setText(notices.get(position).title);
        if (notices.get(position).context.length()>25){
            holder.tv_notice_info.setText(notices.get(position).context.substring(0,22)+".......");
        }else {
            holder.tv_notice_info.setText(notices.get(position).context);
        }
        holder.tv_notice_teacher.setText("发布教师："+notices.get(position).t_name);
        holder.tv_notice_course.setText("发布班级："+notices.get(position).course_name);


        // 视图对象点击响应事件
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    Intent intent = new Intent(context, ViewNoticeActivity.class);
                    intent.putExtra("notices", (Serializable) notices);
                    intent.putExtra("position",position);
                    context.startActivity(intent);
                }
            }
        });
    }

    /**
     * 返回资源个数
     * @return
     */
    @Override
    public int getItemCount() {
        return notices.size();
    }

    // 更新公告
    public void replaceItem(List<Notice> notices) {
        this.notices = notices;
        notifyDataSetChanged();
    }

    /**
     * 界面对象类
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        // 界面中所具有的视图
        // 公告发布时间
        public  TextView tc_notice_timeview;
        // 公告名称
        public  TextView tv_notice_name;
        // 公告内容
        public TextView tv_notice_info;
        // 公告发布教师及时间
        public TextView tv_notice_teacher;
        // 发布课程
        public TextView tv_notice_course;

        public Notice mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            // 查找视图对应id
            tc_notice_timeview = (TextView) view.findViewById(R.id.tc_notice_timeview);
            tv_notice_name = (TextView) view.findViewById(R.id.tv_notice_name);
            tv_notice_info = (TextView) view.findViewById(R.id.tv_notice_info);
            tv_notice_teacher = (TextView) view.findViewById(R.id.tv_notice_teacher);
            tv_notice_course = (TextView) view.findViewById(R.id.tv_notice_course);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
