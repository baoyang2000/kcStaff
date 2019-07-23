package com.jh.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jh.R;
import com.jh.view.RippleBackground;
import com.jhsdk.bean.sip.JHCallInfo;
import com.jhsdk.constant.JHConstant;
import com.jhsdk.core.JHSDKCore;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VideoWaitFragment extends Fragment {

    private ImageView foundDevice;

    private LinearLayout layoutAnswer;
    private LinearLayout layoutClose;
    boolean inVideoTalking = false;
    VideoTalkFragment videoTalkFragment = null;

    private Timer timer = new Timer();
    private TimerTask mTimerTask;
    private RippleBackground rippleBackground;

    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        private final WeakReference<VideoWaitFragment> weakReference;

        public MyHandler(VideoWaitFragment videoWaitFragment) {
            weakReference = new WeakReference<>(videoWaitFragment);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            VideoWaitFragment videoWaitFragment = weakReference.get();
            if (videoWaitFragment != null) {
                foundDevice();
            }
        }
    }

    private final MyHandler mHandler = new MyHandler(this);

    public boolean inVideoTalking() {
        return inVideoTalking;
    }

    public VideoTalkFragment getVideoTalkFragment() {
        return videoTalkFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_videowait_guard, null);
        init(view);
        return view;
    }

    private void init(View view) {
        layoutAnswer = (LinearLayout) view.findViewById(R.id.layout_answer);
        layoutClose = (LinearLayout) view.findViewById(R.id.layout_close);
        layoutAnswer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goToVideoTalk();
            }
        });
        TextView tViewAccount = (TextView) view.findViewById(R.id.tViewAccount);
        TextView tViewName = (TextView) view.findViewById(R.id.tViewName);

        View vDivide = view.findViewById(R.id.v_divide);

        ImageView btnAnswer = (ImageView) view.findViewById(R.id.btn_answer);
        ImageView btnClose = (ImageView) view.findViewById(R.id.btn_close);
        btnAnswer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                goToVideoTalk();
            }
        });

        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                JHSDKCore.getCallService().hangup();
                getActivity().finish();
            }
        });


        Bundle bundle = getArguments();
        JHCallInfo callInfo = (JHCallInfo) bundle.getSerializable("JHCallInfo");

        tViewAccount.setText(callInfo.getAccount());
        tViewName.setText(callInfo.getDisplayName());
        // 根据来电或去电显示不同的界面
        if (callInfo.getCallState() == JHConstant.RECEIVE_VIDEO_REQUEST) {
            layoutAnswer.setVisibility(View.VISIBLE);
            vDivide.setVisibility(View.VISIBLE);
            layoutClose.setVisibility(View.VISIBLE);
        } else if (callInfo.getCallState() == JHConstant.INVITE_VIDEO_REQUEST) {
            layoutAnswer.setVisibility(View.GONE);
            vDivide.setVisibility(View.GONE);
            layoutClose.setVisibility(View.VISIBLE);
        }

        rippleBackground = (RippleBackground) view.findViewById(R.id.content);
        rippleBackground.startRippleAnimation();
        foundDevice = (ImageView) view.findViewById(R.id.foundDevice);
        startTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (rippleBackground != null) {
            rippleBackground.stopRippleAnimation();
            rippleBackground = null;
        }
        stopTimer();
    }

    /**
     * 显示视频电话
     */
    public void goToVideoTalk() {
        JHSDKCore.getCallService().acceptCall();
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.sendEmptyMessage(1);
                }
            };
        }
        if (timer != null)
            timer.schedule(mTimerTask, 1000, 3000);

    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void foundDevice() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(400);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList = new ArrayList<>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevice.setVisibility(View.VISIBLE);
        animatorSet.start();
    }

}
