package com.example.wangx.teachingassistantTeacher.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wangx.teachingassistantTeacher.R;
import com.example.wangx.teachingassistantTeacher.bean.DefaultUser;
import com.example.wangx.teachingassistantTeacher.bean.Message;
import com.example.wangx.teachingassistantTeacher.bean.MyMessage;
import com.example.wangx.teachingassistantTeacher.bean.User;
import com.example.wangx.teachingassistantTeacher.ui.view.ChatView;
import com.example.wangx.teachingassistantTeacher.util.ContextUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;
import cn.jiguang.imui.chatinput.ChatInputView;
import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener;
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener;
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener;
import cn.jiguang.imui.chatinput.model.FileItem;
import cn.jiguang.imui.chatinput.model.VideoItem;
import cn.jiguang.imui.commons.ImageLoader;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.messages.MsgListAdapter;

/**
 * 聊天界面
 */
public class TalkActivity extends Activity implements ChatView.OnKeyboardChangedListener,
        ChatView.OnSizeChangedListener, View.OnTouchListener {

    private final int REQUEST_RECORD_VOICE_PERMISSION = 0x0001;
    private final int REQUEST_CAMERA_PERMISSION = 0x0002;
    private final int REQUEST_PHOTO_PERMISSION = 0x0003;

    private Context mContext;// 上下文对象

    private ChatView mChatView;//聊天内容组件
    private MsgListAdapter<MyMessage> mAdapter;//消息内容填充器
    private List<MyMessage> mData;//消息集合

    private InputMethodManager mImm;//输入框组件
    private Window mWindow;//窗体组件
    ContextUtil app;
    private User user;
    private String courseName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_talk);
        app = (ContextUtil) getApplication();
        user = app.getUser();
        DefaultUser defaultUser = new DefaultUser(user.userId, user.userName, user.image);
        // 获取当前的聊天课程名称
        courseName = getIntent().getStringExtra("courseName");
        this.mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mWindow = getWindow();

        mChatView = (ChatView) findViewById(R.id.chat_view);
        mChatView.initModule();
        // 为当前聊天室设置名称
        mChatView.setTitle(courseName);
        // 获取当前聊天信息
        getMessages();
        //mData = getMessages();

        // 数据实时更新
        final BmobRealTimeData rtd = new BmobRealTimeData();
        rtd.start(TalkActivity.this,new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                if (rtd.isConnected()){
                    // 监听消息表更新
                    rtd.subTableUpdate("Message");
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                MyMessage message;
                try {
                    JSONObject data = jsonObject.getJSONObject("data");
                    String text = data.optString("text");
                    String userId = data.optString("userid");
                    String userName = data.optString("userName");
                    String img = data.optString("img");
                    if (!userId.equals(user.userId)){
                        message = new MyMessage(text, IMessage.MessageType.RECEIVE_TEXT);
                        message.setUserInfo(new DefaultUser(userId, userName,  img));
                        mAdapter.addToStart(message, true);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        // 监听用户输入
        mChatView.setKeyboardChangedListener(this);
        // 监听聊天界面改变
        mChatView.setOnSizeChangedListener(this);
        // 监听触摸事件
        mChatView.setOnTouchListener(this);

        // 菜单点击监听事件
        mChatView.setMenuClickListener(new OnMenuClickListener() {
            /**
             * 发送文字消息
             * @param input
             * @return
             */
            @Override
            public boolean onSendTextMessage(final CharSequence input) {
                if (input.length() == 0) {
                    return false;
                }
                final boolean[] flag = new boolean[1];
                // 文字类型消息
                final MyMessage message = new MyMessage(input.toString(), IMessage.MessageType.SEND_TEXT);
                // 创建聊天用户
                message.setUserInfo(new DefaultUser(user.userId, user.userName,  user.image));
                // 消息时间
                message.setTimeString("                                 "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())+"\n                                                                                        "+user.userName);
                Message message1 = new Message();
                message1.setCourseName(courseName);
                message1.setText(input.toString());
                message1.setUserid(user.userId);
                message1.setUserName(user.userName);
                message1.setImg(user.image);
                message1.save(TalkActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        mAdapter.addToStart(message, true);
                        mChatView.getChatInputView().getInputView().setText("");
                        flag[0] = true;
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(TalkActivity.this,"Error:"+i,Toast.LENGTH_SHORT).show();
                        flag[0] = false;
                    }
                });
                // 将消息添加到adapter开始位置中
                return flag[0];
            }

            /**
             * 发送文件
             * @param list
             */
            @Override
            public void onSendFiles(List<FileItem> list) {
                if (list == null || list.isEmpty()) {
                    return;
                }
                MyMessage message;
                for (FileItem item : list) {

                    // 图像文件
                    if (item.getType() == FileItem.Type.Image) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE);

                        // 视频文件
                    } else if (item.getType() == FileItem.Type.Video) {
                        message = new MyMessage(null, IMessage.MessageType.SEND_VIDEO);
                        message.setDuration(((VideoItem) item).getDuration());

                    } else {
                        throw new RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video");
                    }

                    //发送时间
                    message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                    // 文件路径
                    message.setMediaFilePath(item.getFilePath());
                    // 创建消息发送用户
                    message.setUserInfo(new DefaultUser(user.userId, user.userName,  user.image));
                    // 添加到消息adapter中
                    final MyMessage fMsg = message;
                    TalkActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addToStart(fMsg, true);
                        }
                    });
                }
            }

            /**
             * 语音聊天
             */
            @Override
            public void switchToMicrophoneMode() {
                if ((ActivityCompat.checkSelfPermission(TalkActivity.this,
                        Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(TalkActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_VOICE_PERMISSION);
                }
            }

            /**
             * 打开相册
             */
            @Override
            public void switchToGalleryMode() {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TalkActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, REQUEST_PHOTO_PERMISSION);
                }
            }

            /**
             * 打开照相机
             */
            @Override
            public void switchToCameraMode() {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{
                            Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_CAMERA_PERMISSION);
                }

                File rootDir = mContext.getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/photo";
                mChatView.setCameraCaptureFile(fileDir, "temp_photo");
            }
        });

        /**
         * 发送录音
         */
        mChatView.setRecordVoiceListener(new RecordVoiceListener() {
            @Override
            public void onStartRecord() {
                // Show record voice interface
                File rootDir = mContext.getFilesDir();
                String fileDir = rootDir.getAbsolutePath() + "/voice";
                mChatView.setRecordVoiceFile(fileDir, new DateFormat().format("yyyy_MMdd_hhmmss",
                        Calendar.getInstance(Locale.CHINA)) + "");
            }

            @Override
            public void onFinishRecord(File voiceFile, int duration) {
                MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_VOICE);
                message.setUserInfo(new DefaultUser(user.userId, user.userName,  user.image));
                message.setMediaFilePath(voiceFile.getPath());
                message.setDuration(duration);
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                mAdapter.addToStart(message, true);
            }

            @Override
            public void onCancelRecord() {

            }
        });

        /**
         * 打开相册
         */
        mChatView.setOnCameraCallbackListener(new OnCameraCallbackListener() {
            @Override
            public void onTakePictureCompleted(String photoPath) {
                final MyMessage message = new MyMessage(null, IMessage.MessageType.SEND_IMAGE);
                message.setTimeString(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
                message.setMediaFilePath(photoPath);
                message.setUserInfo(new DefaultUser(user.userId, user.userName,  user.image));
                TalkActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addToStart(message, true);
                    }
                });
            }

            @Override
            public void onStartVideoRecord() {

            }

            @Override
            public void onFinishVideoRecord(String videoPath) {

            }

            @Override
            public void onCancelVideoRecord() {

            }
        });
    }

    /**
     * 请求相应权限
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_VOICE_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(mContext, "User denied permission, can't use record voice feature.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(mContext, "User denied permission, can't use take aurora_menuitem_photo feature.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_PHOTO_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // Permission denied
                Toast.makeText(mContext, "User denied permission, can't use select aurora_menuitem_photo feature.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 获取当前聊天信息
    private void getMessages() {
        final List<MyMessage> myMessages = new ArrayList<>();
        // 查询当前课程的聊天记录
        BmobQuery<Message> query = new BmobQuery<>();
        query.addWhereEqualTo("courseName",courseName);
        query.order("-createdAt");
        query.findObjects(TalkActivity.this, new FindListener<Message>() {
            @Override
            public void onSuccess(List<Message> list) {
                if (list.size()>0){
                    for (int i=0;i<list.size();i++){
                        MyMessage message;
                        // 发送消息
                        if (list.get(i).getUserid().equals(user.userId)){
                            message = new MyMessage(list.get(i).getText(), IMessage.MessageType.SEND_TEXT);
                            message.setUserInfo(new DefaultUser(list.get(i).getUserid(), list.get(i).getUserName(),  list.get(i).getImg()));
                            message.setTimeString("                                 "+list.get(i).getCreatedAt()+"\n                                                                                      "+list.get(i).getUserName());
                            // 接收消息
                        }else {
                            message = new MyMessage(list.get(i).getText(), IMessage.MessageType.RECEIVE_TEXT);
                            message.setUserInfo(new DefaultUser(list.get(i).getUserid(), list.get(i).getUserName(),  list.get(i).getImg()));
                            message.setTimeString("                                " + list.get(i).getCreatedAt()+"\n"+list.get(i).getUserName()+"                                                                                        ");
                        }
                        myMessages.add(message);
                    }
                    mData = myMessages;
                    // 初始化内容填充器
                    initMsgAdapter();
                }
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 初始化聊天内容填充器
     */
    private void initMsgAdapter() {
        // 设置用户头像
        ImageLoader imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(TalkActivity.this).load(url).resize(60,60).centerCrop().into(imageView);
            }
        };

        // Use default layout
        MsgListAdapter.HoldersConfig holdersConfig = new MsgListAdapter.HoldersConfig();
        mAdapter = new MsgListAdapter<>("0", holdersConfig, imageLoader);

        // If you want to customise your layout, try to create custom ViewHolder:
        // holdersConfig.setSenderTxtMsg(CustomViewHolder.class, layoutRes);
        // holdersConfig.setReceiverTxtMsg(CustomViewHolder.class, layoutRes);
        // CustomViewHolder must extends ViewHolders defined in MsgListAdapter.
        // Current ViewHolders are TxtViewHolder, VoiceViewHolder.

        /**
         * 聊天相关内容点击事件响应
         */
        mAdapter.setOnMsgClickListener(new MsgListAdapter.OnMsgClickListener<MyMessage>() {
            @Override
            public void onMessageClick(MyMessage message) {
                // do something
                if (message.getType() == IMessage.MessageType.RECEIVE_VIDEO
                        || message.getType() == IMessage.MessageType.SEND_VIDEO) {
                    if (!TextUtils.isEmpty(message.getMediaFilePath())) {
                        Intent intent = new Intent(TalkActivity.this, VideoActivity.class);
                        intent.putExtra(VideoActivity.VIDEO_PATH, message.getMediaFilePath());
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.message_click_hint),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 长按消息事件
         */
        mAdapter.setMsgLongClickListener(new MsgListAdapter.OnMsgLongClickListener<MyMessage>() {
            @Override
            public void onMessageLongClick(MyMessage message) {
                Toast.makeText(mContext, mContext.getString(R.string.message_long_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        /**
         * 头像点击事件
         */
        mAdapter.setOnAvatarClickListener(new MsgListAdapter.OnAvatarClickListener<MyMessage>() {
            @Override
            public void onAvatarClick(MyMessage message) {
                DefaultUser userInfo = (DefaultUser) message.getFromUser();
                Toast.makeText(mContext, mContext.getString(R.string.avatar_click_hint),
                        Toast.LENGTH_SHORT).show();
                // do something
            }
        });

        MyMessage message = new MyMessage("Hello World", IMessage.MessageType.RECEIVE_TEXT);
        message.setUserInfo(new DefaultUser(user.userId, user.userName,  user.image));
//        message.setTimeString();

        mAdapter.addToEnd(mData);
        mAdapter.setOnLoadMoreListener(new MsgListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(int page, int totalCount) {
                if (totalCount < mData.size()) {
                    loadNextPage();
                }
            }
        });

        mChatView.setAdapter(mAdapter);
    }

    private void loadNextPage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addToEnd(mData);
            }
        }, 1000);
    }

    @Override
    public void onKeyBoardStateChanged(int state) {
        switch (state) {
            case ChatInputView.KEYBOARD_STATE_INIT:
                ChatInputView chatInputView = mChatView.getChatInputView();
                if (mImm != null) {
                    mImm.isActive();
                }
                if (chatInputView.getMenuState() == View.INVISIBLE
                        || (!chatInputView.getSoftInputState()
                        && chatInputView.getMenuState() == View.GONE)) {

                    mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                            | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    chatInputView.dismissMenuLayout();
                }
                break;
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (oldh - h > 300) {
            mChatView.setMenuHeight(oldh - h);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ChatInputView chatInputView = mChatView.getChatInputView();

                if (view.getId() == chatInputView.getInputView().getId()) {

                    if (chatInputView.getMenuState() == View.VISIBLE
                            && !chatInputView.getSoftInputState()) {
                        chatInputView.dismissMenuAndResetSoftMode();
                        return false;
                    } else {
                        return false;
                    }
                }
                if (chatInputView.getMenuState() == View.VISIBLE) {
                    chatInputView.dismissMenuLayout();
                }
                if (chatInputView.getSoftInputState()) {
                    View v = getCurrentFocus();

                    if (mImm != null && v != null) {
                        mImm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        mWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                        chatInputView.setSoftInputState(false);
                    }
                }
                break;
        }
        return false;
    }
}
