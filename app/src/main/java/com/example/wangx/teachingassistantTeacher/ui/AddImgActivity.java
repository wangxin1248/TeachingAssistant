package com.example.wangx.teachingassistantTeacher.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Course;
import com.example.wangx.teachingassistantTeacher.bean.Resource;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.loopj.android.http.RequestParams;


import java.io.File;
import java.io.IOException;
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
public class AddImgActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_img_title;
    private ImageView iv_img;
    private Button bt_new_img;
    private Button bt_add_img;
    private Button bt_cancle;
    private Button bt_update;
    private LinearLayout linearlayout_update;

    private String path;// 图片路径
    private User user;
    private Resource resource;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;
    private ProgressDialog progressDialog; // 进度条
    private String encodedString; // 图片编码
    private RequestParams params = new RequestParams(); // 图片上传参数
    ContextUtil app;
    private Spinner sort_by_course;
    private List<Course>courses;
    private String[]c_names;
    private String c_name;
    private ArrayAdapter<String> c_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_img);
        sort_by_course = (Spinner) findViewById(R.id.sort_by_course);

        // 查找id
        et_img_title = (EditText) findViewById(R.id.et_img_title);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        bt_new_img = (Button) findViewById(R.id.bt_new_img);
        bt_add_img = (Button) findViewById(R.id.bt_add_img);
        bt_cancle = (Button) findViewById(R.id.bt_cancle);
        bt_update = (Button) findViewById(R.id.bt_update);
        linearlayout_update = (LinearLayout) findViewById(R.id.linearlayout_update);


        // 设置点击事件
        bt_new_img.setOnClickListener(this);
        bt_add_img.setOnClickListener(this);
        bt_cancle.setOnClickListener(this);
        bt_update.setOnClickListener(this);

        app = (ContextUtil) getApplication();
        user = app.getUser();
        // 进度条初始化
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // 班级信息获取
        BmobQuery<Course> query = new BmobQuery<>();
        query.addWhereEqualTo("teacherId",user.userId);
        query.findObjects(AddImgActivity.this, new FindListener<Course>() {
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
                    Toast.makeText(AddImgActivity.this,"课程为空",Toast.LENGTH_SHORT).show();
                    c_names = new String[0];
                }
                //为下拉列表定义一个适配器，将可选课程内容与ArrayAdapter连接
                c_adapter = new ArrayAdapter<String>(AddImgActivity.this, android.R.layout.simple_spinner_item, c_names);
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
                Toast.makeText(AddImgActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 照片拍摄
            case R.id.bt_new_img:
                // 创建File对象，用于存储拍照后的图片
//                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Date date = new Date(System.currentTimeMillis());
                String filename = format.format(date);
                //存储至DCIM文件夹
                File fpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File outputImage = new File(fpath, "IMG_" + filename + ".jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(AddImgActivity.this, "com.example.wangx.teachingAssistent.fileprovider", outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
                linearlayout_update.setVisibility(View.VISIBLE);
                break;
            // 从相册中选择
            case R.id.bt_add_img:
                // 检测是否获取到了权限
                if (ContextCompat.checkSelfPermission(AddImgActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddImgActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
            // 上传照片
            case R.id.bt_update:
                //获取当前系统的时间
                SimpleDateFormat formater = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                final String str = formater.format(curDate);
                // 初始化数据库对象
                resource = new Resource();
                // 给数据库对象赋值
                resource.title = et_img_title.getText().toString();
                resource.resource_type = "图片";
                resource.t_name = user.userName;
                resource.courseName = c_name;
                if ((!resource.title.equals("")) && (!path.equals(""))) {
//                    long flag = resourceDAO.insert(resource);
                    progressDialog.setMessage("开始上传");
                    progressDialog.show();
                    // 上传文件到bmob云
                    final BmobFile file = new BmobFile(new File(path));
                    file.uploadblock(AddImgActivity.this, new UploadFileListener() {
                        @Override
                        public void onSuccess() {
                            resource.path = file.getFileUrl(AddImgActivity.this);
                            resource.save(AddImgActivity.this, new SaveListener() {
                                @Override
                                public void onSuccess() {
                                    progressDialog.hide();
                                    Toast.makeText(AddImgActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(AddImgActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(AddImgActivity.this,"Error:"+s,Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(AddImgActivity.this, "请输入图片标题", Toast.LENGTH_SHORT).show();
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
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
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
                    Toast.makeText(AddImgActivity.this, "应用缺少运行权限", Toast.LENGTH_SHORT).show();
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
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        path = imageUri.getPath();
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        iv_img.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            default:
                break;
        }
    }

    /**
     * 4。4以下处理图片
     * @param data
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 4。4以上处理图片
     * @param data
     */
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];// 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的uri，则使用普通的处理方式
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);// 根据图片路径显示图片
    }

    /**
     * 显示图片
     * @param imagePath
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            path = imagePath;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            iv_img.setImageBitmap(bitmap);
        } else {
            Toast.makeText(AddImgActivity.this, "无法获取图片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取图片路径
     * @param uri
     * @param selection
     * @return
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}
