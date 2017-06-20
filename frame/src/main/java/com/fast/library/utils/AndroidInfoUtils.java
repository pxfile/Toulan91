package com.fast.library.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.fast.library.FastFrame;
import java.io.File;

/**
 * 说明：手机信息相关工具类
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/10/26 23:57
 * <p/>
 * 版本：verson 1.0
 */

public final class AndroidInfoUtils {

    private AndroidInfoUtils() {}

    /**
     * 说明：获取手机IMEI号码
     *
     * @return 返回手机IMEI号码
     */
    public static String getImeiCode() {
        String result = "";
        try {
            final TelephonyManager tm = (TelephonyManager) FastFrame.getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            result = tm.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 说明：获取手机IMSI号码
     *
     * @return 返回手机IMEI号码
     */
    public static String getImsiCode() {
        String result = "";
        try {
            final TelephonyManager tm = (TelephonyManager) FastFrame.getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            result = tm.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取手机Android_ID
     *
     * @return MacAddress String
     */
    public static String getAndroidId() {
        String androidId = Secure.getString(FastFrame.getApplication().getContentResolver(),
                Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * 说明：获取本机手机号码
     *
     * @return 返回本机手机号码
     */
    public static String getMobilNumber() {
        String result = "";
        try {
            final TelephonyManager tm = (TelephonyManager) FastFrame.getApplication()
                    .getSystemService(Context.TELEPHONY_SERVICE);
            result = tm.getLine1Number();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 说明：myPid
     * @return
     */
    public static int myPid(){
        return android.os.Process.myPid();
    }

    /**
     * 说明：获取系统信息
     *
     * @return
     */
    public static String getOs() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 说明：获取手机MAC地址
     *
     * @return 返回手机MAC地址
     */
    public static String getMacAddress() {
        String res = "";
        try {
            final WifiManager wifiManager = (WifiManager) FastFrame.getApplication()
                    .getSystemService(Context.WIFI_SERVICE);
            final WifiInfo info = wifiManager.getConnectionInfo();
            if (null != info) {
                res = info.getMacAddress();
            }
            if (TextUtils.isEmpty(res)) {
                res = "00:00:00:00:00:00";
            }
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * 说明：获取当前应用程序的VersionName
     *
     *            当前上下文环境
     * @return 返回当前应用的版本号
     */
    public static String versionName() {
        try {
            PackageInfo info = FastFrame.getApplication().getPackageManager().getPackageInfo(
                    FastFrame.getApplication().getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 说明：获取当前应用程序的VersionCode
     *
     * @return 返回当前应用的版本号
     */
    public static int versionCode() {
        try {
            PackageInfo info = FastFrame.getApplication().getPackageManager().getPackageInfo(
                    FastFrame.getApplication().getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 说明：检测手机空间可用大小 get the space is left over on phone self
     */
    public static long getRealSizeOnPhone() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        @SuppressWarnings("deprecation")
        long blockSize = stat.getBlockSize();
        @SuppressWarnings("deprecation")
        long availableBlocks = stat.getAvailableBlocks();
        long realSize = blockSize * availableBlocks;
        return realSize;
    }

    /**
     * 说明：获取设备终端码
     *
     * @return
     */
    public static String getTerminalCode() {
        try {
            return  MD5.getMD5(getImeiCode() + getImsiCode() + getAndroidId());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取设备的可用内存大小
     *
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory() {
        ActivityManager am = (ActivityManager) FastFrame.getApplication()
                .getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo mi = new MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }

    /**
     * 说明：获取当前线程名称
     * @return
     */
    public static String getCurProcessName(){
        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) FastFrame.getApplication()
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
