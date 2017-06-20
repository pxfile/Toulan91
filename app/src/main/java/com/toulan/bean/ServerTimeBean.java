package com.toulan.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 说明：服务器时间
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/14 23:45
 * <p/>
 * 版本：verson 1.0
 */
public class ServerTimeBean {

    /**
     * time_day : 2016-09-14 23:45:19
     * time_stamp : 1473867919
     */
    @SerializedName("time_day")
    private String time;
    @SerializedName("time_stamp")
    private int timeStamp;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }
}
