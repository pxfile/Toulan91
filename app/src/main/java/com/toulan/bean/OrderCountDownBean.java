package com.toulan.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：OrderCountdownBean
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/20 23:09
 * <p/>
 * 版本：verson 1.0
 */
public class OrderCountDownBean implements Parcelable{

    /**
     * oid : 8256
     * countdown_time : 14400
     */

    private int oid;//订单ID
    private int countdown_time;//倒计时服务时长，单位：秒

    protected OrderCountDownBean(Parcel in) {
        oid = in.readInt();
        countdown_time = in.readInt();
    }

    public static final Creator<OrderCountDownBean> CREATOR = new Creator<OrderCountDownBean>() {
        @Override
        public OrderCountDownBean createFromParcel(Parcel in) {
            return new OrderCountDownBean(in);
        }

        @Override
        public OrderCountDownBean[] newArray(int size) {
            return new OrderCountDownBean[size];
        }
    };

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getCountdown_time() {
        return countdown_time;
    }

    public void setCountdown_time(int countdown_time) {
        this.countdown_time = countdown_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(oid);
        dest.writeInt(countdown_time);
    }
}
