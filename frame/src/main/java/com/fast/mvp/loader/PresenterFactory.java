package com.fast.mvp.loader;

import com.fast.mvp.presenter.MvpPresenter;

/**
 * 说明：PresenterFactory
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Interface
 * <p/>
 * 时间：2016/5/30 14:12
 * <p/>
 * 版本：verson 1.0
 */
public interface PresenterFactory<T extends MvpPresenter> {
    T create();
}
