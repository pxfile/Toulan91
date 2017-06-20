package com.toulan.openIM;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.alibaba.mobileim.channel.cloud.contact.YWProfileInfo;
import com.fast.library.ui.ContentView;
import com.toulan.ui.CommonActivity;
import com.toulan91.R;

/**
 * 说明：FindContactActivity
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/28 10:49
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.demo_container_activity)
public class FindContactActivity extends CommonActivity implements IParent {

    private static final String TAG = FindContactActivity.class.getSimpleName();
    private Fragment mCurrentFrontFragment;

    @Override
    public void onInitStart() {
        addFragment(new FindContactFragment(),false);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     */
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (mCurrentFrontFragment != null) {
            mCurrentFrontFragment.onActivityResult(arg0, arg1, arg2);
        }
    }


    //跳转相关

    public void addFragment(Fragment fragment, boolean addToBackStack) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.wx_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
//		getSupportFragmentManager().executePendingTransactions();

        mCurrentFrontFragment=fragment;
    }
    public void finish(boolean POP_BACK_STACK_INCLUSIVE) {
        if(POP_BACK_STACK_INCLUSIVE){
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }else{
            getSupportFragmentManager().popBackStack();
        }
    }

    //传递数据用，由father来持有
    private YWProfileInfo ywProfileInfo;
    //传递数据用，由father来持有
    private boolean hasContactAlready;

    public YWProfileInfo getYWProfileInfo() {
        return ywProfileInfo;
    }

    public void setYWProfileInfo(YWProfileInfo ywProfileInfo) {
        this.ywProfileInfo = ywProfileInfo;
    }
    public boolean isHasContactAlready() {
        return hasContactAlready;
    }

    public void setHasContactAlready(boolean hasContactAlready) {
        this.hasContactAlready = hasContactAlready;
    }
}
