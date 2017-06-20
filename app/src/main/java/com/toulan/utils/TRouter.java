package com.toulan.utils;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.toulan.ui.CommonActivity;
import com.toulan.ui.activity.LoginActivity;
import com.toulan.ui.service.TraceService;
import com.toulan.ui.service.UpdateService;

import static com.toulan91.R.string.url;

/**
 * 说明：路由器
 * <p>
 * 作者：fanly
 * <p>
 * 类型：Class
 * <p>
 * 时间：2016/9/13 10:10
 * <p>
 * 版本：verson 1.0
 */
public class TRouter {

    /**
     * 跳转登录
     * @param activity
     */
    public static void startLogin(@NonNull CommonActivity activity){
        activity.showActivity(LoginActivity.class);
    }

    /**
     * 启动更新下载服务
     * @param url
     */
    public static void startUpdateService(@NonNull CommonActivity activityCommon, @NonNull String url){
        Intent intent = new Intent(activityCommon, UpdateService.class);
        intent.putExtra(UpdateService.DOWN_LOAD_URL,url);
        activityCommon.startService(intent);
    }

    /**
     * 开启鹰眼
     * @param activityCommon
     */
    public static void startTraceService(@NonNull CommonActivity activityCommon){
        if (Tsp.isLogin()){
            Intent intent = new Intent(activityCommon, TraceService.class);
            intent.setAction("com.toulan.action_trace_service");
            activityCommon.startService(intent);
        }
    }

}
