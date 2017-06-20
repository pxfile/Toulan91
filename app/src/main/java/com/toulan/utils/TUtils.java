package com.toulan.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.fast.library.ui.ToastUtil;
import com.fast.library.utils.DateUtils;
import com.fast.library.utils.StringUtils;
import com.toulan.bean.OrderBean;

import java.util.Calendar;
import java.util.Locale;

/**
 * 说明：工具类
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/13 9:21
 * <p/>
 * 版本：verson 1.0
 */
public class TUtils {

    public static String formatPhone(String mobile){
        StringBuilder stringBuilder = new StringBuilder();
        if (!StringUtils.isEmpty(mobile)){
            String temp = mobile.replace(" ","");
            for (int i = 0;i < temp.length();i++){
                if (i == 3){
                    stringBuilder.append(" ");
                }else if (i == 7){
                    stringBuilder.append(" ");
                }
                stringBuilder.append(temp.charAt(i));
            }
        }
        return stringBuilder.toString();
    }

    public static String convertWorkingTime(int second){
        StringBuilder sb = new StringBuilder();
        if (second <= 0){
            sb.append("0秒");
        }else {
            int hour = second / 3600;
            int min = (second - hour * 3600) / 60;
            int sec = second - min * 60 - hour * 3600;
            if (hour != 0){
                sb.append(hour).append("小时");
            }
            if (min != 0){
                sb.append(min).append("分钟");
            }
            if (sec != 0){
                sb.append(sec).append("秒");
            }
        }
        return sb.toString();
    }

    public static String convertCountDownTime(int second){
        StringBuilder sb = new StringBuilder();
        if (second <= 0){
            sb.append("0s");
        }else {
            int hour = second / 3600;
            int min = (second - hour * 3600) / 60;
            int sec = second - min * 60 - hour * 3600;
            if (hour != 0){
                sb.append(hour).append("h");
            }
            if (min != 0){
                sb.append(min).append("m");
            }
            if (sec != 0){
                sb.append(sec).append("s");
            }
        }
        return sb.toString();
    }

    public static void startMap(Activity activity,String lat, String lng){
        try {
            Uri uri = Uri.parse(String.format("geo:%1$s,%2$s",lat,lng));
            Intent it  = new Intent(Intent.ACTION_VIEW,uri);
            activity.startActivity(it);
        }catch (Exception e){
            e.printStackTrace();
            ToastUtil.get().shortToast("该手机没有安装地图应用！");
        }

    }

    public static long getTime(){
        return ServerTimeUtils.getInstance().getMillSeconds();
    }

    /**
     * 超时
     * @param bean
     * @return
     */
    public static boolean orderTimerOverTime(OrderBean bean){
        String []temp1 = bean.getOrderDetail().getServiceTime().split(" ");
        if (temp1.length >= 1){
            String []temp2 = temp1[1].split("-");
            String serverTime = temp2[1];
            long oneSecond = DateUtils.getStrToLong(ServerTimeUtils.getInstance().getTime(DateUtils.FORMAT_HH_MM_1),DateUtils.FORMAT_HH_MM_1);
            long twoSecond = DateUtils.getStrToLong(serverTime,DateUtils.FORMAT_HH_MM_1);
            if (oneSecond >= twoSecond){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 来太快了
     * @param bean
     * @return
     */
    public static boolean orderTimerComeSoon(OrderBean bean){
        int min = 120;
        String []temp1 = bean.getOrderDetail().getServiceTime().split(" ");
        if (temp1.length >= 1){
            String []temp2 = temp1[1].split("-");
            String serverTime = temp2[0];
            if (DateUtils.getSecondSpace(ServerTimeUtils.getInstance().getTime(DateUtils.FORMAT_HH_MM_1),serverTime,DateUtils.FORMAT_HH_MM_1) > min*60){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public static int[] getStartLocationTime(){
        int []times = new int[2];
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.setTimeInMillis(ServerTimeUtils.getInstance().getMillSeconds());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        if (min == 0){
            times[0] = hour;
            times[1] = min;
        }else if (min > 0 && min <= 15){
            times[0] = hour;
            times[1] = 15;
        }else if (min > 15 && min <= 30){
            times[0] = hour;
            times[1] = 30;
        }else if (min > 30 && min <= 45){
            times[0] = hour;
            times[1] = 45;
        }else {
            times[0] = hour+1;
            if (times[0] == 24){
                times[0] = 0;
            }
            times[1] = 0;
        }
        return times;
    }
}
