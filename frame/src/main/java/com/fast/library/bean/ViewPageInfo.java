package com.fast.library.bean;

import android.os.Bundle;

/**
 * 说明：ViewPageInfo
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 15:59
 * <p/>
 * 版本：verson 1.0
 */
public class ViewPageInfo {
    public final String title;
    public final Bundle params;
    public final Class<?> clazz;

    public ViewPageInfo(String title, Class<?> clazz, Bundle params){
        this.title = title;
        this.clazz = clazz;
        this.params = params;
    }
}
