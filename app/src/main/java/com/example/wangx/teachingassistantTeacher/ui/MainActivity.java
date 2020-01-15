package com.example.wangx.teachingassistantTeacher.ui;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Notice;
import com.example.wangx.teachingassistantTeacher.bean.Resource;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.ui.fragment.ActivityFragment;
import com.example.wangx.teachingassistantTeacher.ui.fragment.HomeFragment;
import com.example.wangx.teachingassistantTeacher.ui.fragment.NoticeFragment;
import com.example.wangx.teachingassistantTeacher.ui.fragment.ResourceFragment;
import com.example.wangx.teachingassistantTeacher.ui.helper.BottomNavigationViewHelper;
import com.example.wangx.teachingassistantTeacher.adapter.ViewPagerAdapter;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * 项目主界面
 */
public class MainActivity extends AppCompatActivity
        implements
        NoticeFragment.OnListFragmentInteractionListener
        ,ResourceFragment.OnListFragmentInteractionListener {

    private User user;
    private ContextUtil app;
    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationItemView navigation_resource;
    // 底部菜单选择事件响应
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("RestrictedApi")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                // 公告
                case R.id.navigation_notice:
                    viewPager.setCurrentItem(0);
                    // 动态修改底部导航栏的内容
                    navigation_resource = (BottomNavigationItemView) findViewById(R.id.navigation_resource);
                    // 教师身份
                    if(1==user.type){
                        navigation_resource.setTitle("资源");
                    }
                    // 学生身份
                    if(2==user.type){
                        navigation_resource.setTitle("学习");
                    }
                    return true;
                // 资源
                case R.id.navigation_activity:
                    viewPager.setCurrentItem(2);
                    // 动态修改底部导航栏的内容
                    navigation_resource = (BottomNavigationItemView) findViewById(R.id.navigation_resource);
                    // 教师身份
                    if(1==user.type){
                        navigation_resource.setTitle("资源");
                    }
                    // 学生身份
                    if(2==user.type){
                        navigation_resource.setTitle("学习");
                    }
                    return true;
                // 活动
                case R.id.navigation_resource:
                    viewPager.setCurrentItem(1);
                    // 动态修改底部导航栏的内容
                    navigation_resource = (BottomNavigationItemView) findViewById(R.id.navigation_resource);
                    // 教师身份
                    if(1==user.type){
                        navigation_resource.setTitle("资源");
                    }
                    // 学生身份
                    if(2==user.type){
                        navigation_resource.setTitle("学习");
                    }
                    return true;
                // 我
                case R.id.navigation_home:
                    viewPager.setCurrentItem(3);
                    // 动态修改底部导航栏的内容
                    navigation_resource = (BottomNavigationItemView) findViewById(R.id.navigation_resource);
                    // 教师身份
                    if(1==user.type){
                        navigation_resource.setTitle("资源");
                    }
                    // 学生身份
                    if(2==user.type){
                        navigation_resource.setTitle("学习");
                    }
                    return true;
            }
            return false;
        }

    };

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (ContextUtil) getApplication();
        user = app.getUser();
        // 动态修改底部导航栏的内容
        navigation_resource = (BottomNavigationItemView) findViewById(R.id.navigation_resource);
        // 教师身份
        if(1==user.type){
            navigation_resource.setTitle("资源");
        }
        // 学生身份
        if(2==user.type){
            navigation_resource.setTitle("学习");
        }


        viewPager = (ViewPager)findViewById(R.id.viewpager);
        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
        BottomNavigationViewHelper.disableShiftMode(navigation);

        // 设置底部菜单点击事件
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // viewPager操作
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            // 当前界面被选中设置底部菜单显示
            @Override
            public void onPageSelected(int position) {
                if(menuItem!=null){
                    menuItem.setChecked(false);
                }else{
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //禁止ViewPager滑动
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        setupViewPager(viewPager);
    }

    private void setBottonTextAndIcon(){

    }

    /**
     * ViewPager界面设置
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // 添加对应的fragment界面
        adapter.addFragment(new NoticeFragment());
        adapter.addFragment(new ResourceFragment());
        adapter.addFragment(new ActivityFragment());
        adapter.addFragment(new HomeFragment());

        // 设置适配器
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onListFragmentInteraction(Notice item) {

    }

    @Override
    public void onListFragmentInteraction(Resource item) {

    }
}
