package com.toulan.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.fast.library.utils.NetUtils;
import com.toulan.bean.EmptyBean;
import com.toulan.bean.OrderBean;
import com.toulan.model.OnModelListener;
import com.toulan.model.TDataManager;
import com.toulan.utils.TUtils;
import com.toulan91.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 说明：订单详情
 * <p>
 * 作者：fanly
 * <p>
 * 类型：Class
 * <p>
 * 时间：2016/9/23 10:11
 * <p>
 * 版本：verson 1.0
 */
public class OrderDetailDialog extends CommonDialog {

    @Bind(R.id.tv_order_detail_id)
    TextView tvOrderDetailId;//服务单号
    @Bind(R.id.tv_order_detail_name)
    TextView tvOrderDetailName;//服务项目
    @Bind(R.id.tv_order_detail_time)
    TextView tvOrderDetailTime;//服务时间
    @Bind(R.id.tv_order_detail_address)
    TextView tvOrderDetailAddress;//服务地址
    @Bind(R.id.tv_order_detail_type)
    TextView tvOrderDetailType;//地址类型
    @Bind(R.id.tv_order_detail_area)
    TextView tvOrderDetailArea;//建筑面积
    @Bind(R.id.tv_order_detail_person)
    TextView tvOrderDetailPerson;//出单人员
    @Bind(R.id.tv_order_detail_service)
    TextView tvOrderDetailService;//派单客服
    @Bind(R.id.tv_order_detail_remark)
    TextView tvOrderDetailRemark;//订单备注

    private OrderBean mOrderBean;

    public OrderDetailDialog(Context context) {
        super(context);
    }

    public void showOrderDetail(OrderBean bean) {
        if (bean != null) {
            mOrderBean = bean;
            setOrderInfo();
            show();
        }
    }

    @Override
    public void onCreate() {
    }

    private void setOrderInfo() {
        ViewTools.setText(tvOrderDetailId,""+mOrderBean.getOrderDetail().getOid());
        ViewTools.setText(tvOrderDetailName,mOrderBean.getOrderDetail().getCateg());
        ViewTools.setText(tvOrderDetailTime,mOrderBean.getOrderDetail().getServiceTime());
        ViewTools.setText(tvOrderDetailAddress,mOrderBean.getOrderDetail().getServiceAddress());
        ViewTools.setText(tvOrderDetailType,mOrderBean.getOrderDetail().getAddressType());
        ViewTools.setText(tvOrderDetailArea,mOrderBean.getOrderDetail().getAcreage()+"㎡");
        ViewTools.setText(tvOrderDetailPerson,mOrderBean.getOrderDetail().getWorkther());
        ViewTools.setText(tvOrderDetailService,mOrderBean.getOrderDetail().getOperator());
        ViewTools.setText(tvOrderDetailRemark,mOrderBean.getOrderDetail().getRemark());
    }

    @Override
    public int setDialogView() {
        return R.layout.dialog_ordre_detail;
    }

    @OnClick({R.id.btn_order_detail_call, R.id.btn_order_detail_map, R.id.iv_order_detail_close})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_order_detail_call:
                if (NetUtils.isNetConnected()){
                    TDataManager.getInstance().callPhone(mOrderBean.getOrderDetail().getOid(), new OnModelListener<ArrayList<EmptyBean>>(EmptyBean.class,true) {
                        @Override
                        public void onFail(int what, int code, String msg) {
                            toast(msg);
                        }

                        @Override
                        public void onSuccess(int what, ArrayList<EmptyBean> been) {
                            toast(R.string.call_success);
                        }
                    });
                    dismiss();
                }else {
                    toast(R.string.network_error);
                }
                break;
            case R.id.btn_order_detail_map:
                if (mContext instanceof Activity){
                    TUtils.startMap((Activity) mContext,mOrderBean.getOrderDetail().getServiceLat(),mOrderBean.getOrderDetail().getServiceLng());
                }
                dismiss();
                break;
            case R.id.iv_order_detail_close:
                dismiss();
                break;
        }
    }

}
