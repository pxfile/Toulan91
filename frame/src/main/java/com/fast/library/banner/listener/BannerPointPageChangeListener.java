package com.fast.library.banner.listener;

import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import java.util.List;

/**
 * 说明：指示器监听器
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/4/8 20:12
 * <p/>
 * 版本：verson 1.0
 */
public class BannerPointPageChangeListener implements ViewPager.OnPageChangeListener{

    private ViewPager.OnPageChangeListener mListener;
    private List<ImageView> pointViews;
    private int selected;
    private int normal;

    public BannerPointPageChangeListener(List<ImageView> pointViews,int selected,int normal){
        this.pointViews = pointViews;
        this.selected = selected;
        this.normal = normal;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mListener != null){
            mListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mListener != null){
            mListener.onPageSelected(position);
        }
        for (int i = 0;i < pointViews.size();i++){
            if (i != position){
                pointViews.get(i).setImageResource(normal);
            }else {
                pointViews.get(position).setImageResource(selected);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mListener != null){
            mListener.onPageScrollStateChanged(state);
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        this.mListener = onPageChangeListener;
    }
}
