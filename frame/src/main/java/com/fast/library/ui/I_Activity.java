package com.fast.library.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 说明：Activity接口协议
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/10/28 21:40
 * <p/>
 * 版本：verson 1.0
 */
public interface I_Activity {
    /*初始化*/
    void onInitCreate(Bundle bundle);
    /*初始化参数(onStart中调用)*/
    void onInitStart();
    /*点击事件回调方法*/
    void clickView(View view, int id);
    /*获取数据*/
    void getIntentData(Intent intent);
}
