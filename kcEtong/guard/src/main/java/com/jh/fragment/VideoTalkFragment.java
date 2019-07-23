package com.jh.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.R;
import com.jh.utils.AudioUtils;
import com.jhsdk.bean.sip.JHCallInfo;
import com.jhsdk.bean.sip.JHVideoSize;
import com.jhsdk.constant.JHConstant;
import com.jhsdk.core.JHSDKCore;
import com.jhsdk.core.callback.JHSDKListener;
import com.jhsdk.media.JHMediaEngine;
import com.jhsdk.utils.AccountUtils;

import org.webrtc.videoengine.ViEAndroidGLES20;

import java.lang.ref.WeakReference;
import java.util.Calendar;

public class VideoTalkFragment extends Fragment implements View.OnClickListener,
        JHSDKListener.VideoChangedListener {
    //private boolean usingFrontCamera = true;
    private SurfaceView remoteRender;
    private LinearLayout lLayoutUnlock;
    private ImageView speaker_btn, mic_btn, btn_answer;
    private View view_interval;

    private Chronometer mCallTime;
    private ProgressBar pBarLoading;

    private DisplayMetrics dm;

    private long lastClickTimePhoto = 0;
    private long lastClickTimeUnlock = 0;
    private long lastClickTimeAnswer = 0;

    private final int CALL_CONNECT_KEY = 1;
    private final int VIDEO_SIZE_CHANGED_KEY = 2;
    private JHCallInfo callInfo;

    private Handler handler = new ServiceHandler(this);

    @SuppressLint("HandlerLeak")
    private class ServiceHandler extends Handler {
        WeakReference<VideoTalkFragment> s;

        public ServiceHandler(VideoTalkFragment videoTalkFragment) {
            s = new WeakReference<>(videoTalkFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VideoTalkFragment videoTalkFragment = s.get();
            if (videoTalkFragment == null) {
                return;
            }
            switch (msg.what) {
                case CALL_CONNECT_KEY:
                    if (AccountUtils.isOotdoorDevice(callInfo)) {
                        lLayoutUnlock.setVisibility(View.VISIBLE);
                    }
                    view_interval.setVisibility(View.GONE);
                    btn_answer.setVisibility(View.GONE);
                    speaker_btn.setVisibility(View.VISIBLE);
                    mic_btn.setVisibility(View.VISIBLE);
                    pBarLoading.setVisibility(View.GONE);
                    startCountTime();
                    break;
                case VIDEO_SIZE_CHANGED_KEY:
                    resize(msg.arg1, msg.arg2);
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_screen, null);
        JHSDKCore.addListener(this);
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        init(view);
        return view;
    }

    @Override
    public void onVideoStateChanged(JHVideoSize videoSize) {
        handler.obtainMessage(VIDEO_SIZE_CHANGED_KEY, videoSize.getWidth(), videoSize.getHeight()).sendToTarget();
    }

    private void init(View view) {
        Bundle bundle = getArguments();
        callInfo = (JHCallInfo) bundle.getSerializable("JHCallInfo");
        pBarLoading = (ProgressBar) view.findViewById(R.id.pBarLoading);

        ImageView mHangup = (ImageView) view.findViewById(R.id.hangup);
        mHangup.setOnClickListener(this);
        lLayoutUnlock = (LinearLayout) view.findViewById(R.id.lLayoutUnlock);
        ImageView mUnlock = (ImageView) view.findViewById(R.id.unlock);
        mUnlock.setOnClickListener(this);
        ImageView photo = (ImageView) view.findViewById(R.id.photo);
        photo.setOnClickListener(this);
        remoteRender = (SurfaceView) view.findViewById(R.id.remoteRender);
        resize(320, 240);
        TextView tViewUsername = (TextView) view.findViewById(R.id.tViewUsername);
        TextView tViewAccount = (TextView) view.findViewById(R.id.tViewAccount);
        JHMediaEngine.instance().SetupVideoChannel();

        if (AccountUtils.isOotdoorDevice(callInfo)) {
            JHMediaEngine.instance().StartVideoChannel(remoteRender, false);
        } else {
            JHMediaEngine.instance().StartVideoChannel(remoteRender, true);
        }
        JHMediaEngine.instance().SetCameraOutputRotation(getActivity());

        tViewAccount.setText(callInfo.getAccount());
        tViewUsername.setText(callInfo.getDisplayName());

        btn_answer = (ImageView) view.findViewById(R.id.btn_answer);
        btn_answer.setOnClickListener(this);
        view_interval = view.findViewById(R.id.view_interval);

        speaker_btn = (ImageView) view.findViewById(R.id.speaker_btn);
        speaker_btn.setOnClickListener(this);
        mic_btn = (ImageView) view.findViewById(R.id.mic_btn);
        mic_btn.setOnClickListener(this);

        if (!AudioUtils.instance(getActivity().getApplication()).isSpeakerState()) {
            AudioUtils.instance(getActivity().getApplication()).openSpeakerOn();
        }

        speaker_btn.setSelected(AudioUtils.instance(getActivity().getApplication()).isSpeakerState());
        //btn_mic.setSelected(!AudioUtils.instance().isMuteState());

        mCallTime = (Chronometer) view.findViewById(R.id.calling_time);
        mCallTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String time = chronometer.getText().toString();
                if ("00:30".equals(time) && AccountUtils.isMonitor(callInfo)) {
                    JHSDKCore.getCallService().hangup();
                }
            }
        });

        // 根据来电或去电显示不同的界面
        if (callInfo.getCallState() == JHConstant.RECEIVE_VIDEO_REQUEST) {
            view_interval.setVisibility(View.INVISIBLE);
            btn_answer.setVisibility(View.VISIBLE);

        } else if (callInfo.getCallState() == JHConstant.INVITE_VIDEO_REQUEST) {
            JHMediaEngine.instance().StartVoiceChannel();
            view_interval.setVisibility(View.GONE);
            btn_answer.setVisibility(View.GONE);
            if (!AccountUtils.isMonitor(callInfo)) {
                speaker_btn.setVisibility(View.VISIBLE);
                mic_btn.setVisibility(View.VISIBLE);
            }
            pBarLoading.setVisibility(View.GONE);
            startCountTime();
        }
    }

    //重新计算视频
    public void resize(int videoSizeWidth, int voideSizeHeight) {
        int width = dm.widthPixels;//宽度
        int height = dm.heightPixels;//高度
        if (height >= width) {
            height = (int) Math.round((voideSizeHeight * width * 1.0 / videoSizeWidth));
        } else {
            width = (int) Math.round((videoSizeWidth * height * 1.0 / voideSizeHeight));
        }
        RelativeLayout.LayoutParams cameraFL = new RelativeLayout.LayoutParams(width, height); // set size
        cameraFL.addRule(RelativeLayout.CENTER_IN_PARENT);
        remoteRender.setLayoutParams(cameraFL);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int MIN_CLICK_DELAY_TIME = 3000;
        if (id == R.id.hangup) {
            JHSDKCore.getCallService().hangup();
            getActivity().finish();
        } else if (id == R.id.btn_answer) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTimeAnswer > MIN_CLICK_DELAY_TIME) {
                lastClickTimeAnswer = currentTime;
                JHSDKCore.getCallService().acceptCall();
            }
        } else if (id == R.id.unlock) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTimeUnlock > MIN_CLICK_DELAY_TIME) {
                lastClickTimeUnlock = currentTime;
                JHSDKCore.getCallService().sendDtmf("*");
            } else {
                Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.please_not_click)
                        , Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else if (id == R.id.photo) {
            long currentTime = Calendar.getInstance().getTimeInMillis();
            if (currentTime - lastClickTimePhoto > MIN_CLICK_DELAY_TIME) {
                lastClickTimePhoto = currentTime;
                ViEAndroidGLES20.photosign = true;
            } else {
                Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.please_not_click)
                        , Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        } else if (id == R.id.speaker_btn) {
            if (AudioUtils.instance(getActivity().getApplication()).isSpeakerState()) {
                AudioUtils.instance(getActivity().getApplication()).closeSpeakerOn();
            } else {
                AudioUtils.instance(getActivity().getApplication()).openSpeakerOn();
            }
            speaker_btn.setSelected(AudioUtils.instance(getActivity().getApplication()).isSpeakerState());
        } else if (id == R.id.mic_btn) {
            if (AudioUtils.instance(getActivity().getApplication()).isMuteState()) {
                AudioUtils.instance(getActivity().getApplication()).closeMicOn();
            } else {
                AudioUtils.instance(getActivity().getApplication()).openMicOn();
            }
            mic_btn.setSelected(AudioUtils.instance(getActivity().getApplication()).isMuteState());
        }
    }

    public void stopVideoChannel() {
        JHMediaEngine.instance().StopVideoChannel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        remoteRender = null;
        stopVideoChannel();
        endCountTime();
        JHSDKCore.removeListener(this);
    }

    public void startVoiceCallScreen() {
        handler.sendEmptyMessage(CALL_CONNECT_KEY);
        JHMediaEngine.instance().StartVoiceChannel();
    }

    //显示时间
    public void startCountTime() {
        if (mCallTime == null) {
            throw new IllegalArgumentException("no callee_duration view found");
        }
        mCallTime.setVisibility(View.VISIBLE);
        mCallTime.setBase(SystemClock.elapsedRealtime());
        mCallTime.start();
    }

    public void endCountTime() {
        if (mCallTime != null) {
            mCallTime.setBase(SystemClock.elapsedRealtime());
        }
    }
}
