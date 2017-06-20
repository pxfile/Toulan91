package com.toulan.ui.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.toulan91.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 说明：CancelSureDialog
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/24 17:41
 * <p/>
 * 版本：verson 1.0
 */
public class CancelSureDialog extends CommonDialog {

    @Bind(R.id.dialog_content)
    TextView dialogContent;
    @Bind(R.id.btn_sure)
    Button btnSure;
    @Bind(R.id.btn_cancel)
    Button btnCancel;

    private OnCancelSureListener mListener;

    public CancelSureDialog(Context context) {
        super(context);
    }

    @OnClick({R.id.btn_sure, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sure:
                if (mListener != null){
                    mListener.onSure();
                }
                break;
            case R.id.btn_cancel:
                if (mListener != null){
                    mListener.onCancel();
                }
                break;
        }
    }

    public interface OnCancelSureListener {
        void onCancel();
        void onSure();
    }

    public CancelSureDialog setCancelSureListener(OnCancelSureListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    public void onCreate() {
        setCanceledOnTouchOutside(false);
    }

    public CancelSureDialog setContentText(String text) {
        dialogContent.setText(text);
        return this;
    }

    public CancelSureDialog setSureText(String text) {
        btnSure.setText(text);
        return this;
    }

    public CancelSureDialog setCancelText(String text) {
        btnCancel.setText(text);
        return this;
    }


    @Override
    public int setDialogView() {
        return R.layout.dialog_cancel_sure;
    }
}
