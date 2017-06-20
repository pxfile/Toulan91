package com.fast.library.Adapter.viewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.fast.library.bean.ViewPageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：ViewPagerAdapter
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 15:58
 * <p/>
 * 版本：verson 1.0
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<ViewPageInfo> infos;
    private Context context;

    public ViewPagerAdapter(FragmentManager fm, Context context, List<ViewPageInfo> infos) {
        super(fm);
        this.context = context;
        this.infos = infos == null ? new ArrayList<ViewPageInfo>(0) : infos;
    }

    @Override
    public Fragment getItem(int position) {
        ViewPageInfo info = infos.get(position);
        return Fragment.instantiate(context,info.clazz.getName(),info.params);
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return infos.get(position).title;
    }
}
