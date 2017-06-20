package com.fast.library.banner.holder;

import android.content.Context;
import android.view.View;

/**
 * 说明：Holder
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/4/8 17:02
 * <p/>
 * 版本：verson 1.0
 */
public interface Holder<T> {
    View createView(Context context);
    void convert(Context context, int position, T item);
}
