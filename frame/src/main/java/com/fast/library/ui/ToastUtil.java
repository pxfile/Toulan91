package com.fast.library.ui;

import com.fast.library.FastFrame;
import com.fast.library.utils.UIUtils;

/**
 * 说明：View提示的工具类
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015-8-26
 * <p/>
 * 版本：verson 1.0
 */

public class ToastUtil {

    private static ToastUtil inject;
    private android.widget.Toast mToast;

    /*禁止实例化*/
    private ToastUtil() {
    }

    public static ToastUtil get() {
        if (inject == null) {
            synchronized (ToastUtil.class) {
                if (inject == null) {
                    inject = new ToastUtil();
                }
            }
        }
        return inject;
    }

    /**
     * 说明：显示短Toast
     *
     * @param msg
     */
    public android.widget.Toast shortToast(String msg) {
        return showToast(msg, android.widget.Toast.LENGTH_SHORT);
    }


    /**
     * 说明：显示短Toast
     *
     * @param msg
     */
    public android.widget.Toast shortToast(int msg) {
        return shortToast(UIUtils.getString(msg));
    }

    /**
     * 说明：显示Toast
     * @param msg
     * @param time
     * @return
     */
    private android.widget.Toast showToast(String msg, int time) {
        if (mToast == null) {
            mToast = android.widget.Toast.makeText(FastFrame.getApplication(), msg, time);
        } else {
            mToast.setText(msg);
            mToast.setDuration(time);
        }
        mToast.show();
        return mToast;
    }

    /**
     * 说明：取消显示Toast
     */
    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
        }
    }

    /**
     * 说明：显示长Toast
     *
     * @param msg
     */
    public android.widget.Toast longToast(String msg) {
        return showToast(msg, android.widget.Toast.LENGTH_LONG);
    }

    /**
     * 说明：显示长Toast
     *
     * @param msg
     */
    public android.widget.Toast longToast(int msg) {
        return longToast(UIUtils.getString(msg));
    }

}

