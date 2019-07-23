package com.jh.utils;

import android.app.Application;
import android.app.Service;
import android.media.AudioManager;
import android.os.Build;


/**
 * 功能：用于管理音频输出
 * 作者：yangtao
 * 创建时间：2016/3/16 09:06
 */
public class AudioUtils {
    private static AudioUtils instance;
    protected AudioManager audioManager;
    private boolean isMuteState = false;

    public static AudioUtils instance(Application application) {
        if (instance == null) {
            synchronized (AudioUtils.class) {
                if (instance == null) {
                    instance = new AudioUtils(application);
                }
            }
        }
        return instance;
    }

    public AudioUtils(Application application){
        audioManager = (AudioManager) application.getSystemService(Service.AUDIO_SERVICE);
    }

    // 打开扬声器
    public void openSpeakerOn() {
        try {
            if (!audioManager.isSpeakerphoneOn())
                audioManager.setSpeakerphoneOn(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 关闭扬声器
    public void closeSpeakerOn() {
        try {
            if (audioManager != null) {
                // int curVolume =
                // audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                if (audioManager.isSpeakerphoneOn())
                    audioManager.setSpeakerphoneOn(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
                }
                // audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                // curVolume, AudioManager.STREAM_VOICE_CALL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openMicOn() {
        if (!isMuteState) {
            // 打开静音
            audioManager.setMicrophoneMute(true);
            isMuteState = true;
        }
    }

    public void closeMicOn() {
        if (isMuteState) {
            // 关闭静音
            audioManager.setMicrophoneMute(false);
            isMuteState = false;
        }
    }

    //设置为正常模式
    public void resetModel(){
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setMicrophoneMute(false);
        isMuteState = false;
    }

    //正在呼叫
    public void callRing(){
        audioManager.setMode(AudioManager.MODE_RINGTONE);
        audioManager.setSpeakerphoneOn(false);
    }

    //正在被叫
    public void calledRing(){
        audioManager.setMode(AudioManager.MODE_RINGTONE);
        audioManager.setSpeakerphoneOn(true);
    }

    public int returnSituationalModel() {
        return audioManager.getRingerMode();
    }

    public boolean isMuteState() {
        return isMuteState;
    }

    public boolean isSpeakerState(){
        return audioManager.isSpeakerphoneOn();
    }
}
