package com.toulan.ui.dialog;

import android.content.Context;

import com.fast.library.view.CircleProgressView;
import com.toulan91.R;

import butterknife.Bind;

/**
 * 说明：等待框
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 11:58
 * <p/>
 * 版本：verson 1.0
 */
public class LoadingDialog extends CommonDialog{

    @Bind(R.id.dialog_loading_cpv)
    CircleProgressView loadingCPV;

    public LoadingDialog(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        loadingCPV.setCircleColos(R.color.colorAccent);
    }

    @Override
    public int setDialogView() {
        return R.layout.dialog_loading;
    }

    public LoadingDialog setCancelOnSide(boolean canceled){
        setCanceledOnTouchOutside(canceled);
        return this;
    }

    public LoadingDialog setCancel(boolean canceled){
        setCancelable(canceled);
        return this;
    }

}
