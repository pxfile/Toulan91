package com.toulan.utils;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.toulan91.R;


/**
 * 说明：ToolBar帮助类
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/14 22:48
 * <p/>
 * 版本：verson 1.0
 */
public final class ToolbarHelper {

    //上下文
    private Context mContext;
    //toolbar
    private Toolbar mToolbar;
    //用户自定义view
    private View mUserView;
    //base view
    private FrameLayout mContentView;
    private LayoutInflater mInflater;

    /*
    * 两个属性
    * 1、toolbar是否悬浮在窗口之上
    * 2、toolbar的高度获取
    * */
    private static int[] ATTRS = {
            R.attr.windowActionBarOverlay,
            R.attr.actionBarSize
    };

    public ToolbarHelper(Context context, int layoutId){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initContentView();
        initToolbar();
        initUserView(layoutId);
    }

    /**
     * 说明：初始化用户自定义布局
     * @param id
     */
    @SuppressWarnings("ResourceType")
    private void initUserView(int id){
        mUserView = mInflater.inflate(id,null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TypedArray array = mContext.getTheme().obtainStyledAttributes(ATTRS);
        boolean overly = array.getBoolean(0, false);
        int toolBarSize = (int) array.getDimension(1,(int) mContext.getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
        array.recycle();
        params.topMargin = overly ? 0 :toolBarSize;
        mContentView.addView(mUserView, params);
    }

    /**
     * 说明：初始化toolbar
     */
    private void initToolbar(){
        View toolbar = mInflater.inflate(R.layout.view_toolbar, mContentView);
        mToolbar = (Toolbar)toolbar.findViewById(R.id.id_tool_bar);
    }

    /**
     * 说明：初始化整个内容
     */
    private void initContentView(){
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);
    }

    public Toolbar getToolbar(){
        return mToolbar;
    }

    public View getView(){
        return mContentView;
    }
}


