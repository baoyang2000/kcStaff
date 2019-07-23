package com.jh.utils;

import android.content.Context;
import android.os.PowerManager;

/**
 * 功能：
 * 作者：yangtao
 * 创建时间：2016/5/16 10:08
 */
public class WakeLockUtil {
    // 电源管理器
    private PowerManager mPowerManager;
    // 唤醒锁
    private PowerManager.WakeLock mWakeLock;

    public WakeLockUtil(Context context) {
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    public void wakeUp(){
        // 点亮亮屏
        mWakeLock = mPowerManager.newWakeLock
                (PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "Tag");
        mWakeLock.acquire();
    }

    public void releaseWake(){
        if (mWakeLock != null) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }
}
