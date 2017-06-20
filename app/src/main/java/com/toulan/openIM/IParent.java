package com.toulan.openIM;

import com.alibaba.mobileim.channel.cloud.contact.YWProfileInfo;
import android.support.v4.app.Fragment;
/**
 * 说明：
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Interface
 * <p/>
 * 时间：2016/9/28 10:50
 * <p/>
 * 版本：verson 1.0
 */
public interface IParent {

    //Fragment跳转相关

    public void addFragment(Fragment fragment, boolean addToBackStack);

    public void finish(boolean POP_BACK_STACK_INCLUSIVE);

    public YWProfileInfo getYWProfileInfo();

    public void setYWProfileInfo(YWProfileInfo ywProfileInfo) ;

    public boolean isHasContactAlready();

    public void setHasContactAlready(boolean hasContactAlready);
}
