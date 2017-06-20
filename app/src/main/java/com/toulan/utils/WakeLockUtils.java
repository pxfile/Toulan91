package com.toulan.utils;

import android.content.Context;
import android.os.PowerManager;

/**
 * 说明：唤醒锁工具类
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/29 14:36
 * <p/>
 * 版本：verson 1.0
 */
public class WakeLockUtils {

    private PowerManager.WakeLock wakeLock = null;

    /**
     * 获取电源锁，保持该服务在屏幕熄灭时仍获取CPU，保持运行
     */
    public void acquirWakeLock(Context context, String tag) {
        if (wakeLock == null) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, tag);
            if (null != wakeLock) {
                wakeLock.acquire();
            }
        }
    }

    public void releaseWakeLock() {
        if (null != wakeLock) {
            wakeLock.release();
            wakeLock = null;
        }
    }

}
