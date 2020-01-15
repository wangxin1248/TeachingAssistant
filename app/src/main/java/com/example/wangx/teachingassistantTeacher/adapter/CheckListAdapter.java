package com.example.wangx.teachingassistantTeacher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Check;

import java.util.List;

/**
 * 点名结果adapter
 */

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder>{
    private List<Check> checks;
    private String[] names;
    private int i=1;

    public CheckListAdapter(List<Check> checks, String[] names) {
        this.checks = checks;
        this.names = names;
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
        int layoutIdForListItem = R.layout.list_check;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new CheckListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv_no.setText((i++)+"");
        holder.tv_id.setText(checks.get(position).getStudentId());
        holder.tv_name.setText(names[position]);
        holder.tv_time.setText(checks.get(position).getCreatedAt());
        holder.tv_place.setText(checks.get(position).getPlace());
    }

    @Override
    public int getItemCount() {
        return checks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_no;
        private TextView tv_name;
        private TextView tv_id;
        private TextView tv_time;
        private TextView tv_place;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_no = (TextView) itemView.findViewById(R.id.tv_no);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_place = (TextView) itemView.findViewById(R.id.tv_place);
        }
    }
}
