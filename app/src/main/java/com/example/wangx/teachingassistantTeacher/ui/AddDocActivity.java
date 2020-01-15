package com.example.wangx.teachingassistantTeacher.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Resource;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
/**
 * 上传文档界面
 */
public class AddDocActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_img_title;
    private Button bt_add_video;
    private Button bt_cancle;
    private Button bt_update;
    private LinearLayout linearlayout_update;

    private String path;// 文档路径
    private TextView tv_doc_name;
    private User user;
    private Resource resource;
    public static final int CHOOSE_PHOTO = 2;
    private ProgressDialog progressDialog; // 进度条
    ContextUtil app;
    private Spinner sort_by_course;
    private List<Course> courses;
    private String[]c_names;
    private String c_name;
    private ArrayAdapter<String> c_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doc);
        sort_by_course = (Spinner) findViewById(R.id.sort_by_course);

        // 查找id
        tv_doc_name = (TextView) findViewById(R.id.tv_doc_name);
        et_img_title = (EditText) findViewById(R.id.et_img_title);
        bt_add_video = (Button) findViewById(R.id.bt_add_video);
        bt_cancle = (Button) findViewById(R.id.bt_cancle);
        bt_update = (Button) findViewById(R.id.bt_update);
        linearlayout_update = (LinearLayout) findViewById(R.id.linearlayout_update);


        // 设置点击事件
        bt_add_video.setOnClickListener(this);
        bt_cancle.setOnClickListener(this);
        bt_update.setOnClickListener(this);

        // 查询当前登陆用户
        app = (ContextUtil) getApplication();
        user = app.getUser();
        // 进度条初始化
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        // 初始化数据库对象
        resource = new Resource();
        // 班级信息获取
        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherId",user.userId);
        query.findObjects(AddDocActivity.this, new FindListener<Course>() {
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
                    Toast.makeText(AddDocActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                    c_names = new String[0];
                }
                //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                c_adapter = new ArrayAdapter<String>(AddDocActivity.this, android.R.layout.simple_spinner_item, c_names);
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
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        arg0.setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(AddDocActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 从文件中选择
            case R.id.bt_add_video:
                // 检测是否获取到了权限
                if (ContextCompat.checkSelfPermission(AddDocActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddDocActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    // 打开文件
                    openAlbum();
                }
                linearlayout_update.setVisibility(View.VISIBLE);
                break;
            // 取消上传
            case R.id.bt_cancle:
                finish();
                break;
            // 上传文档
            case R.id.bt_update:
                // 给数据库对象赋值
                resource.title = et_img_title.getText().toString();
                resource.t_name = user.userName;
                resource.courseName = c_name;
                if ((!resource.title.equals("")) && (!path.equals(""))) {
                    progressDialog.setMessage("准备上传...");
                    progressDialog.show();
                    // 创建视频文件上传对象
                    final BmobFile file = new BmobFile(new File(path));
                    // 开始上传视频
                    file.uploadblock(AddDocActivity.this, new UploadFileListener() {
                        @Override
                        public void onSuccess() {
                            // 保存视频路径到数据库
                            resource.path = file.getFileUrl(AddDocActivity.this);
                            resource.save(AddDocActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.hide();
                                    Toast.makeText(AddDocActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(AddDocActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AddDocActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                        }

                        // 文件上传进度
                        @Override
                        public void onProgress(Integer value) {
                            super.onProgress(value);
                            progressDialog.setMessage("开始上传"+value+"%");
                        }
                    });
                } else {
                    Toast.makeText(AddDocActivity.this, "请输入文档标题", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    /**
     * 销毁方法
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

    /**
     * 打开文件浏览文档，支持ppt,word,excel,使用Intent启动
     */
    private void openAlbum() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CHOOSE_PHOTO);
    }


    /**
     * 动态检测系统运行权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(AddDocActivity.this, "应用缺少运行权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    /**
     * 处理Activity返回的结果
     * 获取视频的路径并显示
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                // 获取文件路径
                String url = Uri.decode(data.getDataString());
                // 判断文件类型
                int n = url.lastIndexOf(".");
                String s = url.substring(n+1,url.length());
                if (s.equals("xls")||s.equals("xlsx")){
                    n = url.indexOf("/storage");
                    String str =  url.substring(n,url.length());
                    path = str;
                    String[] ss=str.split("/");
                    resource.resource_type = "Excel";
                    // 保存excel缩略图到数据库
                    resource.url = "http://bmob-cdn-18036.b0.upaiyun.com/2018/04/17/f23a220c40c97636805e1500b2260e93.png";
                    tv_doc_name.setText("文档名称："+ss[ss.length-1]);
                }else if(s.equals("doc")||s.equals("docx")){
                    n = url.indexOf("/storage");
                    String str =  url.substring(n,url.length());
                    path = str;
                    String[] ss=str.split("/");
                    resource.resource_type = "Word";
                    // 保存word缩略图到数据库
                    resource.url = "http://bmob-cdn-18036.b0.upaiyun.com/2018/04/17/9f1028cf406e40b380f8da380e409775.png";
                    tv_doc_name.setText("文档名称："+ss[ss.length-1]);
                }else if (s.equals("ppt")||s.equals("pptx")){
                    n = url.indexOf("/storage");
                    String str =  url.substring(n,url.length());
                    path = str;
                    String[] ss=str.split("/");
                    resource.resource_type = "PPT";
                    // 保存ppt缩略图到数据库
                    resource.url = "http://bmob-cdn-18036.b0.upaiyun.com/2018/04/17/60b5b4f540a6264280b1249161835832.png";
                    tv_doc_name.setText("文档名称："+ss[ss.length-1]);
                }else{
                    Toast.makeText(AddDocActivity.this,"文件格式错误，请重新选择！",Toast.LENGTH_SHORT).show();
                    openAlbum();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
