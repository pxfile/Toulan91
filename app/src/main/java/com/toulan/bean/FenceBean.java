package com.toulan.bean;

import com.fast.library.tools.EventCenter;
import com.toulan.utils.TConstant;
import com.toulan.utils.Tsp;

import org.greenrobot.eventbus.EventBus;

/**
 * 说明：围栏信息
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/28 23:41
 * <p/>
 * 版本：verson 1.0
 */
public class FenceBean {
    // 轨迹服务ID
    private long serviceId = TConstant.BaiDuTrace.TRACE_SERVICE_ID;
    // 创建者
    private String creator;//Sam_16
    // 围栏名称
    private String fenceName;
    // 围栏描述
    private String fenceDesc;
    // 监控对象列表（多个entityName，以英文逗号"," 分割）
    private String monitoredPersons;
    // 观察者列表（多个entityName，以英文逗号"," 分割）
    private String observers;
    // 围栏圆心（圆心位置, 格式 : "经度,纬度"）
    private String center;
    // 坐标类型 （1：GPS经纬度，2：国测局经纬度，3：百度经纬度）
    private int coordType = 3;
    // 围栏半径（单位 : 米）
    private double radius = TConstant.BaiDuTrace.Radius;
    private int oid;

    public FenceBean(){}

    public static FenceBean createFence(OrderBean bean){
        if (bean != null){
            UserBean userBean = Tsp.getUserInfo();
            FenceBean fenceBean = new FenceBean();
            fenceBean.setCenter(bean.getOrderDetail().getServiceLng(),bean.getOrderDetail().getServiceLat());
            fenceBean.setCreator(userBean.getFenceName());
            fenceBean.setFenceName(bean.getOrderDetail().getOid()+"");
            fenceBean.setFenceDesc(userBean.getFenceName()+"_"+bean.getOrderDetail().getOid());
            fenceBean.setMonitoredPersons(userBean.getFenceName());
            fenceBean.setObservers(userBean.getFenceName());
            fenceBean.setOid(bean.getOrderDetail().getOid());
            return fenceBean;
        }else {
            return null;
        }
    }

    public long getServiceId(){
        return serviceId;
    }

    public int getCoordType(){
        return coordType;
    }

    public double getRadius(){
        return radius;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getFenceName() {
        return fenceName;
    }

    public void setFenceName(String fenceName) {
        this.fenceName = fenceName;
    }

    public String getFenceDesc() {
        return fenceDesc;
    }

    public void setFenceDesc(String fenceDesc) {
        this.fenceDesc = fenceDesc;
    }

    public String getObservers() {
        return observers;
    }

    public void setObservers(String observers) {
        this.observers = observers;
    }

    public String getMonitoredPersons() {
        return monitoredPersons;
    }

    public void setMonitoredPersons(String monitoredPersons) {
        this.monitoredPersons = monitoredPersons;
    }

    public String getCenter() {
        return center;
    }

    /**
     * 设置围栏经纬度
     * @param lng 经度
     * @param lat 纬度
     */
    public void setCenter(String lng,String lat) {
        this.center = lng+ "," + lat;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public void create(){
        EventBus.getDefault().post(new EventCenter<FenceBean>(TConstant.EventType.createCircularFence,this));
    }

    @Override
    public String toString() {
        return "FenceBean{" +
                "serviceId=" + serviceId +
                ", creator='" + creator + '\'' +
                ", fenceName='" + fenceName + '\'' +
                ", fenceDesc='" + fenceDesc + '\'' +
                ", monitoredPersons='" + monitoredPersons + '\'' +
                ", observers='" + observers + '\'' +
                ", center='" + center + '\'' +
                ", coordType=" + coordType +
                ", radius=" + radius +
                ", oid=" + oid +
                '}';
    }
}
