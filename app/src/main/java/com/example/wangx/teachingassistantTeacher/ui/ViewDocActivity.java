package com.example.wangx.teachingassistantTeacher.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.Resource;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * 资源查看界面
 */
public class ViewDocActivity extends AppCompatActivity {

    private TextView tv_img_title;
    private TextView tv_resource_teacher;
    private TextView tv_resource_time;
    private Button bt_download;

    private List<Resource> resources;
    private Resource resource;
    private User user;
    ContextUtil app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_doc);
        tv_img_title = (TextView) findViewById(R.id.tv_img_title);
        tv_resource_time = (TextView) findViewById(R.id.tv_resource_time);
        tv_resource_teacher = (TextView) findViewById(R.id.tv_resource_teacher);
        bt_download = (Button) findViewById(R.id.bt_download);

        int position = getIntent().getIntExtra("position", 0);
        resources = (List<Resource>) getIntent().getSerializableExtra("resources");
        resource = resources.get(position);
        app = (ContextUtil) getApplication();
        user = app.getUser();

        // 是学生则允许其下载资源
        if (2 == user.type) {
            bt_download.setVisibility(View.VISIBLE);
            bt_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /**
                     * 下载图片通知
                     */
                    // 创建BmobFile下载文件对象
                    String filename = "";
                    // 下载文档
                    if ("Word".equals(resource.resource_type)){
                        filename = resource.getTitle()+".doc";
                    }else if ("Excel".equals(resource.resource_type)){
                        filename = resource.getTitle()+".xls";
                    }else if ("PPT".equals(resource.resource_type)){
                        filename = resource.getTitle()+".ppt";
                    }
                    File fpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    final File outputImage = new File(fpath, filename);
                    BmobFile file = new BmobFile(filename, "", resource.getPath());

                    file.download(ViewDocActivity.this, outputImage, new DownloadFileListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onProgress(Integer progress, long total) {
                            // 通知提示下载进度
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            Notification notification = new NotificationCompat.Builder(ViewDocActivity.this)
                                    .setContentTitle("开始下载")
                                    .setContentText((progress/total)*100+"%")
                                    .setProgress((int) total, progress, true)
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.drawable.ic_file_download_black_24dp)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                    .build();
                            manager.notify(1, notification);
                        }

                        @Override
                        public void onSuccess(String s) {
                            // 使用Intent打开视频
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.addCategory("android.intent.category.DEFAULT");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Uri uri = Uri.fromFile(outputImage);
                            if ("Word".equals(resource.resource_type)){
                                intent.setDataAndType(uri, "application/msword");
                            }else if ("Excel".equals(resource.resource_type)){
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                            }else if ("PPT".equals(resource.resource_type)){
                                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                            }
                            PendingIntent pi = PendingIntent.getActivities(ViewDocActivity.this, 0, new Intent[]{intent}, 0);

                            // 使用通知来提示用户下载完成
                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            Notification notification = new NotificationCompat.Builder(ViewDocActivity.this)
                                    .setContentTitle("下载完成")
                                    .setContentText("100%")
                                    .setProgress(0, 0, false)
                                    .setWhen(System.currentTimeMillis())
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                    .setPriority(NotificationCompat.PRIORITY_MAX)
                                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                                    .setContentIntent(pi)
                                    .build();
                            manager.notify(1, notification);
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(ViewDocActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
        // 设置界面显示数据
        tv_resource_teacher.setText("教师：" + resource.t_name);
        tv_resource_time.setText(resource.getCreatedAt());
        tv_img_title.setText(resource.title);
    }

}
