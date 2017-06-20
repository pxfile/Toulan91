package com.fast.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import com.fast.library.http.HttpTaskKey;
import com.fast.library.ui.AbstractActivity;
import com.fast.library.ui.ToastUtil;
import com.fast.mvp.presenter.MvpPresenter;

/**
 * 说明：Activity基类
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/12/26 11:29
 * <p/>
 * 版本：verson 1.0
 */
public abstract class BaseActivity<Presenter extends MvpPresenter> extends AbstractActivity implements HttpTaskKey,LoaderManager.LoaderCallbacks<Presenter> {

    private Presenter mPresenter;

    protected final String HTTP_TASK_KEY = "key_"+hashCode();
    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public void getIntentData(Intent intent) {
    }

    /***************************************************************************************/

    public void shortToast(int res){
        ToastUtil.get().shortToast(res);
    }
    public void shortToast(String res){
        ToastUtil.get().shortToast(res);
    }
    public void longToast(String res){
        ToastUtil.get().longToast(res);
    }
    public void longToast(int res){
        ToastUtil.get().longToast(res);
    }
    public void cancelToast(){
        ToastUtil.get().cancelToast();
    }

    /***************************************************************************************/

    @Override
    public void clickView(View v, int id) {

    }

    /***************************************************************************************/

    public abstract int createLoaderID();

    public Presenter getPresenter(){
        return mPresenter;
    }

    @Override
    public void onLoadFinished(Loader<Presenter> loader, Presenter data) {
        mPresenter = data;
    }

    @Override
    public void onLoaderReset(Loader<Presenter> loader) {
        mPresenter = null;
    }
    /***************************************************************************************/

}
