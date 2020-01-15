package com.example.wangx.teachingassistantTeacher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Code;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.example.wangx.teachingassistantTeacher.util.MyDate;

import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 教师考勤界面
 */
public class KaoqinActivity extends AppCompatActivity {
    ContextUtil app;
    private User user;
    private Spinner sort_by_course;
    private List<Course> courses;
    private String[]c_names;
    private String c_name;
    private ArrayAdapter<String> c_adapter;

    private Button bt_createCode;
    private Button bt_show;
    private LinearLayout layout_code;
    private TextView tv_code;
    private TextView tv_place;
    private Code BCode;

    private String place;
    private double latitude;
    private double longitude;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    place = aMapLocation.getAddress(); // 获取地址
                    latitude = aMapLocation.getLatitude();// 获取精度
                    longitude = aMapLocation.getLongitude();// 获取维度
                    if (!"".equals(place)){
                        tv_place.setText(place);
                        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
                    }
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Toast.makeText(KaoqinActivity.this,"AmapError"+"location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo(),Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaoqin);
        // 查找界面组件id
        sort_by_course = (Spinner) findViewById(R.id.sort_by_course);
        bt_createCode = (Button) findViewById(R.id.bt_createCode);
        bt_show = (Button) findViewById(R.id.bt_show);
        layout_code = (LinearLayout) findViewById(R.id.layout_code);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_place = (TextView) findViewById(R.id.tv_place);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        // 查看当前登陆用户
        app = (ContextUtil) getApplication();
        user = app.getUser();

        // 班级信息获取
        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherId",user.userId);
        query.findObjects(KaoqinActivity.this, new FindListener<Course>() {
            @Override
            public void onSuccess(List<Course> list) {
                courses = list;
                if (null!=courses) {
                    c_names = new String[courses.size()];
                    //将获取到的课程信息名称存放在数组中
                    int i = 0;
                    for (Course course : courses) {
                        c_names[i] = course.courseName;
                        i++;
                    }
                }else {
                    Toast.makeText(KaoqinActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                    c_names = new String[0];
                }
                //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                c_adapter = new ArrayAdapter<String>(KaoqinActivity.this, android.R.layout.simple_spinner_item, c_names);
                //为适配器设置下拉列表下拉时的菜单样式
                c_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //将可选内容添加到下拉列表中
                sort_by_course.setAdapter(c_adapter);

                sort_by_course.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        //c_name得到了选中的值
                        c_name = c_names[arg2];
                        //设置显示当前选项的项
                        arg0.setVisibility(View.VISIBLE);

                        // 清空当前显示
                        bt_createCode.setEnabled(true);
                        layout_code.setVisibility(View.INVISIBLE);
                        // 查询当前点名码是否失效
                        BmobQuery<Code> query1 = new BmobQuery<Code>();
                        query1.addWhereEqualTo("courseName",c_name);
                        query1.setLimit(1);
                        // 按时间排序
                        query1.order("-createdAt");
                        query1.findObjects(KaoqinActivity.this, new FindListener<Code>() {
                            @Override
                            public void onSuccess(List<Code> list) {
                                if (list.size()>0){
                                    String format = "yyyy-MM-dd HH:mm:ss";
                                    String time = list.get(0).getCreatedAt();
                                    String currentTime = MyDate.getCurrentDate(format);
                                    // 点名码未失效则显示当前点名码
                                    if (time.substring(0,10).equals(currentTime.substring(0,10))){
                                        if ((Integer.parseInt(currentTime.substring(11,13))-Integer.parseInt(time.substring(11,13)))<=2){
                                            // 按钮不可见
                                            bt_createCode.setEnabled(false);
                                            layout_code.setVisibility(View.VISIBLE);
                                            tv_code.setText(list.get(0).getCode());
                                        }
                                    }
                                }else {
                                    // 按钮可见
                                    Toast.makeText(KaoqinActivity.this,"没有数据",Toast.LENGTH_SHORT).show();
                                    bt_createCode.setEnabled(true);
                                    layout_code.setVisibility(View.INVISIBLE);
                                }
                            }
                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(KaoqinActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        arg0.setVisibility(View.VISIBLE);
                    }
                });
            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(KaoqinActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });

        // 生成点名码
        bt_createCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 验证码随机生成
                Random random = new Random();
                // 匿名内部类中的成员变量必须设置为final类型
                final int code = random.nextInt(999999) % (900000) + 100000;
                if (latitude!=0&&longitude!=0){
                    // 保存点名码
                    BCode = new Code();
                    BCode.setCode(code+"");
                    BCode.setCourseName(c_name);
                    BCode.setLatitude(latitude);
                    BCode.setLongitude(longitude);
                    BCode.save(KaoqinActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            layout_code.setVisibility(View.VISIBLE);
                            bt_createCode.setEnabled(false);
                            tv_code.setText(code + "");
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(KaoqinActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(KaoqinActivity.this,"当前无法获取位置信息，请退出当前界面重试",Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 考勤结果查看
        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(KaoqinActivity.this,KaoqinResultActivity.class);
                intent.putExtra("courseName",c_name);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }
}
