package com.toulan.bean;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * 说明：TabEntity
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 16:06
 * <p/>
 * 版本：verson 1.0
 */
public class TabEntity implements CustomTabEntity {

    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
