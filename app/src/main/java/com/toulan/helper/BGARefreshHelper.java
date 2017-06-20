package com.toulan.helper;

import android.content.Context;

import com.fast.library.ui.ToastUtil;
import com.fast.library.utils.NetUtils;
import com.toulan91.R;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * 说明：下拉刷新，加载更多
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/8/16 14:23
 * <p/>
 * 版本：verson 1.0
 */
public class BGARefreshHelper implements BGARefreshLayout.BGARefreshLayoutDelegate {

//    正在加载更多
    private final String loadingMoreText = "正在加载...";

    private BGARefreshCallBack mCallBack;
    private BGARefreshLayout mBGARefresh;
    private Context mContext;

    public BGARefreshHelper(BGARefreshLayout layout,BGARefreshCallBack callBack,boolean isLoadMore){
        this.mBGARefresh = layout;
        mContext = mBGARefresh.getContext();
        initBGARefresh(isLoadMore);
        setBGARefreshCallBack(callBack);
    }

    public void setBGARefreshCallBack(BGARefreshCallBack callBack){
        mCallBack = callBack;
    }

    private void initBGARefresh(boolean isLoadMore){
//        设置代理
        mBGARefresh.setDelegate(this);
//        设置下拉刷新和上拉加载更多的风格
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(mContext,isLoadMore);
        mBGARefresh.setRefreshViewHolder(refreshViewHolder);
//        设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText(loadingMoreText);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        if (NetUtils.isNetConnected()){
            if (mCallBack != null){
                mCallBack.onBGARefresh(refreshLayout);
            }
        }else {
            ToastUtil.get().shortToast(R.string.network_error);
            refreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (NetUtils.isNetConnected()){
            if (mCallBack != null){
                return mCallBack.onBGALoadMore(refreshLayout);
            }else {
                return true;
            }
        }else {
            refreshLayout.endLoadingMore();
            ToastUtil.get().shortToast(R.string.network_error);
            return true;
        }
    }

    /**
     * 自动刷新
     */
    public void autoRefresh(){
        if (mBGARefresh != null){
            mBGARefresh.beginRefreshing();
        }
    }

    public interface BGARefreshCallBack{
        void onBGARefresh(BGARefreshLayout refreshLayout);
        boolean onBGALoadMore(BGARefreshLayout refreshLayout);
    }
}
