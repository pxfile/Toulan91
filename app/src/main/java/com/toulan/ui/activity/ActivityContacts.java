package com.toulan.ui.activity;


import com.fast.library.ui.ContentView;
import com.toulan.ui.CommonActivity;
import com.toulan.utils.OpenImUtils;
import com.toulan91.R;

/**
 * 说明：ActivityContacts
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/26 14:00
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.activity_one_frame)
public class ActivityContacts extends CommonActivity{

    @Override
    public void onInitStart() {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.add(R.id.fl_content, OpenImUtils.getContactsFragment(), OpenImUtils.getContactsFragment().getClass()
                .getName());
        transaction.commit();
    }
}
