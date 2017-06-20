package com.toulan.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.toulan.bean.OrderBean;
import com.toulan.utils.TUtils;
import com.toulan91.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 说明：OrderConfirmDialog
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/24 10:48
 * <p/>
 * 版本：verson 1.0
 */
public class OrderConfirmDialog extends CommonDialog {

    @Bind(R.id.tv_order_confirm_id)
    TextView tvOrderId;
    @Bind(R.id.tv_order_confirm_server_time)
    TextView tvOrderConfirmServerTime;
    @Bind(R.id.tv_order_confirm_add_money)
    TextView tvOrderConfirmAddMoney;
    @Bind(R.id.tv_order_confirm_custom_add_money)
    TextView tvOrderConfirmCustomAddMoney;
    @Bind(R.id.tv_order_confirm_remark)
    TextView tvOrderConfirmRemark;

    private OrderBean mOrderBean;

    public OrderConfirmDialog(Context context) {
        super(context);
    }

    public void showOrderConfirm(OrderBean bean) {
        if (bean != null) {
            mOrderBean = bean;
            setOrderInfo();
            show();
        }
    }

    private void setOrderInfo() {
        ViewTools.setText(tvOrderId,"单号"+mOrderBean.getOrderFinish().getOid());
        ViewTools.setText(tvOrderConfirmServerTime, TUtils.convertWorkingTime(mOrderBean.getOrderFinish().getWorkingTime()));
        ViewTools.setText(tvOrderConfirmAddMoney,mOrderBean.getOrderFinish().getMyFarMoney()+"元");
        ViewTools.setText(tvOrderConfirmCustomAddMoney,mOrderBean.getOrderFinish().getCustomerFarMoney()+"元");
        ViewTools.setText(tvOrderConfirmRemark,mOrderBean.getOrderFinish().getSceneNotes());
    }

    @Override
    public void onCreate() {
        setCanceledOnTouchOutside(false);
    }

    @Override
    public int setDialogView() {
        return R.layout.dialog_order_confirm;
    }

    @OnClick({R.id.iv_order_detail_close, R.id.dialog_command_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_order_detail_close:
            case R.id.dialog_command_confirm:
                dismiss();
                break;
        }
    }
}
