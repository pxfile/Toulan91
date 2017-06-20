package com.toulan.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 说明：job_order_detail_normal
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/20 23:08
 * <p/>
 * 版本：verson 1.0
 */
public class OrderDetailBean implements Parcelable{

    /**
     * oid : 8256
     * categ : 日常清洁22
     * service_time : 2016-09-15 14:00-18:00
     * service_address : 宝山区大观园C区de
     * service_lng : 121.409041
     * service_lat : 31.3986226
     * address_type : 家庭-两房两厅
     * acreage : 100
     * workther : 赵凤霞
     * operator : 张林
     * remark : 新订单
     */

    private int oid;//订单ID
    private String categ;//服务项目
    @SerializedName("service_time")
    private String serviceTime;//服务时间端
    @SerializedName("service_address")
    private String serviceAddress;//服务地址
    @SerializedName("service_lng")
    private String serviceLng;//服务地址经度
    @SerializedName("service_lat")
    private String serviceLat;//服务地址纬度
    @SerializedName("address_type")
    private String addressType;//地址类型
    private int acreage;//房屋面积
    private String workther;//出单人
    private String operator;//派单人
    private String remark;//订单备注

    protected OrderDetailBean(Parcel in) {
        oid = in.readInt();
        categ = in.readString();
        serviceTime = in.readString();
        serviceAddress = in.readString();
        serviceLng = in.readString();
        serviceLat = in.readString();
        addressType = in.readString();
        acreage = in.readInt();
        workther = in.readString();
        operator = in.readString();
        remark = in.readString();
    }

    public static final Creator<OrderDetailBean> CREATOR = new Creator<OrderDetailBean>() {
        @Override
        public OrderDetailBean createFromParcel(Parcel in) {
            return new OrderDetailBean(in);
        }

        @Override
        public OrderDetailBean[] newArray(int size) {
            return new OrderDetailBean[size];
        }
    };

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getCateg() {
        return categ;
    }

    public void setCateg(String categ) {
        this.categ = categ;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getServiceLng() {
        return serviceLng;
    }

    public void setServiceLng(String serviceLng) {
        this.serviceLng = serviceLng;
    }

    public String getServiceLat() {
        return serviceLat;
    }

    public void setServiceLat(String serviceLat) {
        this.serviceLat = serviceLat;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public int getAcreage() {
        return acreage;
    }

    public void setAcreage(int acreage) {
        this.acreage = acreage;
    }

    public String getWorkther() {
        return workther;
    }

    public void setWorkther(String workther) {
        this.workther = workther;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(oid);
        dest.writeString(categ);
        dest.writeString(serviceTime);
        dest.writeString(serviceAddress);
        dest.writeString(serviceLng);
        dest.writeString(serviceLat);
        dest.writeString(addressType);
        dest.writeInt(acreage);
        dest.writeString(workther);
        dest.writeString(operator);
        dest.writeString(remark);
    }
}
