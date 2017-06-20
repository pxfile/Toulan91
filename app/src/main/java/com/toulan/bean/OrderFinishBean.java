package com.toulan.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 说明：OrderFinishBean
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/20 23:10
 * <p/>
 * 版本：verson 1.0
 */
public class OrderFinishBean implements Parcelable{

    /**
     * oid : 8256
     * working_time : 0
     * my_far_money : 0
     * customer_far_money : 0
     * scene_notes :
     */

    private int oid;//订单ID
    @SerializedName("working_time")
    private int workingTime;//工作时长，单位：秒
    @SerializedName("my_far_money")
    private int myFarMoney;//加时费，单位：元
    @SerializedName("customer_far_money")
    private int customerFarMoney;//客户多付款项，单位：元
    @SerializedName("scene_notes")
    private String sceneNotes;//场景描述

    protected OrderFinishBean(Parcel in) {
        oid = in.readInt();
        workingTime = in.readInt();
        myFarMoney = in.readInt();
        customerFarMoney = in.readInt();
        sceneNotes = in.readString();
    }

    public static final Creator<OrderFinishBean> CREATOR = new Creator<OrderFinishBean>() {
        @Override
        public OrderFinishBean createFromParcel(Parcel in) {
            return new OrderFinishBean(in);
        }

        @Override
        public OrderFinishBean[] newArray(int size) {
            return new OrderFinishBean[size];
        }
    };

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(int workingTime) {
        this.workingTime = workingTime;
    }

    public int getMyFarMoney() {
        return myFarMoney;
    }

    public void setMyFarMoney(int myFarMoney) {
        this.myFarMoney = myFarMoney;
    }

    public int getCustomerFarMoney() {
        return customerFarMoney;
    }

    public void setCustomerFarMoney(int customerFarMoney) {
        this.customerFarMoney = customerFarMoney;
    }

    public String getSceneNotes() {
        return sceneNotes;
    }

    public void setSceneNotes(String sceneNotes) {
        this.sceneNotes = sceneNotes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(oid);
        dest.writeInt(workingTime);
        dest.writeInt(myFarMoney);
        dest.writeInt(customerFarMoney);
        dest.writeString(sceneNotes);
    }
}
