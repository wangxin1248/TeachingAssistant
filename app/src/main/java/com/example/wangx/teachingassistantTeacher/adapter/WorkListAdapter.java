package com.example.wangx.teachingassistantTeacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.bean.Work;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 笔记数据adapter
 */
public class WorkListAdapter extends RecyclerView.Adapter<WorkListAdapter.MyViewHolder> {

    private List<Work> works;
    private OnItemClickLitener mOnItemClickLitener;
    private Context context;

    public WorkListAdapter(List<Work> works,Context context) {
        this.context = context;
        this.works = works;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    /**
     * 创建数据处理器
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public WorkListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.activity_list_work;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new MyViewHolder(view);
    }

    /**
     * 列表项数据绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final WorkListAdapter.MyViewHolder holder, final int position) {

        // 在这里对界面中的控件赋值
        holder.mItem=works.get(position);
        holder.tv_activity_title.setText(works.get(position).getTitle());
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("userId",works.get(position).getAuthor());
        query.findObjects(context, new FindListener<User>() {
            @Override
            public void onSuccess(List<User> list) {
                if (list.size()>0) {
                    holder.tv_activity_author.setText("作者：" + list.get(0).userName);
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(context,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });
        holder.tv_activity_time.setText("发布时间："+works.get(position).getCreatedAt());
        holder.tv_activity_courseName.setText("发布课程："+works.get(position).getCourseName());
        if (works.get(position).content.length() > 25) {
            holder.tv_activity_content.setText(works.get(position).content.substring(0,22)+"......");
        }else{
            holder.tv_activity_content.setText(works.get(position).getContent());
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    /**
     * 返回列表项个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (null==works){
            return 0;
        }else {
            return works.size();
        }
    }

    // 更新笔记
    public void replaceItem(List<Work> works) {
        this.works = works;
        notifyDataSetChanged();
    }

    /**
     * 列表项点击事件接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    /**
     * 数据处理器
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_activity_title;
        public TextView tv_activity_time;
        public TextView tv_activity_author;
        public TextView tv_activity_courseName;
        public TextView tv_activity_content;
        public Work mItem;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_activity_title = (TextView) itemView.findViewById(R.id.tv_activity_title);
            tv_activity_time = (TextView) itemView.findViewById(R.id.tv_activity_time);
            tv_activity_author = (TextView) itemView.findViewById(R.id.tv_activity_author);
            tv_activity_courseName = (TextView) itemView.findViewById(R.id.tv_activity_courseName);
            tv_activity_content = (TextView) itemView.findViewById(R.id.tv_activity_content);

        }
    }
}
