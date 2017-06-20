package com.toulan.utils;

import com.fast.library.utils.ACache;
import com.fast.library.utils.AndroidInfoUtils;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.NumberUtils;
import com.fast.library.utils.SPUtils;
import com.fast.library.utils.StringUtils;
import com.toulan.TApplication;
import com.toulan.bean.UserBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 说明：Tsp
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/14 21:42
 * <p/>
 * 版本：verson 1.0
 */
public class Tsp {

    public static SPUtils sp = SPUtils.getInstance(TConstant.APP);
    public static ACache mCache = ACache.get(TApplication.getApplication());

    public interface KEY{
        int CACHE_TIME = 86400;//一天
        String userInfo = "userInfo";
        String orderTimerServerTime = "orderTimerServerTime";//服务时长
        String orderTimerAddTime = "orderTimerAddTime";//加时时长
        String orderPics = "orderPics";//订单图片
        String myPhone = "myPhone";//我的电话号码
        String lastPhoneRecord = "lastPhoneRecord";//上次电话时间记录
        String lastSmsRecord = "lastSmsRecord";//上次短信时间记录
    }

    public static void saveOrderTimerServerTime(int oid,int time){
        mCache.put(KEY.orderTimerServerTime + oid,String.valueOf(time),KEY.CACHE_TIME);
    }

    public static void saveOrderTimerAddTime(int oid,int time){
        mCache.put(KEY.orderTimerAddTime + oid,String.valueOf(time),KEY.CACHE_TIME);
    }

    public static void saveOrderPic(int oid, ArrayList<String> list){
        try {
            JSONArray array = new JSONArray();
            for (int i=0;i < list.size();i++){
                JSONObject object = new JSONObject();
                object.put("key"+i,list.get(i));
                array.put(object);
            }
            mCache.put(KEY.orderPics + oid,array.toString(),KEY.CACHE_TIME);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getOrderPic(int oid){
        ArrayList<String> list = new ArrayList<>();
        String pics = mCache.getAsString(KEY.orderPics + oid);
        if (!StringUtils.isEmpty(pics)){
            try {
                JSONArray array = new JSONArray(pics);
                for (int i=0;i < array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    list.add(object.getString("key"+i));
                }
                mCache.put(KEY.orderPics + oid,array.toString(),KEY.CACHE_TIME);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    public static int getOrderTimerServerTime(int oid){
        String time = mCache.getAsString(KEY.orderTimerServerTime + oid);
        return StringUtils.isEmpty(time) ? 0 : NumberUtils.toInt(time,0);
    }

    public static int getOrderTimerAddTime(int oid){
        String time = mCache.getAsString(KEY.orderTimerAddTime + oid);
        return StringUtils.isEmpty(time) ? 0 : NumberUtils.toInt(time,0);
    }

    public static void saveOrderSuccess(int oid){
        mCache.remove(KEY.orderTimerAddTime + oid);
        mCache.remove(KEY.orderTimerServerTime + oid);
    }

    public static boolean isLogin(){
        return getUserInfo() != null;
    }

    public static void exit(){
        saveUserInfo(null);
    }

    public static void saveUserInfo(UserBean bean){
        if (bean != null){
            sp.write(KEY.userInfo, GsonUtils.toJson(bean));
        }else {
            sp.write(KEY.userInfo, "");
        }
    }

    public static UserBean getUserInfo(){
        UserBean bean = GsonUtils.toBean(sp.readString(KEY.userInfo,""),UserBean.class);
        return bean;
    }

    public static String getUserSecret(){
        if (isLogin()){
            return getUserInfo().getUserSecret();
        }else {
            return "";
        }
    }

    public static void setMyPhoneNumber(String number){
        sp.write(KEY.myPhone,number);
    }

    public static String getMyPhoneNumber(){
        return sp.readString(KEY.myPhone,"");
    }
}
