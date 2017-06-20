package com.fast.library.http.callback;

import java.io.File;

/**
 * 说明：下载文件回调
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/4/13 0:37
 * <p/>
 * 版本：verson 1.0
 */
public abstract class DownloadCallBack {

    /**
     * 说明：下载开始调用
     */
    public void onStart(){}

    /**
     * 说明：下载成功调用
     */
    public abstract void onSuccess(File target);

    /**
     * 说明：下载失败调用
     */
    public abstract void onFailure();

    /**
     * 说明：下载中调用
     * @param progress
     * @param networkSpeed
     */
    public void onProgress(float progress, long networkSpeed){}

}
