package com.toulan.ui.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.fast.library.Adapter.viewpager.ViewPagerAdapter;
import com.fast.library.bean.ViewPageInfo;
import com.fast.library.tools.EventCenter;
import com.fast.library.tools.TaskEngine;
import com.fast.library.tools.ViewTools;
import com.fast.library.ui.ContentView;
import com.fast.library.utils.UIUtils;
import com.toulan.bean.JobTabBean;
import com.toulan.bean.UserBean;
import com.toulan.database.CallDao;
import com.toulan.ui.activity.PayActivity;
import com.toulan.utils.TConstant;
import com.toulan.utils.Tsp;
import com.toulan.view.JobTabLayout;
import com.toulan91.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 说明：JobFragment
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 16:01
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.fragment_job)
public class JobFragment extends CommonFragment implements JobTabLayout.OnJobTabListener {

    @Bind(R.id.tv_job_title)
    TextView tvJobTitle;
    @Bind(R.id.job_tab_layout)
    JobTabLayout jobTabLayout;
    @Bind(R.id.vp_job)
    ViewPager vpJob;

    UserBean mUserBean;
    private ViewPagerAdapter mFragmentAdapter;

    @Override
    protected void onFirstUserVisible() {
        super.onFirstUserVisible();
        mUserBean = Tsp.getUserInfo();
        initTitleInfo();
        jobTabLayout.setOnJobTabListener(this);
        initJob();
    }

    private void initJob(){
        mFragmentAdapter = new ViewPagerAdapter(getChildFragmentManager(), mCommonActivity, createFragment());
        vpJob.setAdapter(mFragmentAdapter);
        vpJob.setOffscreenPageLimit(mFragmentAdapter.getCount());

        TaskEngine.getInstance().schedule(new TimerTask() {
            @Override
            public void run() {
                mCommonActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        jobTabLayout.setCurrentTab(0);
                    }
                });
            }
        }, 100);
    }

    @Override
    protected void onUserVisible() {
        super.onUserVisible();
        jobTabLayout.updateView();
    }

    public ArrayList<ViewPageInfo> createFragment() {
        ArrayList<ViewPageInfo> list = new ArrayList<>(6);
        list.add(0, new ViewPageInfo("", TodayOrderFragment.class, null));
        list.add(1, new ViewPageInfo("", OtherOrderFragment.class, getBundle(1)));
        list.add(2, new ViewPageInfo("", OtherOrderFragment.class, getBundle(2)));
        list.add(3, new ViewPageInfo("", OtherOrderFragment.class, getBundle(3)));
        list.add(4, new ViewPageInfo("", OtherOrderFragment.class, getBundle(4)));
        list.add(5, new ViewPageInfo("", OtherOrderFragment.class, getBundle(5)));
        return list;
    }

    private Bundle getBundle(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(OtherOrderFragment.BUNDLE_INDEX, index);
        return bundle;
    }

    private void initTitleInfo() {
        String title = String.format(UIUtils.getString(R.string.str_main_title), mUserBean.getUserNo(), mUserBean.getName());
        ViewTools.setText(tvJobTitle, title);
    }

    @Override
    public void onJobTabSelected(JobTabBean bean) {
        EventBus.getDefault().post(new EventCenter<JobTabBean>(TConstant.EventType.toRefreshJobOrder, bean));
        vpJob.setCurrentItem(bean.index, false);
    }

    @Override
    public void onJobTabChanged() {
        initJob();
    }

    @OnClick(R.id.iv_job_pay)
    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_job_pay:
                showActivity(PayActivity.class);
                break;
        }
    }

}
