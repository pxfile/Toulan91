package com.toulan.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.toulan.ui.service.TLocationService;

/**
 * 说明：LoongggAlarmReceiver
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/30 10:41
 * <p/>
 * 版本：verson 1.0
 */
public class LoongggAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int intervalSecond = intent.getIntExtra(AlarmManagerUtils.INTERVAL, 0);
        if (intervalSecond != 0) {
            AlarmManagerUtils.setAlarmTime(context, System.currentTimeMillis() + intervalSecond, intent);
        }
        Intent clockIntent = new Intent(context, TLocationService.class);
        clockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(clockIntent);
    }

}
