package com.toulan.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.fast.library.Adapter.recyclerview.BaseRecyclerAdapter;
import com.fast.library.tools.EventCenter;
import com.fast.library.tools.ViewTools;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.NetUtils;
import com.fast.library.utils.StringUtils;
import com.toulan.bean.EmptyBean;
import com.toulan.bean.FenceBean;
import com.toulan.bean.JobTabBean;
import com.toulan.bean.OrderBean;
import com.toulan.helper.EmptyViewHelper;
import com.toulan.helper.OrderHelper;
import com.toulan.helper.WriterHelper;
import com.toulan.listener.OrderFunctionListener;
import com.toulan.model.OnModelListener;
import com.toulan.model.OnStringListener;
import com.toulan.model.TDataManager;
import com.toulan.ui.activity.OrderPicDetailActivity;
import com.toulan.ui.activity.OrderTimerActivity;
import com.toulan.ui.adapter.TodayOrderAdapter;
import com.toulan.ui.dialog.OrderConfirmDialog;
import com.toulan.ui.dialog.OrderConfirmSubmitDialog;
import com.toulan.ui.dialog.OrderDetailDialog;
import com.toulan.utils.TConstant;
import com.toulan.utils.TUtils;
import com.toulan.utils.Tsp;
import com.toulan91.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

import static android.app.Activity.RESULT_OK;

/**
 * 说明：今天订单列表
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/20 22:49
 * <p/>
 * 版本：verson 1.0
 */
public class TodayOrderFragment extends FragmentBaselList<OrderBean> implements OrderFunctionListener, EmptyViewHelper.OnEmptyViewClickListener {

    public String days;
    public TodayOrderAdapter mOrderAdapter;
    private OrderDetailDialog mOrderDetailDialog;
    private OrderConfirmDialog mOrderConfirmDialog;
    private OrderConfirmSubmitDialog mSubmitDialog;

    private final static int REQUEST_CODE_PIC = 100;
    private ArrayList<String> arrayList;

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected void onFirstUserVisible() {
        super.onFirstUserVisible();
        mEmptyViewHelper.setOnEmptyViewClickListener(this);
    }

    @Override
    public BaseRecyclerAdapter createAdapter() {
        LinearLayoutManager manager = new LinearLayoutManager(mCommonActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mOrderAdapter = new TodayOrderAdapter(mRecyclerView,null);
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
                    createFence(orderList);
                }
            }
            @Override
            public void onFail(String msg) {
                mEmptyViewHelper.loadFail(msg);
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
        if (TConstant.EventType.toRefreshJobOrder.equals(eventCenter.type) && eventCenter.data.index == 0){
            days = eventCenter.data.getQueryKey();
            autoRefresh();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toOrderPicDetail(EventCenter<String> eventCenter){
        if (TConstant.EventType.toOrderDetailPic.equals(eventCenter.type)){
            Bundle bundle = new Bundle();
            bundle.putString(OrderPicDetailActivity.DATA,eventCenter.data);
            showActivity(OrderPicDetailActivity.class,bundle);
        }
    }

    @Override
    public boolean onBGALoadMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void clickOrderFunction(TodayOrderAdapter.Function function, OrderBean bean) {
        if (function == TodayOrderAdapter.Function.orderDetail){
            showOrderDetail(bean);
        }else if (function == TodayOrderAdapter.Function.cloud){
            if (bean.getOrderPic() == null || bean.getOrderPic().isEmpty()){
                File takePhotoDir = new File(Environment.getExternalStorageDirectory(), TConstant.APP);
                arrayList = Tsp.getOrderPic(bean.getOrderDetail().getOid());
                startActivityForResult(BGAPhotoPickerActivity.newIntent(mCommonActivity,bean.getOrderDetail().getOid(),takePhotoDir,9,arrayList,true),REQUEST_CODE_PIC);
            }else {
                EventBus.getDefault().post(new EventCenter<String>(TConstant.EventType.toOrderDetailPic, GsonUtils.toJson(bean)));
            }
        }else if (function == TodayOrderAdapter.Function.timer){
            startOrderTimer(bean);
        }else if (function == TodayOrderAdapter.Function.confirm){
            confirmOrder(bean);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PIC) {
            arrayList = BGAPhotoPickerActivity.getSelectedImages(data);
            int oid = BGAPhotoPickerActivity.getOrderId(data);
            if (!arrayList.isEmpty() && oid != 0){
                submitPic(oid,arrayList);
            }
        }
    }
    private void uploadPic(String token,final int oid, final ArrayList<String> list){
        TDataManager.getInstance().uploadFile(token, list, new OnStringListener() {
            @Override
            public void onStart(int what) {
                showLoading();
            }
            @Override
            public void onFinish(int what) {
                dismissLoading();
            }

            @Override
            public void onSuccess(int what, String node) {
                try {
                    JSONObject object = new JSONObject(node);
                    int failed = object.getJSONArray("failed").length();
                    if (failed != list.size()){
                        shortToast("上传成功");
//                        Tsp.saveOrderPic(oid,list);
                    }
                    autoRefresh();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(int what, int code, String msg) {
                shortToast(msg);
            }
        });
    }

    private void submitPic(final int oid, final ArrayList<String> pics){
        TDataManager.getInstance().uploadToken(oid, new OnStringListener() {
            @Override
            public void onStart(int what) {
                showLoading();
            }
            @Override
            public void onFinish(int what) {
                dismissLoading();
            }
            @Override
            public void onSuccess(int what, String node) {
                String token = GsonUtils.optString(node,"uploadToken");
                if (!StringUtils.isEmpty(token)){
                    uploadPic(token,oid,pics);
                }else {
                    shortToast("上传错误！");
                }
            }
            @Override
            public void onFail(int what, int code, String msg) {
                shortToast("上传错误！");
            }
        });
    }

    public void confirmOrder(OrderBean bean){
        int workingTime = bean.getOrderFinish().getWorkingTime();
        int cacheTime = Tsp.getOrderTimerServerTime(bean.getOrderDetail().getOid());
        if (workingTime > 0){
            if (mOrderConfirmDialog == null){
                mOrderConfirmDialog = new OrderConfirmDialog(mCommonActivity);
                mOrderConfirmDialog.setCanceledOnTouchOutside(false);
            }
            mOrderConfirmDialog.showOrderConfirm(bean);
        }else {
            if (cacheTime > 0 || TUtils.orderTimerOverTime(bean)){
                if (mSubmitDialog == null){
                    mSubmitDialog = new OrderConfirmSubmitDialog(mCommonActivity);
                    mSubmitDialog.setOnSubmitListener(new OrderConfirmSubmitDialog.OnSubmitListener() {
                        @Override
                        public void onSubmit(int oid, int workingTime, int addTime, int addMoney, int customMoney, String notes) {
                            submit(oid,workingTime,addTime,addMoney,customMoney,notes);
                        }
                    });
                }
                mSubmitDialog.showOrderConfirm(bean);
            }else{
                shortToast("请开始您的服务！");
            }
        }
    }

    public void startOrderTimer(OrderBean bean){
        Bundle bundle = new Bundle();
        bundle.putString(OrderTimerActivity.ORDER_BEAN, GsonUtils.toJson(bean));
        showActivity(OrderTimerActivity.class,bundle);
    }

    public void showOrderDetail(OrderBean bean){
        if (mOrderDetailDialog == null){
            mOrderDetailDialog = new OrderDetailDialog(mCommonActivity);
            mOrderDetailDialog.setCanceledOnTouchOutside(false);
        }
        mOrderDetailDialog.showOrderDetail(bean);
    }

    @Override
    public void onClickEmptyView(View emptyView) {
        autoRefresh();
    }

    private void submit(final int oid, int workingTime, int addTime, int addMoney, int customMoney, String notes){
        TDataManager.getInstance().orderSave(oid, workingTime, addTime, addMoney, customMoney, notes, new OnModelListener<ArrayList<EmptyBean>>(EmptyBean.class) {

            @Override
            public void onStart(int what) {
                showLoading();
            }

            @Override
            public void onFail(int what, int code, String msg) {
                shortToast(msg);
            }

            @Override
            public void onSuccess(int what, ArrayList<EmptyBean> bean) {
                shortToast("提交成功！");
                autoRefresh();
                Tsp.saveOrderSuccess(oid);
            }

            @Override
            public void onFinish(int what) {
                dismissLoading();
            }
        });
    }

    private void createFence(ArrayList<OrderBean> orderList){
        for (OrderBean bean:orderList){
            FenceBean fenceBean = FenceBean.createFence(bean);
            if (fenceBean != null){
                fenceBean.create();
            }else {
                WriterHelper.writeFenceLog(fenceBean.toString());
            }
        }
    }
}
