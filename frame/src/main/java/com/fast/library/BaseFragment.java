package com.fast.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.library.http.HttpTaskKey;
import com.fast.library.ui.SupportFragment;
import com.fast.library.ui.ToastUtil;

/**
 * 说明：Fragment基类(V4)
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/3/26 1:23
 * <p/>
 * 版本：verson 1.0
 */
public abstract class BaseFragment extends SupportFragment implements HttpTaskKey {

    private FragmentActivity mFragmentActivity;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mFragmentActivity = getActivity();
        return inflater.inflate(getRootViewResID(),null);
    }

    @Override
    public String getHttpTaskKey() {
        return "key"+hashCode();
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



    /***************************************************************************************/

    public void skipActivity(Class<?> cls) {
        skipActivity(cls);
        mFragmentActivity.finish();
    }

    public void skipActivity(Intent intent) {
        skipActivity(intent);
        mFragmentActivity.finish();
    }

    public void skipActivity(Class<?> cls, Bundle bundle) {
        skipActivity(cls, bundle);
        mFragmentActivity.finish();
    }

    public void showActivity(Class<?> cls) {
        Intent intent = new Intent(mFragmentActivity,cls);
        mFragmentActivity.startActivity(intent);
    }

    public void showActivity(Intent intent) {
        mFragmentActivity.startActivity(intent);
    }

    public void showActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mFragmentActivity,cls);
        intent.putExtras(bundle);
        mFragmentActivity.startActivity(intent);
    }

    /***************************************************************************************/

    /***************************************************************************************/

    public FragmentManager getSupportFragmentManager() {
        if (mFragmentActivity != null){
            return mFragmentActivity.getSupportFragmentManager();
        }else {
            return null;
        }
    }

    /***************************************************************************************/
}
