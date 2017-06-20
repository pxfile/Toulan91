package com.toulan.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

/**
 * 说明：AlarmManagerUtils
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/30 10:39
 * <p/>
 * 版本：verson 1.0
 */
public class AlarmManagerUtils {

    public static final String ALARM_ACTION = "com.toulan91.location.receiver";
    public static final String INTERVAL = "interval";
    public static final String ALARM_ID = "alarm_id";

    public static void setAlarmTime(Context context, long timeInMillis, Intent intent) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(context, intent.getIntExtra(ALARM_ID, 0),
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int interval = intent.getIntExtra(INTERVAL, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender);
        }
    }

    public static void cancelAlarm(Context context, int id) {
        Intent intent = new Intent(ALARM_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }

    /**
     * @param hour            时
     * @param minute          分
     * @param id              闹钟的id
     * @param intervalMillis  重复时间
     */
    public static void setAlarm(Context context,int hour, int minute, int id,int intervalMillis) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get
                (Calendar.DAY_OF_MONTH), hour, minute, 0);
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra(INTERVAL, intervalMillis);
        intent.putExtra(ALARM_ID, id);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setWindow(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    intervalMillis, sender);
        } else {
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, sender);
        }
    }


}
