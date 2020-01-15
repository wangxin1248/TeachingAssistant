package com.example.wangx.teachingassistantTeacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Resource;
import com.example.wangx.teachingassistantTeacher.ui.ViewDocActivity;
import com.example.wangx.teachingassistantTeacher.ui.ViewResourceActivity;
import com.example.wangx.teachingassistantTeacher.ui.ViewVideoActivity;
import com.example.wangx.teachingassistantTeacher.ui.fragment.ResourceFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

/**
 * 资源adapter
 */
public class MyResourceRecyclerViewAdapter extends RecyclerView.Adapter<MyResourceRecyclerViewAdapter.ViewHolder> {

    private static final String TAG ="bbbbbbbbbbb" ;
    // 数据项填充内容
    private  List<Resource> resources;
    private final OnListFragmentInteractionListener mListener;

    private Context context;


    /**
     * 构造函数
     * @param items
     * @param listener
     * @param context
     */
    public MyResourceRecyclerViewAdapter(List<Resource> items, OnListFragmentInteractionListener listener, Context context) {
        resources = items;
        mListener = listener;
        this.context = context;
    }



    /**
     * 初始化ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_resource, parent, false);
        return new ViewHolder(view);
    }


    /**
     * 绑定ViewHolder，为其设置对应数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = resources.get(position);

        // 设置标题
        holder.tv_resource_title.setText(resources.get(position).title);
        // 设置时间
        holder.tv_resource_time.setText(resources.get(position).getCreatedAt());
        // 设置发布教师
        holder.tv_resource_teacher.setText(resources.get(position).t_name);
        // 设置发布班级
        holder.tv_resource_class.setText(resources.get(position).courseName);
        // 设置资源类型
        holder.tv_resource_type.setText("类型:"+resources.get(position).resource_type);

        // 如果资源是图片
        if ("图片".equals(resources.get(position).resource_type)){
            // 加载云端图片
            Picasso.with(context).load(resources.get(position).path).resize(180, 90).centerCrop().into(holder.iv_resource_img);
        }
        // 如果资源是视频
        else if ("视频".equals(resources.get(position).resource_type)){
            // 加载视频缩略图
            Picasso.with(context).load(resources.get(position).url).resize(180, 90).centerCrop().into(holder.iv_resource_img);
        }
        // 如果资源是文档
        else {
            // 加载文档默认图片
            Picasso.with(context).load(resources.get(position).url).resize(180, 90).centerCrop().into(holder.iv_resource_img);
        }
        // 列表项点击事件处理
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    // 如果资源是图片
                    if ("图片".equals(resources.get(position).resource_type)){
                        Intent intent = new Intent(context, ViewResourceActivity.class);
                        intent.putExtra("resources", (Serializable) resources);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                    }
                    // 如果资源是视频
                    else if ("视频".equals(resources.get(position).resource_type)){
                        Intent intent = new Intent(context, ViewVideoActivity.class);
                        intent.putExtra("resources", (Serializable) resources);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                    }
                    // 如果资源是文档
                    else {
                        Intent intent = new Intent(context, ViewDocActivity.class);
                        intent.putExtra("resources", (Serializable) resources);
                        intent.putExtra("position",position);
                        context.startActivity(intent);
                    }
                }
            }
        });
    }
    /**
     * 返回数据项数量
     * @return
     */
    @Override
    public int getItemCount() {
        return resources.size();
    }

    // 更新数据
    public void replaceItem(List<Resource> resources) {
        this.resources = resources;
        notifyDataSetChanged();
    }

    /**
     * 视图内容填充
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        // 界面对应控件
        public ImageView iv_resource_img;
        public TextView tv_resource_time;
        public TextView tv_resource_title;
        public TextView tv_resource_teacher;
        public TextView tv_resource_type;
        public TextView tv_resource_class;

        public Resource mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            // 查找界面对应控件
            iv_resource_img = (ImageView) view.findViewById(R.id.iv_resource_img);
            tv_resource_time = (TextView) view.findViewById(R.id.tv_resource_time);
            tv_resource_title = (TextView) view.findViewById(R.id.tv_resource_title);
            tv_resource_teacher = (TextView) view.findViewById(R.id.tv_resource_teacher);
            tv_resource_class = (TextView) view.findViewById(R.id.tv_resource_class);
            tv_resource_type = (TextView) view.findViewById(R.id.tv_resource_type);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
