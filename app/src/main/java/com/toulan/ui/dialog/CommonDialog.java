package com.toulan.ui.dialog;

import android.content.Context;
import android.support.annotation.StringRes;

import com.fast.library.ui.ToastUtil;
import com.toulan91.R;

import butterknife.ButterKnife;

/**
 * 说明：CommonDialog
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 11:21
 * <p/>
 * 版本：verson 1.0
 */
public abstract class CommonDialog extends BaseDialog{

    public CommonDialog(Context context) {
        super(context);
    }

    @Override
    public void onInit() {
        ButterKnife.bind(this);
        onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public abstract void onCreate();

    public void toast(@StringRes int resId){
        ToastUtil.get().shortToast(resId);
    }

    public void toast(String msg){
        ToastUtil.get().shortToast(msg);
    }
}
