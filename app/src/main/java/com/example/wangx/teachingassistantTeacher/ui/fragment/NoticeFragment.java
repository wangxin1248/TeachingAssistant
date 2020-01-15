package com.example.wangx.teachingassistantTeacher.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.adapter.MyNoticeRecyclerViewAdapter;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.Notice;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.ui.AddNoticeActivity;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 公告界面
 */
public class NoticeFragment extends Fragment {

    private User user;
    ContextUtil app;
    private List<Courselist> courselists;

    private List<Notice> notices;
    private FloatingActionButton notice_fab;
    private TextView tv_tip;

    private MyNoticeRecyclerViewAdapter adapter;
    // 下拉刷新控件
    private SwipeRefreshLayout swipeRefreshLayout;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoticeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NoticeFragment newInstance(int columnCount) {
        NoticeFragment fragment = new NoticeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notice_list, container, false);
        // 初始化Bmob云
        Bmob.initialize(getActivity(),"551cb1a09ea696a94f107d9c002b72f1");

        tv_tip = (TextView) view.findViewById(R.id.tv_tip);

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.linearlayout_swipe_refresh) ;
        //调整SwipeRefreshLayout的位置
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        setListener();//设置监听事件
        app = (ContextUtil) getActivity().getApplication();
        user = app.getUser();
        // 登陆用户不同，显示内容不同
        if (1==user.type){
            BmobQuery<Notice> query = new BmobQuery<>();
            query.addWhereEqualTo("t_name",user.userName);
            query.findObjects(getActivity(), new FindListener<Notice>() {
                @Override
                public void onSuccess(List<Notice> list) {
                    notices = list;
                    // Set the adapter
                    if (notices!=null) {
                        // 逆序
                        Collections.reverse(notices);
                        // 不显示提示信息
                        tv_tip.setVisibility(View.INVISIBLE);

                        Context context = view.getContext();
                        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notice_list);
                        if (mColumnCount <= 1) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        } else {
                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                        }
                        recyclerView.setAdapter(adapter = new MyNoticeRecyclerViewAdapter(notices, mListener,context));
                    }else {
                        tv_tip.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(getActivity(),"Error"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (2==user.type){
            // 查询课程信息
            BmobQuery<Courselist> query = new BmobQuery<>();
            query.addWhereEqualTo("studentId",user.userId);
            query.findObjects(getActivity(), new FindListener<Courselist>() {
                @Override
                public void onSuccess(List<Courselist> list) {
                    courselists = list;
                    notices = new ArrayList<>();
                    if (null!=courselists){
                        String[] b = new String[courselists.size()];
                        for (int i=0;i<courselists.size();i++){
                            b[i] = courselists.get(i).courseName;
                        }
                        BmobQuery<Notice> query1 = new BmobQuery<>();
                        query1.addWhereContainedIn("course_name", Arrays.asList(b));
                        query1.findObjects(getActivity(), new FindListener<Notice>() {
                            @Override
                            public void onSuccess(List<Notice> list) {
                                notices = list;
                                // Set the adapter
                                if (notices!=null) {
                                    // 逆序
                                    Collections.reverse(notices);
                                    // 不显示提示信息
                                    tv_tip.setVisibility(View.INVISIBLE);

                                    Context context = view.getContext();
                                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notice_list);
                                    if (mColumnCount <= 1) {
                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    } else {
                                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                                    }
                                    recyclerView.setAdapter(adapter = new MyNoticeRecyclerViewAdapter(notices, mListener,context));
                                }else {
                                    tv_tip.setVisibility(View.VISIBLE);
                                }
                            }
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(getActivity(),"Error:"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        Toast.makeText(getActivity(),"选课表为空",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onError(int i, String s) {
                    Toast.makeText(getActivity(),"Error:"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }

        tv_tip = (TextView) view.findViewById(R.id.tv_tip);
        notice_fab = (FloatingActionButton) view.findViewById(R.id.notice_fab);
        // 教师可以显示添加按钮并操作，学生不行
        if (1==user.type){
            // 按钮设置为可见
            notice_fab.setVisibility(View.VISIBLE);
            // 点击事件
            notice_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), AddNoticeActivity.class));
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Notice item);
    }

    /**
     * 设置监听事件
     */
    private void setListener(){
        //swipeRefreshLayout刷新监听
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            // 刷新操作响应
            @Override
            public void onRefresh() {
                // 登陆用户不同，显示内容不同
                if (1==user.type){
                    BmobQuery<Notice> query = new BmobQuery<>();
                    query.addWhereEqualTo("t_name",user.userName);
                    query.findObjects(getActivity(), new FindListener<Notice>() {
                        @Override
                        public void onSuccess(List<Notice> list) {
                            notices = list;
                            Collections.reverse(notices);
                            adapter.replaceItem(notices);
                            // 不显示刷新控件
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(getActivity(),"Error:"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (2==user.type){
                    // 查询课程信息
                    BmobQuery<Courselist> query = new BmobQuery<>();
                    query.addWhereEqualTo("studentId",user.userId);
                    query.findObjects(getActivity(), new FindListener<Courselist>() {
                        @Override
                        public void onSuccess(List<Courselist> list) {
                            courselists = list;
                            notices = new ArrayList<>();
                            if (null!=courselists){
                                String[] b = new String[courselists.size()];
                                for (int i=0;i<courselists.size();i++){
                                    b[i] = courselists.get(i).courseName;
                                }
                                BmobQuery<Notice> query1 = new BmobQuery<>();
                                query1.addWhereContainedIn("course_name", Arrays.asList(b));
                                query1.findObjects(getActivity(), new FindListener<Notice>() {
                                    @Override
                                    public void onSuccess(List<Notice> list) {
                                        notices = list;
                                        Collections.reverse(notices);
                                        adapter.replaceItem(notices);
                                        // 不显示刷新控件
                                        swipeRefreshLayout.setRefreshing(false);
                                        Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onError(int i, String s) {
                                        Toast.makeText(getActivity(),"Error:"+s,Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Toast.makeText(getActivity(),"选课表为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(getActivity(),"Error:"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
