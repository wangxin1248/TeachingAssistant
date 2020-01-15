package com.example.wangx.teachingassistantTeacher.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.wangx.teachingassistantTeacher.adapter.MyResourceRecyclerViewAdapter;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.Resource;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.ui.AddDocActivity;
import com.example.wangx.teachingassistantTeacher.ui.AddImgActivity;
import com.example.wangx.teachingassistantTeacher.ui.AddVideoActivity;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 资源界面
 */
public class ResourceFragment extends Fragment {

    private User user;

    private Resource resource;
    private List<Resource> resources;
    private List<Courselist> courselists;

    private Context context = getContext();

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tv_tip;
    private FloatingActionsMenu resource_fab;
    private com.getbase.floatingactionbutton.FloatingActionButton resource_fab_add_img;
    private com.getbase.floatingactionbutton.FloatingActionButton resource_fab_add_video;
    private com.getbase.floatingactionbutton.FloatingActionButton resource_fab_add_doc;

    private MyResourceRecyclerViewAdapter adapter;
    ContextUtil app;


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ResourceFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ResourceFragment newInstance(int columnCount) {
        ResourceFragment fragment = new ResourceFragment();
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
        final View view = inflater.inflate(R.layout.fragment_resource_list, container, false);

        tv_tip = (TextView) view.findViewById(R.id.tv_tip);

        swipeRefreshLayout=(SwipeRefreshLayout) view.findViewById(R.id.linearlayout_swipe_refresh) ;
        //调整SwipeRefreshLayout的位置
        swipeRefreshLayout.setProgressViewOffset(false, 0,  (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
        setListener();//设置监听事件
        app = (ContextUtil) getActivity().getApplication();
        user = app.getUser();

        resource_fab = (FloatingActionsMenu) view.findViewById(R.id.resource_fab);
        // 教师可以显示添加按钮并操作，学生不行
        if (1==user.type){
            // 按钮设置为可见
            resource_fab.setVisibility(View.VISIBLE);
            BmobQuery<Resource> query = new BmobQuery<>();
            query.addWhereEqualTo("t_name",user.userName);
            query.findObjects(getActivity(), new FindListener<Resource>() {
                @Override
                public void onSuccess(List<Resource> list) {
                    resources = list;
                    // Set the adapter
                    if (resources!=null) {
                        // 逆序
                        Collections.reverse(resources);
                        // 不显示提示信息
                        tv_tip.setVisibility(View.INVISIBLE);

                        Context context = view.getContext();
                        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.resource_list);
                        if (mColumnCount <= 1) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        } else {
                            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                        }
                        // 设置内容填充
                        recyclerView.setAdapter(adapter = new MyResourceRecyclerViewAdapter(resources, mListener,context));
                    }else {
                        // 显示提示信息
                        tv_tip.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onError(int i, String s) {
                    Toast.makeText(getActivity(),"Error"+s,Toast.LENGTH_SHORT).show();
                }
            });
        }
        // 学生则查询学生所选课程所对应的资源
        if (2==user.type){
            BmobQuery<Courselist> query = new BmobQuery<>();
            query.addWhereEqualTo("studentId",user.userId);
            query.findObjects(getActivity(), new FindListener<Courselist>() {
                @Override
                public void onSuccess(List<Courselist> list) {
                    courselists = list;
                    resources = new ArrayList<>();
                    if (null!=courselists){
                        String[] b = new String[courselists.size()];
                        for (int i=0;i<courselists.size();i++){
                            b[i] = courselists.get(i).courseName;
                        }
                        BmobQuery<Resource> query1 = new BmobQuery<>();
                        query1.addWhereContainedIn("courseName", Arrays.asList(b));
                        query1.findObjects(getActivity(), new FindListener<Resource>() {
                            @Override
                            public void onSuccess(List<Resource> list) {
                                resources = list;
                                // Set the adapter
                                if (resources!=null) {
                                    // 逆序
                                    Collections.reverse(resources);
                                    // 不显示提示信息
                                    tv_tip.setVisibility(View.INVISIBLE);

                                    Context context = view.getContext();
                                    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.resource_list);
                                    if (mColumnCount <= 1) {
                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    } else {
                                        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                                    }
                                    // 设置内容填充
                                    recyclerView.setAdapter(adapter = new MyResourceRecyclerViewAdapter(resources, mListener,context));
                                }else {
                                    // 显示提示信息
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
        // 上传图片
        resource_fab_add_img = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.resource_fab_add_img);
        resource_fab_add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), AddImgActivity.class));
            }
        });
        // 上传视频
        resource_fab_add_video = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.resource_fab_add_video);
        resource_fab_add_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), AddVideoActivity.class));
            }
        });
        // 上传文档
        resource_fab_add_doc = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.resource_fab_add_doc);
        resource_fab_add_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), AddDocActivity.class));
            }
        });
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
        void onListFragmentInteraction(Resource item);
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
                // 查询数据库数据
//                String a[] = {"0"};
//                resources = resourceDAO.query("id > ?",a);

                // 教师可以显示添加按钮并操作，学生不行
                if (1==user.type){
                    // 按钮设置为可见
                    resource_fab.setVisibility(View.VISIBLE);
                    BmobQuery<Resource> query = new BmobQuery<>();
                    query.addWhereEqualTo("t_name",user.userName);
                    query.findObjects(getActivity(), new FindListener<Resource>() {
                        @Override
                        public void onSuccess(List<Resource> list) {
                            resources = list;
                            // 逆序
                            Collections.reverse(resources);
                            adapter.replaceItem(resources);
                            // 不显示刷新控件
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(getActivity(),"Error"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                // 学生则查询学生所选课程所对应的资源
                if (2==user.type){
                    BmobQuery<Courselist> query = new BmobQuery<>();
                    query.addWhereEqualTo("studentId",user.userId);
                    query.findObjects(getActivity(), new FindListener<Courselist>() {
                        @Override
                        public void onSuccess(List<Courselist> list) {
                            courselists = list;
                            resources = new ArrayList<>();
                            if (null!=courselists){
                                String[] b = new String[courselists.size()];
                                for (int i=0;i<courselists.size();i++){
                                    b[i] = courselists.get(i).courseName;
                                }
                                BmobQuery<Resource> query1 = new BmobQuery<>();
                                query1.addWhereContainedIn("courseName", Arrays.asList(b));
                                query1.findObjects(getActivity(), new FindListener<Resource>() {
                                    @Override
                                    public void onSuccess(List<Resource> list) {
                                        resources = list;
                                        // 逆序
                                        Collections.reverse(resources);
                                        adapter.replaceItem(resources);
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

