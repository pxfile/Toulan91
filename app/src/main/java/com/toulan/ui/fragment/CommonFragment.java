package com.toulan.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.fast.library.view.BaseLazyFragment;
import com.toulan.ui.CommonActivity;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * 说明：CommonFragment
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 16:00
 * <p/>
 * 版本：verson 1.0
 */
public class CommonFragment extends BaseLazyFragment{

    public CommonActivity mCommonActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CommonActivity){
            mCommonActivity = (CommonActivity) context;
        }
    }

    @Override
    protected void onInitCreate(Bundle savedInstanceState, View view) {
        if (isRegisterEventBus()){
            EventBus.getDefault().register(this);
        }
        ButterKnife.bind(this,view);
    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isRegisterEventBus()){
            EventBus.getDefault().unregister(this);
        }
        ButterKnife.unbind(this);
    }

    public boolean isRegisterEventBus(){
        return false;
    }

    public void showLoading(){
        if (mCommonActivity != null){
            mCommonActivity.showLoading(false,false);
        }
    }

    public void dismissLoading(){
        if (mCommonActivity != null){
            mCommonActivity.dismissLoading();
        }
    }

}
