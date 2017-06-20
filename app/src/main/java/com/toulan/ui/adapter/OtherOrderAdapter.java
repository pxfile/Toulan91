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
import com.toulan.bean.OrderBean;
import com.toulan.listener.OrderFunctionListener;
import com.toulan91.R;

import java.util.List;

/**
 * 说明：OtherOrderAdapter
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/21 23:02
 * <p/>
 * 版本：verson 1.0
 */
public class OtherOrderAdapter extends BaseRecyclerAdapter<OrderBean>{

    private ImageView ivOrderStatePic;
    public RelativeLayout rlJobDetail;
    private TextView tvServiceProject,tvServiceTime,tvServiceAddress,tvServicePerson;
    private TextView tvOrderId;

    private OrderFunctionListener mListener;

    public OtherOrderAdapter(RecyclerView recyclerView, List<OrderBean> data) {
        super(recyclerView, data);
    }

    public void setOrderFunctionListener(OrderFunctionListener listener){
        mListener = listener;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.item_order_other;
    }

    @Override
    public void convert(RecyclerViewHolder holder, final OrderBean item, int position, int viewType) {
        tvOrderId = holder.getTextView(R.id.tv_order_id);
        ivOrderStatePic = holder.getImageView(R.id.iv_order_state_pic);

        tvServiceProject = holder.getTextView(R.id.tv_service_project);
        tvServiceTime = holder.getTextView(R.id.tv_service_time);
        tvServiceAddress = holder.getTextView(R.id.tv_service_address);
        tvServicePerson = holder.getTextView(R.id.tv_service_person);

        rlJobDetail = holder.getView(R.id.rl_job_detail);

        if (item.isGoing()){
            ivOrderStatePic.setImageResource(R.drawable.job_order_going);

//            订单ID
            SpanSetting orderIdSpan = new SpanSetting().setCharSequence(""+item.getOrderDetail().getOid())
                    .setColor(Color.parseColor("#ff5f62")).setFontSize(22);
            SpanSetting orderIdDesSpan = new SpanSetting().setCharSequence("\n订单号");
            SpanTextUtils.setText(tvOrderId,orderIdSpan,orderIdDesSpan);
        }else {
            ivOrderStatePic.setImageResource(R.drawable.job_order_finish);

//            订单ID
            SpanSetting orderIdSpan = new SpanSetting().setCharSequence(""+item.getOrderDetail().getOid())
                    .setColor(Color.parseColor("#09c373")).setFontSize(22);
            SpanSetting orderIdDesSpan = new SpanSetting().setCharSequence("\n订单号");
            SpanTextUtils.setText(tvOrderId,orderIdSpan,orderIdDesSpan);
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
                    mListener.clickOrderFunction(TodayOrderAdapter.Function.orderDetail,item);
                }
            }
        });
    }
}
