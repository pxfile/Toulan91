package com.toulan.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.fast.library.utils.UIUtils;
import com.toulan91.R;

/**
 * 说明：BaseDialog
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 11:09
 * <p/>
 * 版本：verson 1.0
 */
public abstract class BaseDialog extends Dialog {

    private View mDialogView;
    public Context mContext;

    public BaseDialog(Context context) {
        this(context, R.style.DiyDialog);
    }

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init(setDialogView());
    }

    public BaseDialog(Context context, int themeResId, int layoutId) {
        super(context, themeResId);
        this.mContext = context;
        init(layoutId);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 说明：初始化
     */
    private void init(int layoutId) {
        mDialogView = UIUtils.inflate(layoutId);
        if (isFullScreen()) {
            getDialogView().setMinimumWidth(UIUtils.screenWidth());
            getDialogView().setMinimumHeight(UIUtils.screenHeight());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            addContentView(mDialogView, params);
        } else if (setParams() != null) {
            addContentView(mDialogView, setParams());
        } else {
            setContentView(mDialogView);
        }
        if (addWindowAnimations() > 0) {
            getWindow().setWindowAnimations(addWindowAnimations());
        }
        onInit();
    }

    public int addWindowAnimations() {
        return 0;
    }

    public boolean isFullScreen() {
        return false;
    }

    public LinearLayout.LayoutParams setParams() {
        return null;
    }

    /**
     * 说明：初始化
     */

    public abstract void onInit();

    /**
     * 说明：设置自定义布局
     * @return
     */
    public abstract int setDialogView();

    /**
     * 说明：获取自定义布局
     * @return
     */
    public View getDialogView() {
        return mDialogView;
    }

    public void onDestroy() {
        this.mContext = null;
    }

}


