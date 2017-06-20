package com.toulan.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 说明：订单信息
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/20 22:54
 * <p/>
 * 版本：verson 1.0
 */
public class OrderBean implements Parcelable{

    /**
     * orderStatus : 0
     * orderDetail : {"oid":8256,"categ":"日常清洁22","service_time":"2016-09-15 14:00-18:00","service_address":"宝山区大观园C区de","service_lng":"121.409041","service_lat":"31.3986226","address_type":"家庭-两房两厅","acreage":100,"workther":"赵凤霞","operator":"张林","remark":"新订单"}
     * orderPic : ["http://91toulan-bucket.oss-cn-hangzhou.aliyuncs.com/app/employee/201609/147390922857da11ece34c8_sham.jpg","http://91toulan-bucket.oss-cn-hangzhou.aliyuncs.com/app/employee/201609/147390922857da11ec16358_20160914144901.png"]
     * orderCountdown : {"oid":8256,"countdown_time":14400}
     * orderFinish : {"oid":8256,"working_time":0,"my_far_money":0,"customer_far_money":0,"scene_notes":""}
     */

    private int orderStatus;//订单状态：0-未提交，1-已提交
    private OrderDetailBean orderDetail;//job_order_detail_normal
    private OrderCountDownBean orderCountdown;//订单ID
    private OrderFinishBean orderFinish;//订单确认
    private List<String> orderPic;//订单图片-job_check_cloud_normal

    protected OrderBean(Parcel in) {
        orderStatus = in.readInt();
        orderDetail = in.readParcelable(OrderDetailBean.class.getClassLoader());
        orderCountdown = in.readParcelable(OrderCountDownBean.class.getClassLoader());
        orderFinish = in.readParcelable(OrderFinishBean.class.getClassLoader());
        orderPic = in.createStringArrayList();
    }

    public static final Creator<OrderBean> CREATOR = new Creator<OrderBean>() {
        @Override
        public OrderBean createFromParcel(Parcel in) {
            return new OrderBean(in);
        }

        @Override
        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderDetailBean getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetailBean orderDetail) {
        this.orderDetail = orderDetail;
    }

    public OrderCountDownBean getOrderCountdown() {
        return orderCountdown;
    }

    public void setOrderCountdown(OrderCountDownBean orderCountdown) {
        this.orderCountdown = orderCountdown;
    }

    public OrderFinishBean getOrderFinish() {
        return orderFinish;
    }

    public void setOrderFinish(OrderFinishBean orderFinish) {
        this.orderFinish = orderFinish;
    }

    public List<String> getOrderPic() {
        return orderPic;
    }

    public void setOrderPic(List<String> orderPic) {
        this.orderPic = orderPic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderStatus);
        dest.writeParcelable(orderDetail, flags);
        dest.writeParcelable(orderCountdown, flags);
        dest.writeParcelable(orderFinish, flags);
        dest.writeStringList(orderPic);
    }

    public boolean isGoing(){
        return orderStatus == 0;
    }
}
