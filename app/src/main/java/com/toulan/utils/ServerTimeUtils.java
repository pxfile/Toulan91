package com.toulan.utils;

import android.os.SystemClock;

import com.fast.library.utils.DateUtils;
import com.fast.library.utils.StringUtils;

/**
 * 说明：服务器时间
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/29 15:36
 * <p/>
 * 版本：verson 1.0
 */
public class ServerTimeUtils {

    private static ServerTimeUtils serverTimeUtils = new ServerTimeUtils();

    private boolean isInit = false;

    private static String KEY_CURRENT_TIME = "KEY_CURRENT_TIME";
    private static String KEY_FIRST_BOOT_TIME = "KEY_FIRST_BOOT_TIME";

    private ServerTimeUtils(){}

    public static ServerTimeUtils getInstance(){
        return serverTimeUtils;
    }

    public boolean isInit(){
        return Tsp.sp.readLong(KEY_CURRENT_TIME,0) != 0;
    }

    public void init(String serverTime, String format){
        long currentTime = DateUtils.getStrToLong(serverTime,format);
        long firstBootTime = SystemClock.elapsedRealtime();
        Tsp.sp.write(KEY_CURRENT_TIME,currentTime);
        Tsp.sp.write(KEY_FIRST_BOOT_TIME,firstBootTime);
        isInit = true;
    }

    public String getTime(String format){
        String time = "";
        try {
            time =  DateUtils.getLongToStr(getMillSeconds(),format);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (StringUtils.isEmpty(time)){
                time = DateUtils.getNowTime(format);
            }
        }
        return time;
    }

    public long getMillSeconds(){
        if (isInit){
            long currentTime = Tsp.sp.readLong(KEY_CURRENT_TIME);
            long first = Tsp.sp.readLong(KEY_FIRST_BOOT_TIME);
            long second = SystemClock.elapsedRealtime();
            return currentTime + (second - first);
        }else {
            return DateUtils.getMillisecond();
        }
    }

}
