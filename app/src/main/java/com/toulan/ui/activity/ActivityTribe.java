package com.toulan.ui.activity;

import com.fast.library.ui.ContentView;
import com.toulan.ui.CommonActivity;
import com.toulan.ui.fragment.TribeFragment;
import com.toulan.utils.OpenImUtils;
import com.toulan91.R;

/**
 * 说明：ActivityTribe
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
public class ActivityTribe extends CommonActivity{

    @Override
    public void onInitStart() {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        TribeFragment fragment = new TribeFragment();
        transaction.add(R.id.fl_content, fragment, fragment.getClass()
                .getName());
        transaction.commit();
    }
}
