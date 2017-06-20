package com.toulan.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.toulan91.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 说明：AppUpdateDialog
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/19 22:28
 * <p/>
 * 版本：verson 1.0
 */
public class AppUpdateDialog extends CommonDialog {

    private AppUpdateListener mListener;
    private String downloadUrl;

    @Bind(R.id.tv_app_command_cancel)
    TextView tvCancel;
    @Bind(R.id.tv_app_command_confirm)
    TextView tvConfirm;
    @Bind(R.id.tv_command_title)
    TextView tvCommandTitle;

    public AppUpdateDialog(Context context, AppUpdateListener listener) {
        super(context);
        this.mListener = listener;
    }

    @Override
    public void onCreate() {
        tvCancel.setText(R.string.app_update_cancel);
        tvConfirm.setText(R.string.app_update_install);
        tvCommandTitle.setText(R.string.app_update_tip);
    }

    public AppUpdateDialog setDownloadUrl(String url){
        this.downloadUrl = url;
        return this;
    }

    @Override
    public int setDialogView() {
        return R.layout.dialog_app_command;
    }

    @OnClick({R.id.tv_app_command_cancel, R.id.tv_app_command_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_app_command_cancel:
                if (mListener != null){
                    mListener.cancel();
                }
                break;
            case R.id.tv_app_command_confirm:
                if (mListener != null){
                    mListener.install(downloadUrl);
                }
                break;
        }
        dismiss();
    }

    public interface AppUpdateListener {
        void install(String url);
        void cancel();
    }
}
