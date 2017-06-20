package com.fast.library.ui;

/**
 * 说明：规范Activity中服务注册的接口协议
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/10/28 21:49
 * <p/>
 * 版本：verson 1.0
 */
public interface I_Service {
    /**
     * 注册广播
     */
    void registerService();

    /**
     * 解除注册广播
     */
    void unRegisterService();
}
