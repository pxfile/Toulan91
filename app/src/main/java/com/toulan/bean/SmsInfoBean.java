package com.toulan.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.toulan.TApplication;
import com.toulan.utils.ServerTimeUtils;
import com.toulan.utils.Tsp;

/**
 * 说明：SmsInfoBean
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/19 22:02
 * <p/>
 * 版本：verson 1.0
 */
@DatabaseTable(tableName = "t_sms")
public class SmsInfoBean {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "content")
    private String content;
    @DatabaseField(columnName = "sender")
    private String sender;
    @DatabaseField(columnName = "createTime")
    private String createTime;
    @DatabaseField(columnName = "receiver")
    private String receiver;
    @DatabaseField(columnName = "smsType")
    private String smsType;//1：接收，2：发送

    public SmsInfoBean(){}

    public SmsInfoBean(String content, String sender,String createTime,String smsType) {
        this.content = content;
        this.sender = sender;
        this.createTime = createTime;
        this.smsType = smsType;
        if (Tsp.isLogin()){
            this.receiver = Tsp.getMyPhoneNumber();
        }
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean save(){
        return TApplication.getApplication().getSmsDao().insert(this);
    }

    @Override
    public String toString() {
        return "SmsInfoBean{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", createTime='" + createTime + '\'' +
                ", receiver='" + receiver + '\'' +
                ", smsType='" + smsType + '\'' +
                '}';
    }
}
