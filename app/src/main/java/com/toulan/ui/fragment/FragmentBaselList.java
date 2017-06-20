package com.toulan.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.fast.library.Adapter.recyclerview.BaseRecyclerAdapter;
import com.fast.library.utils.NetUtils;
import com.toulan.helper.BGARefreshHelper;
import com.toulan.helper.EmptyViewHelper;
import com.toulan91.R;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 说明：FragmentBaseList
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/20 22:49
 * <p/>
 * 版本：verson 1.0
 */
public abstract class FragmentBaselList<T> extends CommonFragment implements BGARefreshHelper.BGARefreshCallBack, NetUtils.OnNetWorkChangedListener {

    RecyclerView mRecyclerView;
    BGARefreshLayout mBGARefreshLayout;
    View emptyView;
    View viewBGA;
    RelativeLayout rlNetworkTip;

    private BaseRecyclerAdapter<T> mRecyclerAdapter;
    BGARefreshHelper mBGAHelper;
    EmptyViewHelper mEmptyViewHelper;

    @Override
    protected void onFirstUserVisible() {
        super.onFirstUserVisible();
        mRecyclerView = bind(R.id.recycler_view);
        mBGARefreshLayout = bind(R.id.bga_refresh);
        emptyView = bind(R.id.empty_view);
        viewBGA = bind(R.id.view_bga);
        rlNetworkTip = bind(R.id.rl_network_tip);

        mBGAHelper = new BGARefreshHelper(mBGARefreshLayout,this,false);
        mEmptyViewHelper = new EmptyViewHelper(emptyView,viewBGA);
        mRecyclerAdapter = createAdapter();
        mRecyclerView.setAdapter(mRecyclerAdapter);

        NetUtils.registerNetworkChangedListener(this);
    }

    public void autoRefresh(){
        if (NetUtils.isNetConnected()){
            mBGAHelper.autoRefresh();
            rlNetworkTip.setVisibility(View.GONE);
        }else {
            rlNetworkTip.setVisibility(View.VISIBLE);
            loadData(mBGARefreshLayout);
        }
    }

    @Override
    protected int getRootViewResID() {
        return R.layout.fragment_base_pull_list;
    }

    @Override
    public void onBGARefresh(BGARefreshLayout refreshLayout) {
        loadData(refreshLayout);
    }

    public abstract BaseRecyclerAdapter createAdapter();

    public BaseRecyclerAdapter<T> getAdapter(){
        return mRecyclerAdapter;
    }

    public abstract void loadData(BGARefreshLayout refreshLayout);

    @Override
    public void onNetChanged(boolean isConnect, NetUtils.NetWorkType type) {
        if (isConnect){
            autoRefresh();
            rlNetworkTip.setVisibility(View.GONE);
        }else {
            rlNetworkTip.setVisibility(View.VISIBLE);
        }
    }
}
