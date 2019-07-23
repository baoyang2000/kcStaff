package com.jh.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jh.R;
import com.jh.fragment.VideoTalkFragment;
import com.jh.fragment.VideoWaitFragment;
import com.jh.utils.AudioUtils;
import com.jh.utils.RingUtils;
import com.jh.utils.StringUtils;
import com.jh.utils.WakeLockUtil;
import com.jhsdk.bean.sip.BaseJson;
import com.jhsdk.bean.sip.JHCallInfo;
import com.jhsdk.bean.sip.JHMessage;
import com.jhsdk.constant.JHConstant;

import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHSDKListener;
import com.jhsdk.media.JHMediaEngine;
import com.jhsdk.permissions.PermissionsManager;
import com.jhsdk.utils.AccountUtils;
import com.jhsdk.utils.JHReturnCode;

import java.lang.ref.WeakReference;

public class VideoScreenActivity extends AppCompatActivity implements JHMediaEngine.MediaStateListener,
        JHSDKListener.CallStateChangedListener, JHSDKListener.MessageReceivedListener {
    private final String VIDEOWAITE = "VIDEOWAITE";
    private final String VOICE_OPEN = "VOICE_OPEN";
    private final int CALL_TIMEOUT = 1;
    private final int TIPS_KEY = 2;

    private FragmentManager fm;
    VideoWaitFragment videoWaitFragment = null;
    private WakeLockUtil wakeLockUtil;
    private JHCallInfo callInfo;

    private Handler handler = new ServiceHandler(this);
    @Override
    public void onMessageReceived(final JHMessage message) {
        try {
            BaseJson baseJson = new BaseJson(message.getContent());
            if ("call_sync".equals(baseJson.getType())) {
                if (callInfo.getAccount().equals(message.getAccount())) {
                    JHSDKCore.getCallService().hangup();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JHSDKCore.getCallService().makeCall(message.getAccount());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000);
                } else {
                    JHSDKCore.getMessageService().sendMessage(message.getAccount(),
                            JSON.toJSONString(new BaseJson("error", -1, "call_sync")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("HandlerLeak")
    private class ServiceHandler extends Handler {
        WeakReference<VideoScreenActivity> s;

        public ServiceHandler(VideoScreenActivity videoScreenActivity) {
            s = new WeakReference<>(videoScreenActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VideoScreenActivity videoScreenActivity = s.get();
            if (videoScreenActivity != null) {
                if (msg.what == JHConstant.INVITE_VIDEO_REQUEST) {
                    VideoTalkFragment videoTalkFragment = new VideoTalkFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("JHCallInfo", callInfo);
                    videoTalkFragment.setArguments(bundle);
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.layout_fl, videoTalkFragment, VOICE_OPEN);
                    ft.commitAllowingStateLoss();
                } else if (msg.what == CALL_TIMEOUT) {
                    JHSDKCore.getCallService().hangup();
                    finish();
                } else if (msg.what == TIPS_KEY) {
                    String tips = (String) msg.obj;
                    Toast.makeText(VideoScreenActivity.this, tips, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JHSDKCore.addListener(this);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_video_guard);
        fm = getSupportFragmentManager();
        callInfo = (JHCallInfo) getIntent().getSerializableExtra("JHCallInfo");
        wakeLockUtil = new WakeLockUtil(VideoScreenActivity.this);
        wakeLockUtil.wakeUp();
        //android6.0录音权限
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.RECORD_AUDIO}, null);
        JHMediaEngine.instance().setMediaStateListener(this);

        handler.sendEmptyMessageDelayed(CALL_TIMEOUT, 60 * 1000);
        if (callInfo.getCallState() == JHConstant.RECEIVE_VIDEO_REQUEST) {
            RingUtils.getSingleton(getApplicationContext()).playRing(getApplication());
            AudioUtils.instance(getApplication()).calledRing();
            VideoTalkFragment videoTalkFragment = new VideoTalkFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("JHCallInfo", callInfo);
            videoTalkFragment.setArguments(bundle);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.layout_fl, videoTalkFragment, VOICE_OPEN);
            ft.commit();
        } else {
            AudioUtils.instance(getApplication()).callRing();
            videoWaitFragment = new VideoWaitFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("JHCallInfo", callInfo);
            videoWaitFragment.setArguments(bundle);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.layout_fl, videoWaitFragment, VIDEOWAITE);
            ft.commit();
        }

        if (StringUtils.isEmpty(JHSDKCore.getCallService().getCallInfo().getAccount())){
            startActivity(new Intent(VideoScreenActivity.this, LauncherActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeLockUtil.releaseWake();
        handler.removeMessages(CALL_TIMEOUT);
        AudioUtils.instance(getApplication()).resetModel();
        RingUtils.getSingleton(getApplicationContext()).stopRing();
        JHSDKCore.removeListener(this);
    }

    /**
     * 开启语音
     */
    private void startVoiceCallScreen() {
        FragmentManager fm = getSupportFragmentManager();
        VideoTalkFragment videoTalkFragment = (VideoTalkFragment) fm
                .findFragmentByTag(VOICE_OPEN);
        if (videoTalkFragment != null) {
            videoTalkFragment.startVoiceCallScreen();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onMediaStateListener(int code, String reason) {
        if (code == 1){
            Toast.makeText(VideoScreenActivity.this, "程序被禁止启用摄像头功能！", Toast.LENGTH_LONG).show();
        }else if (code == 2){
            Toast.makeText(VideoScreenActivity.this, "程序被禁止启用录音功能！", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCallStateChanged(JHCallInfo callInfo) {
        if (callInfo.getCallState() == JHConstant.CALL_CONNECTED) {
            RingUtils.getSingleton(getApplicationContext()).stopRing();
            handler.removeMessages(CALL_TIMEOUT);
            if (this.callInfo.getCallState() == JHConstant.INVITE_VIDEO_REQUEST) {
                handler.sendEmptyMessage(JHConstant.INVITE_VIDEO_REQUEST);
            } else {
                startVoiceCallScreen();
            }
        } else if (callInfo.getCallState() == JHConstant.CALL_DISCONNECT) {
            int retCode = callInfo.getCallResult();
            if (this.callInfo.getCallState() == JHConstant.RECEIVE_VIDEO_REQUEST) {
                finish();
            } else if ((AccountUtils.isMonitor(this.callInfo)) && retCode != 200) {
                handler.obtainMessage(TIPS_KEY, JHReturnCode.showCallReqStatus(retCode, VideoScreenActivity.this)).sendToTarget();
                finish();
            } else if (retCode == 603 || retCode == 200 || retCode == 500) {
                finish();
            }
        }
    }
}
