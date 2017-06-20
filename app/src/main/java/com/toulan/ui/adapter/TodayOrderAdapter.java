package com.toulan.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fast.library.Adapter.recyclerview.BaseRecyclerAdapter;
import com.fast.library.Adapter.recyclerview.RecyclerViewHolder;
import com.fast.library.span.SpanSetting;
import com.fast.library.span.SpanTextUtils;
import com.fast.library.tools.EventCenter;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.UIUtils;
import com.toulan.bean.OrderBean;
import com.toulan.listener.OrderFunctionListener;
import com.toulan.utils.TConstant;
import com.toulan.utils.TUtils;
import com.toulan.utils.Tsp;
import com.toulan91.R;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：TodayOrderAdapter
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/21 23:02
 * <p/>
 * 版本：verson 1.0
 */
public class TodayOrderAdapter extends BaseRecyclerAdapter<OrderBean>{

    private RelativeLayout rlJobDetail,rlJobClound,rlJobTimer,rlJobConfirm;
    private TextView tvJobConfirm;

    private TextView tvJobCloudNumber,tvOrderId;
    private ImageView ivOrderStatePic;

    private TextView tvServiceProject,tvServiceTime,tvServiceAddress,tvServicePerson;

    private OrderFunctionListener mListener;

    public enum Function{
        orderDetail,cloud,timer,confirm
    }

    public TodayOrderAdapter(RecyclerView recyclerView, List<OrderBean> data) {
        super(recyclerView, data);
    }

    public void setOrderFunctionListener(OrderFunctionListener listener){
        mListener = listener;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_order_today;
    }

    @Override
    public void convert(RecyclerViewHolder holder, final OrderBean item, int position, int viewType) {
        rlJobDetail = holder.getView(R.id.rl_job_detail);
        rlJobClound = holder.getView(R.id.rl_job_cloud);
        rlJobTimer = holder.getView(R.id.rl_job_timer);
        rlJobConfirm = holder.getView(R.id.rl_job_confirm);

        tvJobConfirm = holder.getTextView(R.id.tv_job_confirm);

        tvJobCloudNumber = holder.getTextView(R.id.tv_job_cloud_number);
        tvOrderId = holder.getTextView(R.id.tv_order_id);
        ivOrderStatePic = holder.getImageView(R.id.iv_order_state_pic);

        tvServiceProject = holder.getTextView(R.id.tv_service_project);
        tvServiceTime = holder.getTextView(R.id.tv_service_time);
        tvServiceAddress = holder.getTextView(R.id.tv_service_address);
        tvServicePerson = holder.getTextView(R.id.tv_service_person);

        if (item.isGoing()){
            tvJobConfirm.setText("确认");
        }else {
            tvJobConfirm.setText("已确认");
        }

        if (!TUtils.orderTimerOverTime(item)){
            ivOrderStatePic.setImageResource(R.drawable.job_order_finish);
//            订单ID
            SpanSetting orderIdSpan = new SpanSetting().setCharSequence(""+item.getOrderDetail().getOid())
                    .setColor(Color.parseColor("#09c373")).setFontSize(22);
            SpanSetting orderIdDesSpan = new SpanSetting().setCharSequence("\n订单号");
            SpanTextUtils.setText(tvOrderId,orderIdSpan,orderIdDesSpan);
        }else {
            ivOrderStatePic.setImageResource(R.drawable.job_order_going);
//            订单ID
            SpanSetting orderIdSpan = new SpanSetting().setCharSequence(""+item.getOrderDetail().getOid())
                    .setColor(Color.parseColor("#ff5f62")).setFontSize(22);
            SpanSetting orderIdDesSpan = new SpanSetting().setCharSequence("\n订单号");
            SpanTextUtils.setText(tvOrderId,orderIdSpan,orderIdDesSpan);
        }

        if (item.getOrderPic() != null && !item.getOrderPic().isEmpty()){
            tvJobCloudNumber.setVisibility(View.VISIBLE);
            if (item.getOrderPic().size() <= 99){
                tvJobCloudNumber.setText(String.valueOf(item.getOrderPic().size()));
            }else {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvJobCloudNumber.getLayoutParams();
                params.width = UIUtils.dip2px(25);
                params.height = UIUtils.dip2px(25);
                tvJobCloudNumber.setLayoutParams(params);
                tvJobCloudNumber.setText("99+");
            }
        }else {
            tvJobCloudNumber.setVisibility(View.GONE);
        }

//        服务项目
        SpanSetting projectTipSpan = new SpanSetting().setCharSequence("服务项目：");
        SpanSetting projectSpan = new SpanSetting().setCharSequence(item.getOrderDetail().getCateg())
                .setColor(Color.parseColor("#666666"));
        SpanTextUtils.setText(tvServiceProject,projectTipSpan,projectSpan);
//        服务时间
        SpanSetting timeTipSpan = new SpanSetting().setCharSequence("服务时间：");
        SpanSetting timeSpan = new SpanSetting().setCharSequence(item.getOrderDetail().getServiceTime())
                .setColor(Color.parseColor("#666666"));
        SpanTextUtils.setText(tvServiceTime,timeTipSpan,timeSpan);
//        服务地址
        SpanSetting addressTipSpan = new SpanSetting().setCharSequence("服务地址：");
        SpanSetting addressSpan = new SpanSetting().setCharSequence(item.getOrderDetail().getServiceAddress())
                .setColor(Color.parseColor("#666666"));
        SpanTextUtils.setText(tvServiceAddress,addressTipSpan,addressSpan);
//        服务地址
        SpanSetting personTipSpan = new SpanSetting().setCharSequence("出单人员：");
        SpanSetting personSpan = new SpanSetting().setCharSequence(item.getOrderDetail().getWorkther())
                .setColor(Color.parseColor("#666666"));
        SpanTextUtils.setText(tvServicePerson,personTipSpan,personSpan);

        rlJobDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.clickOrderFunction(Function.orderDetail,item);
                }
            }
        });

        rlJobClound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.clickOrderFunction(Function.cloud,item);
                }
            }
        });

        rlJobTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.clickOrderFunction(Function.timer,item);
                }
            }
        });

        rlJobConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    mListener.clickOrderFunction(Function.confirm,item);
                }
            }
        });
    }
}
