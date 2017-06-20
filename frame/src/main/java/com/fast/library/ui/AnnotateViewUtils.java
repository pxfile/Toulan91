package com.fast.library.ui;

import android.app.Activity;

import java.lang.reflect.Method;

/**
 * 说明：注解工具类[Activity,Fragment]
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/10/28 21:43
 * <p/>
 * 版本：verson 1.1
 */

public class AnnotateViewUtils {

    /**
     * 说明：activity绑定view
     * @param activity
     */
    private static boolean initContentView(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        ContentView bindView = clazz.getAnnotation(ContentView.class);
        boolean isBind = false;
        if (bindView != null){
            isBind = true;
            int layoutId = bindView.value();
            try {
                Method method = clazz.getMethod("setContentView",int.class);
                method.setAccessible(true);
                method.invoke(activity,layoutId);
            }catch (Exception e){
                e.printStackTrace();
                isBind = false;
            }
        }
        return isBind;
    }

    /**
     * 说明：fragment绑定view
     * @param fragment
     */
    private static boolean initContentView(SupportFragment fragment){
        Class<? extends android.support.v4.app.Fragment> clazz = fragment.getClass();
        ContentView bindView = clazz.getAnnotation(ContentView.class);
        boolean isBind = false;
        if (bindView != null){
            isBind = true;
            int layoutId = bindView.value();
            try {
                Method method = clazz.getMethod("setRootViewResID",int.class);
                method.setAccessible(true);
                method.invoke(fragment,layoutId);
            }catch (Exception e){
                e.printStackTrace();
                isBind = false;
            }
        }
        return isBind;
    }

    /**
     * @param currentClass
     *            当前类，一般为Activity或Fragment
     */
    public static boolean init(Object currentClass) {
        if (currentClass instanceof Activity){
            return initContentView((Activity) currentClass);
        }else if (currentClass instanceof SupportFragment){
            return initContentView((SupportFragment) currentClass);
        }else {
            throw new IllegalArgumentException(currentClass + " must be Activity or SupportFragment or FrameFragment!!!");
        }
    }

}

