package com.fast.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 说明：软键盘工具类
 * <p>
 * 作者：fanly
 * <p>
 * 类型：Class
 * <p>
 * 时间：2016/8/18 15:37
 * <p>
 * 版本：verson 1.0
 */
public class KeyBoardUtils {

    public interface OnSoftKeyboardStateChangedListener {
        void OnSoftKeyboardStateChanged(boolean isKeyBoardShow, int keyboardHeight);
    }

    /**
     * 说明：强制显示软键盘
     * @param et
     */
    public static void showSoftInput(EditText et) {
        if (et != null) {
            et.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) et
                    .getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 说明：强制隐藏软键盘
     */
    public static void hiddenSoftInput(EditText editText) {
        if (editText != null) {
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager.isActive()) {
                inputManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }

    /**
     * 获取软键盘高度
     * @param paramActivity
     * @return
     */
    public static int getKeyboardHeight(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity);
        return height;
    }

    /**
     * 屏幕分辨率高
     * @param paramActivity
     * @return
     */
    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * statusBar高度
     * @param paramActivity
     * @return
     */
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;
    }

    /**
     * 可见屏幕高度
     * @param paramActivity
     * @return
     */
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    /**
     * 键盘是否在显示
     * @param paramActivity
     * @return
     */
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity);
        return height != 0;
    }

    public static void setOnGlobalLayoutListener(final Activity activity, final OnSoftKeyboardStateChangedListener listener){
        if (listener == null || activity == null)return;
        activity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isKeyBoardShow(activity)){
                    listener.OnSoftKeyboardStateChanged(true,getKeyboardHeight(activity));
                }else {
                    listener.OnSoftKeyboardStateChanged(false,0);
                }
            }
        });
    }
}
