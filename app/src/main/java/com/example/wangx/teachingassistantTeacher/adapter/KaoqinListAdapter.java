package com.example.wangx.teachingassistantTeacher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Code;
import com.example.wangx.teachingassistantTeacher.util.MyDate;

import java.util.List;

/**
 * 考勤结果adapter
 */

public class KaoqinListAdapter extends RecyclerView.Adapter<KaoqinListAdapter.MyViewHolder>{
    private List<Code> codes;
    private int i=1;
    public KaoqinListAdapter(List<Code> codes) {
        this.codes = codes;
    }
    /**
     * 列表项点击事件接口
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }

    private KaoqinListAdapter.OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(KaoqinListAdapter.OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    /**
     * 初始化adapter
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_kaoqin;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new KaoqinListAdapter.MyViewHolder(view);
    }


    /**
     * 绑定视图数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tv_code.setText(codes.get(position).getCode());
        holder.tv_time.setText(codes.get(position).getCreatedAt());
        String format = "yyyy-MM-dd HH:mm:ss";
        String time = codes.get(position).getCreatedAt();
        String currentTime = MyDate.getCurrentDate(format);
        // 根据时间判断点名活动是否正在进行
        if (time.substring(0,10).equals(currentTime.substring(0,10))){
            if ((Integer.parseInt(currentTime.substring(11,13))-Integer.parseInt(time.substring(11,13)))<=2){
                // 正在进行
                holder.tv_result.setText("正在进行");
                holder.tv_result.setTextColor(Color.parseColor("#12c700"));
            }else {
                holder.tv_result.setText("已结束");
                holder.tv_result.setTextColor(Color.RED);
            }
        }else {
            holder.tv_result.setText("已结束");
            holder.tv_result.setTextColor(Color.RED);
        }
        holder.tv_no.setText((i++)+"");

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

    /**
     * 内容数量
     * @return
     */
    @Override
    public int getItemCount() {
        if (null!=codes) {
            return codes.size();
        }else {
            return 0;
        }
    }

    /**
     * 建立界面联系
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_code;
        public TextView tv_time;
        public TextView tv_result;
        public TextView tv_no;
        public MyViewHolder(View itemView) {
            super(itemView);

            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_result = (TextView) itemView.findViewById(R.id.tv_result);
            tv_no = (TextView) itemView.findViewById(R.id.tv_no);
        }
    }
}
