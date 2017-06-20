package com.toulan.bean;

/**
 * 说明：LocationInfoBean
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/24 13:56
 * <p/>
 * 版本：verson 1.0
 */
public class LocationInfoBean {
    private String time;
    private String lng;
    private String lat;

    public LocationInfoBean(){}

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
