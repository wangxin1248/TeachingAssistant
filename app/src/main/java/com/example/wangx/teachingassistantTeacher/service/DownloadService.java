package com.example.wangx.teachingassistantTeacher.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import cn.bmob.v3.datatype.BmobFile;

/**
 * 资源下载服务
 */
public class DownloadService extends Service {
    private BmobFile bmobFile;
    public DownloadService() {
    }

    private UploadBinder uploadBinder = new UploadBinder();
    @Override
    public IBinder onBind(Intent intent) {
        return uploadBinder;
    }

    private class UploadBinder extends Binder{
        public void setBmobFile(BmobFile file){
            bmobFile = file;
        }

    }
}
