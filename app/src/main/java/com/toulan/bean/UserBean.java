package com.toulan.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 说明：UserBean
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 14:28
 * <p/>
 * 版本：verson 1.0
 */
public class UserBean{

    /**
     * userid : 12
     * user_secret : 37e01614eae2a5a14e7bfbe4428fce1e
     * name : 赵凤霞
     * NO : 025233
     * IM_userid : Employee_185
     * IM_pwd : 123456
     * IM_master : 1-管理员 0-普通人
     */
    @SerializedName("userid")
    private String userId;
    @SerializedName("user_secret")
    private String userSecret;
    private String name;
    @SerializedName("NO")
    private String userNo;
    @SerializedName("IM_userid")
    private String imUserId;
    @SerializedName("IM_pwd")
    private String imPwd;
    @SerializedName("IM_master")
    private int imMaster;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getImUserId() {
        return imUserId;
    }

    public void setImUserId(String imUserId) {
        this.imUserId = imUserId;
    }

    public String getImPwd() {
        return imPwd;
    }

    public void setImPwd(String imPwd) {
        this.imPwd = imPwd;
    }

    public int getImMaster() {
        return imMaster;
    }

    public void setImMaster(int imMaster) {
        this.imMaster = imMaster;
    }

    public boolean isMaster(){
        return imMaster == 1;
    }

    public String getFenceName(){
        return getName() + "_" + getUserId();
    }
}
