package com.example.wangx.teachingassistantTeacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.bean.Vote;
import com.example.wangx.teachingassistantTeacher.util.MyDate;

import java.util.List;

/**
 * 投票项显示
 */
public class VoteListAdapter extends RecyclerView.Adapter<VoteListAdapter.MyViewHolder>{
    public VoteListAdapter(List<Vote> votes,User user) {
        this.votes = votes;
        this.user = user;
    }

    private List<Vote> votes;
    private User user;

    // 更新公告
    public void replaceItem(List<Vote> votes) {
        this.votes = votes;
        notifyDataSetChanged();
    }
    /**
     * 列表项点击事件接口
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private VoteListAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(VoteListAdapter.OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public VoteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_vote;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new VoteListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VoteListAdapter.MyViewHolder holder, int position) {
        holder.tv_name.setText(votes.get(position).getName());
        holder.tv_time.setText(votes.get(position).getCreatedAt());
        // 判断是否是当前用户发起
        if (!user.userId.equals(votes.get(position).getUserId())){
            holder.tv_is_self.setVisibility(View.INVISIBLE);
        }
        holder.tv_courseName.setText("发布课程："+votes.get(position).getCourseName());
        // 结束时间
        String time = votes.get(position).getTime();
        // 当前时间
        String format = "yyyy-MM-dd HH:mm:ss";
        String currentTime = MyDate.getCurrentDate(format);

        // 结束时间切分
        String[] timess = time.split(" ");
        String[] times = timess[0].split("-");
        String[] timesss = timess[1].split(":");

        // 年份判断
        int year = Integer.parseInt(times[0]);
        int cyear = Integer.parseInt(currentTime.substring(0,4));

        // 月份判断
        int month = Integer.parseInt(times[1]);
        int cmonth = Integer.parseInt(currentTime.substring(5,7));

        // 天判断
        int day = Integer.parseInt(times[2]);
        int cday = Integer.parseInt(currentTime.substring(8,10));

        // 小时判断
        int hour = Integer.parseInt(timesss[0]);
        int chour = Integer.parseInt(currentTime.substring(11,13));

        // 判断时间是否结束
        if (cyear<year){
            holder.tv_state.setText("正在进行");
        }else if (cyear==year&&cmonth<month){
            holder.tv_state.setText("正在进行");
        }else if (cyear==year&&cmonth==month&&cday<day){
            holder.tv_state.setText("正在进行");
        }else if (cyear==year&&cmonth==month&&cday==day&&chour<=hour){
            holder.tv_state.setText("正在进行");
        } else {
            holder.tv_state.setText("已结束");
        }
        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return votes.size();
    }

    /**
     * 初始化界面视图
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_state;
        private TextView tv_is_self;
        private TextView tv_courseName;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_state = (TextView) itemView.findViewById(R.id.tv_state);
            tv_is_self = (TextView) itemView.findViewById(R.id.tv_is_self);
            tv_courseName = (TextView) itemView.findViewById(R.id.tv_courseName);
        }
    }
}
