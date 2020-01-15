package com.example.wangx.teachingassistantTeacher.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Check;
import com.example.wangx.teachingassistantTeacher.bean.Code;
import com.example.wangx.teachingassistantTeacher.bean.Courselist;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.example.wangx.teachingassistantTeacher.util.MyDate;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 点名界面
 */
public class CheckActivity extends AppCompatActivity {
    ContextUtil app;
    private User user;
    private Spinner sort_by_course;
    private List<Courselist> courselists;
    private String[] c_names;
    private String c_name;
    private ArrayAdapter<String> c_adapter;

    private LinearLayout layout_check;
    private Button bt_check;
    private EditText et_code;
    private TextView tv_place;
    private TextView tv_test;
    private String code;

    // 定位位置
    private String place;
    private double latitude;
    private double longitude;

    private double tLatitude;
    private double tLongitude;

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
                    Toast.makeText(CheckActivity.this,"AmapError"+"location Error, ErrCode:"
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
        setContentView(R.layout.activity_check);

        // 查找界面组件id
        sort_by_course = (Spinner) findViewById(R.id.sort_by_course);
        layout_check = (LinearLayout) findViewById(R.id.layout_check);
        bt_check = (Button) findViewById(R.id.bt_check);
        et_code = (EditText) findViewById(R.id.et_code);
        tv_place = (TextView) findViewById(R.id.tv_place);
        tv_test = (TextView) findViewById(R.id.tv_test);


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
        BmobQuery<Courselist> query = new BmobQuery<>();
        query.addWhereEqualTo("studentId", user.userId);
        query.findObjects(CheckActivity.this, new FindListener<Courselist>() {
            @Override
            public void onSuccess(List<Courselist> list) {
                courselists = list;
                if (null != courselists) {
                    c_names = new String[courselists.size()];
                    //将获取到的课程信息名称存放在数组中
                    int i = 0;
                    for (Courselist courselist : courselists) {
                        c_names[i] = courselist.courseName;
                        i++;
                    }
                } else {
                    Toast.makeText(CheckActivity.this, "课程为空", Toast.LENGTH_SHORT).show();
                    c_names = new String[0];
                }
                //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                c_adapter = new ArrayAdapter<String>(CheckActivity.this, android.R.layout.simple_spinner_item, c_names);
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

                        // 查询当前点名码是否失效
                        BmobQuery<Code> query1 = new BmobQuery<Code>();
                        query1.addWhereEqualTo("courseName", c_name);
                        query1.setLimit(1);
                        // 按时间排序
                        query1.order("-createdAt");
                        query1.findObjects(CheckActivity.this, new FindListener<Code>() {
                            @Override
                            public void onSuccess(List<Code> list) {
                                if (list.size() > 0) {
                                    code = list.get(0).getCode();
                                    String format = "yyyy-MM-dd HH:mm:ss";
                                    String time = list.get(0).getCreatedAt();
                                    tLatitude = list.get(0).getLatitude();
                                    tLongitude = list.get(0).getLongitude();
                                    tv_test.setText("t:"+tLatitude+":"+tLongitude+"\n"+"s:"+latitude+":"+longitude);
                                    String currentTime = MyDate.getCurrentDate(format);
                                    // 点名码未失效则显示当前点名码
                                    if (time.substring(0, 13).equals(currentTime.substring(0, 13))) {
                                        if ((Integer.parseInt(currentTime.substring(14, 16)) - Integer.parseInt(time.substring(14, 16))) <= 10) {
                                            // 可以进行点名
                                            layout_check.setVisibility(View.VISIBLE);
                                        }else {
                                            layout_check.setVisibility(View.INVISIBLE);
                                            Toast.makeText(CheckActivity.this, "当前课程没有正在进行中的点名", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        layout_check.setVisibility(View.INVISIBLE);
                                        Toast.makeText(CheckActivity.this, "当前课程没有正在进行中的点名", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    // 当前课程不能进行点名
                                    layout_check.setVisibility(View.INVISIBLE);
                                    Toast.makeText(CheckActivity.this, "当前课程没有正在进行中的点名", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(CheckActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CheckActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
            }
        });
        // 点名进行
        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(place)){
                    // 获取点名码
                    if (!"".equals(et_code.getText().toString())) {
                        String myCode = et_code.getText().toString();
                        if (code.equals(myCode)) {
                            // 查询当前学生是否已经签到
                            BmobQuery<Check> query1 = new BmobQuery<Check>();
                            query1.addWhereEqualTo("code", code);
                            query1.addWhereEqualTo("studentId", user.getUserId());
                            query1.findObjects(CheckActivity.this, new FindListener<Check>() {
                                @Override
                                public void onSuccess(List<Check> list) {
                                    if (list.size() > 0) {
                                        Toast.makeText(CheckActivity.this, "您已进行过签到，请勿重复进行", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // 判断学生位置和老师位置是否相差固定的距离
                                        if ((tLatitude+0.0001>latitude&&tLongitude+0.0001>longitude)||(tLatitude-0.0001<latitude&&tLongitude-0.0001<longitude)||(tLatitude+0.0001<latitude&&tLongitude-0.0001<longitude)||(tLatitude-0.0001<latitude&&tLongitude+0.0001<longitude)){
                                            Check check = new Check();
                                            check.setCode(code);
                                            check.setStudentId(user.getUserId());
                                            check.setPlace(place);
                                            check.setCourseName(c_name);
                                            check.save(CheckActivity.this, new SaveListener() {
                                                @Override
                                                public void onSuccess() {
                                                    Toast.makeText(CheckActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                    Toast.makeText(CheckActivity.this, "签到失败" + s, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else {
                                            Toast.makeText(CheckActivity.this, "当前位置不在教室，请重试", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onError(int i, String s) {
                                    Toast.makeText(CheckActivity.this, "Error:" + s, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(CheckActivity.this, "点名码无效，请输入正确的点名码", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CheckActivity.this, "请输入对应的点名码", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CheckActivity.this, "无法获取到当前位置，请退出当前界面重试", Toast.LENGTH_SHORT).show();
                }
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
