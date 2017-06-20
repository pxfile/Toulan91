package com.toulan.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fast.library.Adapter.recyclerview.BaseRecyclerAdapter;
import com.fast.library.tools.EventCenter;
import com.fast.library.tools.ViewTools;
import com.fast.library.utils.NetUtils;
import com.toulan.bean.JobTabBean;
import com.toulan.bean.OrderBean;
import com.toulan.helper.EmptyViewHelper;
import com.toulan.helper.OrderHelper;
import com.toulan.listener.OrderFunctionListener;
import com.toulan.ui.adapter.OtherOrderAdapter;
import com.toulan.ui.adapter.TodayOrderAdapter;
import com.toulan.ui.dialog.OrderDetailDialog;
import com.toulan.utils.TConstant;
import com.toulan91.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 说明：非今天订单列表
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/20 22:49
 * <p/>
 * 版本：verson 1.0
 */
public class OtherOrderFragment extends FragmentBaselList<OrderBean> implements EmptyViewHelper.OnEmptyViewClickListener, OrderFunctionListener {

    public final static String BUNDLE_INDEX = "BUNDLE_INDEX";
    public String days;
    public int index;
    public OtherOrderAdapter mOrderAdapter;
    private OrderDetailDialog mOrderDetailDialog;

    @Override
    protected void getBundleData(Bundle bundle) {
        super.getBundleData(bundle);
        index = bundle.getInt(BUNDLE_INDEX);
    }

    @Override
    protected void onFirstUserVisible() {
        super.onFirstUserVisible();
        mEmptyViewHelper.setOnEmptyViewClickListener(this);
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public BaseRecyclerAdapter createAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(mCommonActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mOrderAdapter = new OtherOrderAdapter(mRecyclerView,null);
        mOrderAdapter.setOrderFunctionListener(this);
        return mOrderAdapter;
    }

    @Override
    public void loadData(final BGARefreshLayout refreshLayout) {
        OrderHelper.getOrderList(days, new OrderHelper.OnOrderListListener() {
            @Override
            public void onStart() {
            }
            @Override
            public void onSuccess(ArrayList<OrderBean> orderList) {
                if (orderList.isEmpty()){
                    mEmptyViewHelper.loadFail(R.string.job_order_list_empty);
                }else {
                    mEmptyViewHelper.loadSuccess();
                    mOrderAdapter.refresh(orderList);
                }
            }
            @Override
            public void onFail(String msg) {
                mEmptyViewHelper.loadFail(R.string.job_order_list_empty);
            }
            @Override
            public void onFinish() {
                refreshLayout.endRefreshing();
                if (NetUtils.isNetConnected()){
                    ViewTools.GONE(rlNetworkTip);
                }else {
                    ViewTools.VISIBLE(rlNetworkTip);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void clickRefresh(EventCenter<JobTabBean> eventCenter){
        if (TConstant.EventType.toRefreshJobOrder.equals(eventCenter.type) && eventCenter.data.index == index){
            days = eventCenter.data.getQueryKey();
            autoRefresh();
        }
    }

    @Override
    public boolean onBGALoadMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void onClickEmptyView(View emptyView) {
        autoRefresh();
    }

    @Override
    public void clickOrderFunction(TodayOrderAdapter.Function function, OrderBean bean) {
        showOrderDetail(bean);
    }

    public void showOrderDetail(OrderBean bean){
        if (mOrderDetailDialog == null){
            mOrderDetailDialog = new OrderDetailDialog(mCommonActivity);
            mOrderDetailDialog.setCanceledOnTouchOutside(false);
        }
        mOrderDetailDialog.showOrderDetail(bean);
    }
}
