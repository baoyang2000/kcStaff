package com.jh.utils;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

/**
 * 功能：控制铃音震动
 * 作者：yangtao
 * 创建时间：2015/10/6 11:47
 */
public class RingUtils {

    private MediaPlayer mMediaPlayer;
    private Vibrator vibrator;
    private Context context;
    long[] pattern = {1000, 2000, 1000, 2000};  // 停止 开启 停止 开启

    private volatile static RingUtils singleton;
    private Uri mediaUri;

    private RingUtils(Context context) {
        this.context = context;
        mediaUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        /*
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         **/
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public static RingUtils getSingleton(Context context) {
        if (singleton == null) {
            singleton = new RingUtils(context);
        }
        return singleton;
    }

    /**
     * 根据用户的情景模式选择响铃类型
     */
    public void playRing(Application application) {

        if (null != vibrator && AudioUtils.instance(application).returnSituationalModel()
                != AudioManager.RINGER_MODE_SILENT) {
            vibrator.vibrate(pattern, 2);
        }

        if (AudioUtils.instance(application).returnSituationalModel() == AudioManager.RINGER_MODE_NORMAL) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;

                mMediaPlayer = MediaPlayer.create(context,
                        mediaUri);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
            } else {
                mMediaPlayer = MediaPlayer.create(context,
                        mediaUri);
                mMediaPlayer.setLooping(true);
                mMediaPlayer.start();
            }
        }
    }

    public void stopRing() {
        if (null != vibrator) {
            vibrator.cancel();
        }
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
