package com.tts;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.entities.DensityUtil;
import com.tucodec.voip.TYVoipAndroid;
import com.tucodec.voip.TYVoipVideoCapture;
import com.tucodec.voip.TYVoipVideoRender;
import com.tucodec.voip.VoipImage;

public class ChatActivity extends AppCompatActivity {

    private String ip;
    private int port, teacherId, studentId;
    private Boolean mSilentMode = true, mSwitchCam = false, mEnableCam = false;
    private TextView toggle_voice_txt,toggle_cam_txt;
    private LinearLayout toggle_voice, finish_meeting, switch_cam, toggle_cam;
    private int screenWidth = 0;
    private int screenHeight = 0;
    private int controlWidth = 0;
    private int controlHeight = 0;
    private int statusHeight = 0;
    private int[] location = new int[2];
    /**
     * TYVoipVideoRender 是 VoIP 客户端的视频渲染视图
     * 用于 VoipImage 的显示
     */
    private TYVoipVideoRender mMainRender,render1;
    private TYVoipAndroid mVoIPClient;
    private TYVoipVideoCapture mVideoCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        getValues();
        setClick();
        initGroupChat();
        putRender();
    }

    /**
     * VoIP 节点, Key: userId, Value: TYVoipVideoRender
     */
    //VoipRender容器，每个会议室成员userId对应一个VoipRender渲染视图
    private SparseArray<TYVoipVideoRender> mVoIPPeers = new SparseArray<TYVoipVideoRender>();

    private TYVoipAndroid.VideoCallback mVideoCallback = new TYVoipAndroid.VideoCallback() {
        @Override
        public void renderVoipVideoData(VoipImage voipImage) {
            //如果image为本地相机返还数据
            if (voipImage.isLocal){
                //本地预览视频
                //设置镜像显示
                mMainRender.setMirror(false);
                mMainRender.pushVideoFrame(voipImage);
            }else {
                mVoIPPeers.get(voipImage.userId).setMirror(false);
                mVoIPPeers.get(voipImage.userId).pushVideoFrame(voipImage);
            }
        }
    };

    private void initView() {
        mMainRender = findViewById(R.id.meeting_view);
        render1 = findViewById(R.id.Member_render1);
        toggle_voice_txt = findViewById(R.id.toggle_voice_txt);
        switch_cam = findViewById(R.id.switch_cam);
        //切换摄像头
        switch_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoCapture.stop();
                mSwitchCam = !mSwitchCam;
                mVideoCapture.start(mSwitchCam, mVoIPClient.getConfig(), (ViewGroup)mMainRender.getParent());
            }
        });
        toggle_voice = findViewById(R.id.toggle_voice);
        //设置是否静音
        toggle_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSilentMode) {
                    //取消静音
                    mVoIPClient.unmuteMicrophone();
                    toggle_voice_txt.setText("静音");
                } else {
                    //静音
                    mVoIPClient.muteMicrophone();
                    toggle_voice_txt.setText("取消静音");
                }
                mSilentMode = !mSilentMode;
            }
        });
        toggle_cam_txt = findViewById(R.id.toggle_cam_txt);
        toggle_cam = findViewById(R.id.toggle_cam);
        toggle_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEnableCam) {
                    //关闭摄像头
                    mVideoCapture.pause();
                    toggle_cam_txt.setText("打开摄像头");
                } else {
                    //打开摄像头
                    mVideoCapture.resume();
                    toggle_cam_txt.setText("关闭摄像头");
                }
                mEnableCam = !mEnableCam;
            }
        });
        finish_meeting = findViewById(R.id.finish_meeting);
        finish_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //同login对应，自身当前
                mVoIPClient.logoutRelayServer();
                //清空该userId add的所有节点，相当于逐个remove
                mVoIPClient.clearClientNodeList();
                // 退出会议室
                //停止相机视频捕捉
                mVideoCapture.stop();
                //结束Voip通话
                mVoIPClient.stopCall();
                //同登录对应，登出转发服务器；
                mVoIPClient.logoutRelayServer();
                //释放资源
                mVoIPClient.release();
                mVoIPClient = null;
                finish();
            }
        });
    }

    private void setClick(){
        //点击事件，放大缩小render
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
        ConstraintLayout.LayoutParams params_meeting = (ConstraintLayout.LayoutParams) mMainRender.getLayoutParams();
        params_meeting.width = screenWidth;
        params_meeting.height = screenHeight;
        mMainRender.setLayoutParams(params_meeting);

        controlWidth  = DensityUtil.dip2px(this,150f);
        controlHeight = DensityUtil.dip2px(this,100f);
        statusHeight = getStatusBarHeight();
        render1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exchangeRenderLocation(0);
            }
        });
    }

    //获取状态栏高度
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //显示缩小 Member_render
    private void exchangeRenderLocation(int i){
        if (render1.getWidth() !=screenWidth){
            render1.getLocationOnScreen(location);
            for (int s = 0;s<i;s++){
                render1.setVisibility(View.INVISIBLE);
                Log.i("voip_android", "exchangeRenderLocation arrayRender[${i}] = GONE");
            }
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) render1.getLayoutParams();
            params.width = screenWidth;
            params.height = screenHeight;
            render1.setLayoutParams(params);
        }else{
            for (int s = 0;s<i;s++){
                render1.setVisibility(View.VISIBLE);
            }
            ConstraintLayout.LayoutParams params_meeting = (ConstraintLayout.LayoutParams) render1.getLayoutParams();
            params_meeting.width = controlWidth;
            params_meeting.height = controlHeight;
            params_meeting.setMargins(location[0],location[1]-i*controlHeight-statusHeight,0,0);
            render1.setLayoutParams(params_meeting);
        }
    }

    private void initGroupChat() {
        //建议将TYVoipAndroid写成单例，方便在别处调用loginRelayServer，与logoutRelayServer
        mVoIPClient = new TYVoipAndroid(new Handler(),getApplication(),null);
        /**
         *连接转发服务区
         *@param ip @param 端口号@param userId @param sessionId
         *@param AppKey @param AppSecret @return !0即为成功
         */
        //登录转发服务器，注意与logoutRelayServer对应，服务器有网络掉线自动登出处理
        //  AppKey 和 AppSecret 是开发者
        // 在图鸭科技开发者网站(http://www.tucodec.com/)
        // 注册账号后获取的 AppKey 和 AppSecret
        int result = 0;
        result = mVoIPClient.loginRelayServer(ip, (short) port, teacherId, 0, 1548215110, 1525332973);
        Log.i("voip_android", String.valueOf(result));
        if (result == 0){
            Toast.makeText(this,"登录失败",Toast.LENGTH_SHORT);
        }
        /**
         * @param callback  TYVoipAndroid 对象
         * @param param    视频捕捉参数
         */
        //初始化本地摄像头
        mVideoCapture = new TYVoipVideoCapture(mVoIPClient,
                new TYVoipVideoCapture.CaptureParam(mVoIPClient.getConfig().videoWidth,
                        mVoIPClient.getConfig().videoHeight));

        /**
         * 打开本地视频捕捉设备
         * @param frontCamera 是否使用前置摄像头
         * @param config      VoIP 配置信息
         * @param parentView  用于包含
         * @return error code  0: 成功
         * -1: 相机无法启动
         * -2: 相机被禁用
         * -3: 打开相机失败
         * -4: 没有相机权限
         */
        mVideoCapture.start(mSwitchCam, mVoIPClient.getConfig(), (ViewGroup)mMainRender.getParent());

        /**
         * 注册音视频回调
         */
        mVoIPClient.registerVideoCallback(mVideoCallback);

        mVoIPClient.registerAudioCallback(mAudioCallback);

        /**
         * 初始化 VoIP 客户端
         * @param videoCapture video capture object used to open/close camera
         */
        mVoIPClient.initVoip(mVideoCapture);

        /**
         * @param videoBitRate 视频码率 512~2048 kb
         * @param audioBitRate 音频采样率 16000 固定值
         * @param userid userId
         */
        //通知转发服务器已准备好上传本地视频流以及下拉他人视频流，注意userId需要先登录
        mVoIPClient.startVoip(512, 16000,teacherId);
        /**
         * @param rotation 视频旋转角度，默认为0
         * 参数为0、1、2、3，传错认为传0
         * 分别对应0度、90度、180度、270度，用来调节横竖屏
         */
        mVideoCapture.setRotation(0);
        /**
         * @param gain 声音大小 0~100
         * 默认为50，可调节
         * 传错值默认传入50
         */
        mVoIPClient.setSpeakerGain(50);
        //打开关闭扬声器，注意需要在XML里写入权限
        setSpeakerphoneOn(true);
    }

    private void setSpeakerphoneOn(boolean on) {
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        if(on) {
            audioManager.setSpeakerphoneOn(true);
        } else {
            audioManager.setSpeakerphoneOn(false);//关闭扬声器
            audioManager.setRouting(AudioManager.MODE_NORMAL, AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            //把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    private TYVoipAndroid.AudioCallback mAudioCallback = new TYVoipAndroid.AudioCallback() {
        @Override
        public void audioNotification(int i) {
        }
    };

    private void putRender() {
        if (studentId != 0){
            mVoIPPeers.put(studentId, render1);
            render1.setVisibility(View.VISIBLE);
            //设置render1在最上方显示
            render1.setZOrderOnTop(true);
            //add对方userId，下拉对方视频流，注意：此时对端也要add你的节点，才能达到相互通信的目的，否则只能单方面接受数据
            //VideoCallback会回调渲染好的视频帧，渲染到对应Render上
            mVoIPClient.addClientNode(studentId);
            //注意：此Demo只演示了如何add节点，当对端结束通话，
            // 应当调用mVoIPClient.removeClientNode(other_userId)，通知转发服务器结束下拉该对端视频流；
            // 并做视图隐藏等对应操作；
        }
    }

    public void getValues() {
        Intent intent = getIntent();
        ip = intent.getStringExtra("ip");
        port = intent.getIntExtra("port", 0);
        teacherId = Integer.parseInt(intent.getStringExtra("teacherId"));
        studentId = Integer.parseInt(intent.getStringExtra("studentId"));
        Log.i("ip", "1:"+ip);
        Log.i("port", "2:"+port);
        Log.i("studentId", "3:"+studentId);
        Log.i("teacherId", "4:"+ teacherId);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RefWatcher refWatcher = TucodeC.getRefWatcher(this);
//        refWatcher.watch(this);
    }

}
