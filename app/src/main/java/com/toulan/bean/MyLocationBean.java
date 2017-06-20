package com.toulan.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.toulan.utils.ServerTimeUtils;

/**
 * 说明：我的位置信息
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/24 13:35
 * <p/>
 * 版本：verson 1.0
 */
@DatabaseTable(tableName = "t_location")
public class MyLocationBean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "time")
    private String time;
    @DatabaseField(columnName = "lng")
    private String lng;
    @DatabaseField(columnName = "lat")
    private String lat;

    public MyLocationBean(){}

    public MyLocationBean(String lng,String lat){
        this.lat = lat;
        this.lng = lng;
        this.time = ServerTimeUtils.getInstance().getMillSeconds()+"";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
