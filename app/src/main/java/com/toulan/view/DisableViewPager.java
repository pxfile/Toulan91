package com.toulan.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 说明：DisableViewPager
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 15:46
 * <p/>
 * 版本：verson 1.0
 */
public class DisableViewPager extends ViewPager {

    public DisableViewPager(Context context) {
        super(context);
    }

    public DisableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
