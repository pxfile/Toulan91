package com.fast.mvp.presenter;

import com.fast.mvp.view.MvpView;

/**
 * 说明：MvpPresenter
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/6/15 16:54
 * <p/>
 * 版本：verson 1.0
 */
public interface MvpPresenter<VH extends MvpView> {
    void attachView(VH mvpView);
    void onStart();
    void detachView();
    VH getMvpView();
    boolean isViewAttached();
    void checkViewAttached();
}
