package com.toulan.ui.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.fast.library.utils.NumberUtils;
import com.fast.library.utils.StringUtils;
import com.toulan.bean.OrderBean;
import com.toulan.utils.Tsp;
import com.toulan91.R;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 说明：OrderConfirmSubmitDialog
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/25 11:28
 * <p/>
 * 版本：verson 1.0
 */
public class OrderConfirmSubmitDialog extends CommonDialog {

    @Bind(R.id.tv_order_confirm_id)
    TextView tvOrderConfirmId;
    @Bind(R.id.et_order_confirm_server_time_hour)
    EditText etHour;
    @Bind(R.id.et_order_confirm_server_time_min)
    EditText etMin;
    @Bind(R.id.et_order_confirm_add_money)
    EditText etAddMoney;
    @Bind(R.id.et_order_confirm_custom_money)
    EditText etCustomMoney;
    @Bind(R.id.et_order_confirm_my_note)
    EditText etMyNote;

    private String hour,min;
    private OrderBean mOrderBean;
    private int oid;
    private OnSubmitListener mListener;

    private int myServerTime,myAddTime,myMoney,myCustomMoney;
    private String notes;

    public interface OnSubmitListener{
        void onSubmit(int oid,int workingTime,int addTime,int addMoney,int customMoney,String notes);
    }

    public void setOnSubmitListener(OnSubmitListener listener){
        this.mListener = listener;
    }

    public OrderConfirmSubmitDialog(Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public void showOrderConfirm(OrderBean bean) {
        if (bean != null) {
            mOrderBean = bean;
            setOrderInfo();
            show();
        }
    }

    private void setOrderInfo() {
        oid = mOrderBean.getOrderDetail().getOid();
        int allTime = Tsp.getOrderTimerServerTime(oid)+Tsp.getOrderTimerAddTime(oid);
        ViewTools.setText(tvOrderConfirmId, "单号" + oid);
        hour = String.valueOf(allTime/3600);
        min = String.valueOf((allTime - NumberUtils.toInt(hour) * 3600)/60);
        if (StringUtils.isEquals(hour,"0")){
            hour = "";
        }
        if (StringUtils.isEquals(min,"0")){
            min = "";
        }
        ViewTools.setText(etMin,min);
        ViewTools.setText(etHour, hour);
        ViewTools.setText(etAddMoney, "");
        ViewTools.setText(etCustomMoney, "");

    }

    @Override
    public void onCreate() {

    }

    @Override
    public int setDialogView() {
        return R.layout.dialog_order_confirm_submit;
    }

    @OnClick({R.id.iv_order_detail_close,R.id.dialog_command_confirm,R.id.dialog_command_cancel})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_order_detail_close:
            case R.id.dialog_command_cancel:
                dismiss();
                break;
            case R.id.dialog_command_confirm:
                dismiss();
                if (check()){
                    if (mListener != null){
                        mListener.onSubmit(oid,myServerTime,myAddTime,myMoney,myCustomMoney,notes);
                    }
                }
                break;
        }
    }

    public boolean check(){
        boolean result = true;
        int etHourNum = NumberUtils.toInt(etHour.getText().toString(),0);
        int etMinNum = NumberUtils.toInt(etMin.getText().toString(),0);
        if (etHourNum <= 0 && etMinNum <= 0){
            toast("请填写服务时长");
            result = false;
        }else {
            if (etHour.getText().toString().equals(hour) && etMin.getText().toString().equals(min)){
                myServerTime = Tsp.getOrderTimerServerTime(oid);
            }else {
                myServerTime = NumberUtils.toInt(etHour.getText().toString(),0) * 3600 + NumberUtils.toInt(etMin.getText().toString(),0)*60;
            }
            myAddTime = Tsp.getOrderTimerAddTime(oid);
            myMoney = NumberUtils.toInt(etAddMoney.getText().toString(),0);
            myCustomMoney = NumberUtils.toInt(etCustomMoney.getText().toString(),0);
            notes = etMyNote.getText().toString();
        }
        return result;
    }
}
