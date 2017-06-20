package com.toulan.bean;

import com.fast.library.utils.StringUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.toulan.TApplication;
import com.toulan.utils.Tsp;

/**
 * 说明：通化记录
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/21 0:05
 * <p/>
 * 版本：verson 1.0
 */
@DatabaseTable(tableName = "t_call")
public class CallLogBean{
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "numberPhone")
    private String numberPhone;//对方号码
    @DatabaseField(columnName = "callType")
    private String callType;
    @DatabaseField(columnName = "callTime")
    private String callTime;
    @DatabaseField(columnName = "callName")
    private String callName;//我的号码
    @DatabaseField(columnName = "duration")
    private String duration;

    public CallLogBean(){}

    public CallLogBean(String numberPhone, String callType, String callTime, String callName, String duration) {
        setNumberPhone(numberPhone);
        setCallType(callType);
        setCallTime(callTime);
        setCallName(callName);
        setDuration(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        if (StringUtils.isEmpty(callType)){
            callType = "";
        }
        this.callType = callType;
    }

    public String getCallName() {
        return callName;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        if (StringUtils.isEmpty(numberPhone)){
            numberPhone = "";
        }
        this.numberPhone = numberPhone;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        if (StringUtils.isEmpty(callTime)){
            callTime = "";
        }
        this.callTime = callTime;
    }

    public void setCallName(String callName) {
        if (StringUtils.isEmpty(callName)){
            callName = Tsp.getMyPhoneNumber();
        }
        this.callName = callName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        if (StringUtils.isEmpty(duration)){
            duration = "";
        }
        this.duration = duration;
    }

    public String getPhoneType(){
        if (StringUtils.isEquals(getCallType(),"呼入")){
            return "2";
        }else {
            return "1";
        }
    }

    @Override
    public String toString() {
        return "CallLogBean{" +
                "numberPhone='" + numberPhone + '\'' +
                ", callType='" + callType + '\'' +
                ", callTime='" + callTime + '\'' +
                ", callName='" + callName + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
