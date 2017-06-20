package com.toulan.helper;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.toulan91.R;

/**
 * 说明：EmptyViewHelper
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/21 22:29
 * <p/>
 * 版本：verson 1.0
 */
public class EmptyViewHelper {

    public View emptyView,userView;
    private TextView tvErrorDesc;
    private OnEmptyViewClickListener mListener;

    public interface OnEmptyViewClickListener{
        void onClickEmptyView(View emptyView);
    }

    public EmptyViewHelper(View emptyView,View userView){
        this.emptyView = emptyView;
        this.userView = userView;
        init();
    }

    private void init(){
        tvErrorDesc = (TextView) emptyView.findViewById(R.id.tv_state_desc);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.onClickEmptyView(emptyView);
                }
            }
        });
        loadSuccess();
    }

    public void setOnEmptyViewClickListener(OnEmptyViewClickListener listener){
        mListener = listener;
    }

    /**
     * 设置错误标题
     * @param resId
     */
    public EmptyViewHelper setTitle(@StringRes int resId){
        ViewTools.setText(tvErrorDesc,resId);
        return this;
    }

    /**
     * 设置错误标题
     * @param str
     */
    public EmptyViewHelper setTitle(String str){
        ViewTools.setText(tvErrorDesc,str);
        return this;
    }

    /**
     * 加载成功
     */
    public void loadSuccess(){
        ViewTools.GONE(emptyView);
        ViewTools.VISIBLE(userView);
    }

    /**
     * 加载失败
     */
    public EmptyViewHelper loadFail(@StringRes int resId){
        setTitle(resId);
        ViewTools.VISIBLE(emptyView);
        ViewTools.GONE(userView);
        return this;
    }

    /**
     * 加载失败
     */
    public EmptyViewHelper loadFail(String str){
        setTitle(str);
        ViewTools.VISIBLE(emptyView);
        ViewTools.GONE(userView);
        return this;
    }
}
