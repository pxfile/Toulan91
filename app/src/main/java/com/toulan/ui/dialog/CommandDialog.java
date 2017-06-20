package com.toulan.ui.dialog;

import android.content.Context;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.toulan91.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 说明：CommandDialog
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 12:35
 * <p/>
 * 版本：verson 1.0
 */
public class CommandDialog extends CommonDialog {

    @Bind(R.id.dialog_command_desc)
    TextView dialogCommandDesc;
    @Bind(R.id.dialog_command_confirm)
    TextView dialogCommandConfirm;

    private OnComandListener mListener;

    public interface OnComandListener{
        void onCommand(CommonDialog dialog);
    }

    public CommandDialog(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {

    }

    public CommandDialog setDesc(String desc){
        ViewTools.setText(dialogCommandDesc,desc);
        return this;
    }

    public CommandDialog setConfirmText(String text){
        ViewTools.setText(dialogCommandConfirm,text);
        return this;
    }

    public CommandDialog setOnCommandListener(OnComandListener listener){
        this.mListener = listener;
        return this;
    }

    @Override
    public int setDialogView() {
        return R.layout.dialog_command;
    }

    @OnClick(R.id.dialog_command_confirm)
    public void onClick() {
        if (mListener != null){
            mListener.onCommand(this);
        }
    }
}
