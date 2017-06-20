package com.fast.library.ui;

import android.content.Intent;
import android.os.Bundle;

/**
 * 说明：规范Activity跳转的接口协议
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/10/28 21:50
 * <p/>
 * 版本：verson 1.0
 */
public interface I_SkipActivity {
    void skipActivity(Class<?> cls);
    void skipActivity(Intent intent);
    void skipActivity(Class<?> cls, Bundle bundle);
    void showActivity(Class<?> cls);
    void showActivity(Intent intent);
    void showActivity(Class<?> cls, Bundle bundle);
    void showActivityForResult(Class<?> cls, int requestCode);
}
