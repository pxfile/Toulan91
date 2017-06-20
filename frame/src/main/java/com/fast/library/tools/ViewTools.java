package com.fast.library.tools;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;

/**
 * 说明：View的帮助类，赋值等功能
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/4/7 11:43
 * <p/>
 * 版本：verson 1.0
 */
public final class ViewTools {

    /**
     * 说明：禁止实例化
     */
    private ViewTools(){}

    /**
     * 说明：View赋值
     *          ---TextView
     *          ---Button
     *          ---CheckBox
     *          ---EditText
     *          ---RadioButton
     * @param v
     * @param text
     */
    public static void setText(View v,CharSequence text){
        if (v == null){
            return;
        }
        if (StringUtils.isEmpty(text)){
            text = "";
        }
        if (v instanceof TextView){
            ((TextView)v).setText(text);
        }else if (v instanceof Button){
            ((Button)v).setText(text);
        }else if (v instanceof CheckBox){
            ((CheckBox)v).setText(text);
        }else if (v instanceof EditText){
            ((EditText)v).setText(text);
        }else if (v instanceof RadioButton){
            ((RadioButton)v).setText(text);
        }
    }

    /**
     * 说明：View赋值
     * @param v
     * @param resString
     */
    public static void setText(View v,int resString){
        setText(v, UIUtils.getString(resString));
    }

    /**
     * 说明：View赋值
     * @param v
     * @param format
     * @param obj
     */
    public static void setText(View v,String format,Object...obj){
        setText(v,String.format(format,obj));
    }

    /**
     * 说明：View赋值
     * @param v
     * @param resFormat
     * @param obj
     */
    public static void setText(View v,int resFormat,Object...obj){
        setText(v,String.format(UIUtils.getString(resFormat),obj));
    }

    /**
     * 说明：设置View为GONE
     * @param view
     */
    public static void GONE(View view){
        if (view != null){
            if (view.getVisibility() != View.GONE){
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 说明：设置View为VISIBLE
     * @param view
     */
    public static void VISIBLE(View view){
        if (view != null){
            if (view.getVisibility() != View.VISIBLE){
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 说明：设置View为INVISIBLE
     * @param view
     */
    public static void INVISIBLE(View view){
        if (view != null){
            if (view.getVisibility() != View.INVISIBLE){
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 说明：判断是否为隐藏
     * @param view
     * @return
     */
    public static boolean isHide(View view){
        return !isShow(view);
    }

    /**
     * 说明：判断是否显示
     * @param view
     * @return
     */
    public static boolean isShow(View view){
        boolean flag = false;
        if (view != null){
            flag = view.getVisibility() == View.VISIBLE;
        }
        return flag;
    }

}
