package com.example.wangx.teachingassistantTeacher.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Resource;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 上传照片界面
 */
@SuppressLint("NewApi")
public class AddVideoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_img_title;
    private VideoView iv_img;
    private Button bt_add_video;
    private Button bt_cancle;
    private Button bt_update;
    private LinearLayout linearlayout_update;

    private String path;// 视频路径
    private String url;//视频缩略图
    private MediaController mController; // 视频播放控制
    private User user;
    private Resource resource;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private ProgressDialog progressDialog; // 进度条
    ContextUtil app;
    private Spinner sort_by_course;
    private List<Course>courses;
    private String[]c_names;
    private String c_name;
    private ArrayAdapter<String> c_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        sort_by_course = (Spinner) findViewById(R.id.sort_by_course);
        mController = new MediaController(this);

        // 查找id
        et_img_title = (EditText) findViewById(R.id.et_img_title);
        iv_img = (VideoView) findViewById(R.id.iv_img);
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

        // 班级信息获取
        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherId",user.userId);
        query.findObjects(AddVideoActivity.this, new FindListener<Course>() {
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
                    Toast.makeText(AddVideoActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                    c_names = new String[0];
                }
                //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                c_adapter = new ArrayAdapter<String>(AddVideoActivity.this, android.R.layout.simple_spinner_item, c_names);
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
                Toast.makeText(AddVideoActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 从相册中选择
            case R.id.bt_add_video:
                // 检测是否获取到了权限
                if (ContextCompat.checkSelfPermission(AddVideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddVideoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    // 打开相册
                    openAlbum();
                }
                linearlayout_update.setVisibility(View.VISIBLE);
                break;
            // 取消上传
            case R.id.bt_cancle:
                finish();
                break;
            // 上传视频
            case R.id.bt_update:
                //获取当前系统的时间
                SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                final String str = formater.format(curDate);
                // 初始化数据库对象
                resource = new Resource();
                // 给数据库对象赋值
                resource.title = et_img_title.getText().toString();
                resource.resource_type = "视频";
                resource.t_name = user.userName;
                resource.courseName = c_name;
                if ((!resource.title.equals("")) && (!path.equals(""))) {
                    progressDialog.setMessage("准备上传...");
                    progressDialog.show();
                    // 创建视频文件上传对象
                    final BmobFile file = new BmobFile(new File(path));
                    // 开始上传视频
                    file.uploadblock(AddVideoActivity.this, new UploadFileListener() {
                        @Override
                        public void onSuccess() {
                            // 保存视频路径到数据库
                            resource.path = file.getFileUrl(AddVideoActivity.this);
                            // 保存视频缩略图到数据库
                            resource.url = "http://bmob-cdn-18036.b0.upaiyun.com/2018/04/17/e03505cc40d5f18480a54730ef5599eb.png";
                            resource.save(AddVideoActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.hide();
                                    Toast.makeText(AddVideoActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(AddVideoActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AddVideoActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                        }

                        // 文件上传进度
                        @Override
                        public void onProgress(Integer value) {
                            super.onProgress(value);
                            progressDialog.setMessage("开始上传"+value+"%");
                        }
                    });
                } else {
                    Toast.makeText(AddVideoActivity.this, "请输入视频标题", Toast.LENGTH_SHORT).show();
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
     * 打开文件浏览
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, CHOOSE_PHOTO);
    }


    /**
     * 动态检测系统运行权限
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
                    Toast.makeText(AddVideoActivity.this, "应用缺少运行权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    /**
     * 处理activity返回的结果
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ContentResolver cr = this.getContentResolver();
                /** 数据库查询操作。
                 * 第一个参数 uri：为要查询的数据库+表的名称。
                 * 第二个参数 projection ： 要查询的列。
                 * 第三个参数 selection ： 查询的条件，相当于SQL where。
                 * 第三个参数 selectionArgs ： 查询条件的参数，相当于 ？。
                 * 第四个参数 sortOrder ： 结果排序。
                 */
                Cursor cursor = cr.query(uri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        // 视频ID:MediaStore.Audio.Media._ID
                        int videoId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                        // 视频名称：MediaStore.Audio.Media.TITLE
                        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                        // 视频路径：MediaStore.Audio.Media.DATA
                        String videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                        // 视频时长：MediaStore.Audio.Media.DURATION
                        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                        // 视频大小：MediaStore.Audio.Media.SIZE
                        long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));

                        // 视频缩略图路径：MediaStore.Images.Media.DATA
                        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                        // 缩略图ID:MediaStore.Audio.Media._ID
                        int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));

                        // 方法一 Thumbnails 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                        // 第一个参数为 ContentResolver，第二个参数为视频缩略图ID， 第三个参数kind有两种为：MICRO_KIND和MINI_KIND 字面意思理解为微型和迷你两种缩略模式，前者分辨率更低一些。
                        Bitmap bitmap1 = MediaStore.Video.Thumbnails.getThumbnail(cr, imageId, MediaStore.Video.Thumbnails.MICRO_KIND, null);

                        // 方法二 ThumbnailUtils 利用createVideoThumbnail 通过路径得到缩略图，保持为视频的默认比例
                        // 第一个参数为 视频/缩略图的位置，第二个依旧是分辨率相关的kind
                        Bitmap bitmap2 = ThumbnailUtils.createVideoThumbnail(imagePath, MediaStore.Video.Thumbnails.MICRO_KIND);
                        // 如果追求更好的话可以利用 ThumbnailUtils.extractThumbnail 把缩略图转化为的制定大小
//                        ThumbnailUtils.extractThumbnail(bitmap, width,height ,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

                        // 显示视频并将视频路径以及视频缩略图路径保存
                        iv_img.setVideoPath(videoPath);
                        iv_img.setMediaController(mController);
//                        iv_img.start();
                        iv_img.requestFocus();
                        path = videoPath;
                    }
                    cursor.close();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
