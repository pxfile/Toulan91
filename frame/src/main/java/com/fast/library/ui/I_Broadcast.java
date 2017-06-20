package com.fast.library.ui;

/**
 * 说明：规范Activity中广播接受者注册的接口协议
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/10/28 21:49
 * <p/>
 * 版本：verson 1.0
 */
public interface I_Broadcast {
    /**
     * 注册广播
     */
    void registerBroadcast();

    /**
     * 解除注册广播
     */
    void unRegisterBroadcast();
}
