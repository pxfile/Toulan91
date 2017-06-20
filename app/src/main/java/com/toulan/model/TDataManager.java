package com.toulan.model;

import com.fast.library.utils.AndroidInfoUtils;
import com.fast.library.utils.DateUtils;
import com.fast.library.utils.StringUtils;
import com.toulan.utils.TConstant;
import com.toulan.utils.TUtils;
import com.toulan.utils.Tsp;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * 说明：网络请求
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/14 21:17
 * <p/>
 * 版本：verson 1.0
 */
public class TDataManager {

    private static TDataManager sManager;
    private RequestQueue requestQueue = null;

    private TDataManager() {
        requestQueue = NoHttp.newRequestQueue();
    }

    public static TDataManager getInstance(){
        if (sManager == null){
            synchronized (TDataManager.class){
                if (sManager == null){
                    sManager = new TDataManager();
                }
            }
        }
        return sManager;
    }

    private String hmac(String s, String keyString) {
        String sEncodedString = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(key);

            byte[] bytes = mac.doFinal(s.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();

            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            sEncodedString = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return sEncodedString;
    }

    private String sign(TreeMap<String,String> map){
        if (map != null && !map.isEmpty()){
            StringBuilder sb = new StringBuilder();
            Iterator<String> iterator = map.keySet().iterator();
            String key;
            while (iterator.hasNext()){
                key = iterator.next();
                sb.append(key).append("=").append(StringUtils.utfEncode(map.get(key)));
            }
            return hmac(sb.toString().toLowerCase(), Tsp.getUserSecret());
        }else {
            return "";
        }
    }

    private Request<String> createStringRequest(String url, TreeMap<String,String> map,boolean isSign){
        Request<String> request = NoHttp.createStringRequest(url, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        if (map != null){
            request.add(map);
            if (isSign){
                request.add("sign",sign(map));
            }
        }
        return request;
    }

    private TreeMap<String,String> getTreeMap(){
        TreeMap<String,String> map = new TreeMap<>();
        map.put("deviceId", AndroidInfoUtils.getTerminalCode());
        map.put("timestampe", TUtils.getTime()+"");
        if (Tsp.isLogin()){
            map.put("userid", Tsp.getUserInfo().getUserId());
        }
        return map;
    }

    /**
     * 获取服务器时间
     * @param listener
     */
    public void getTime(OnModelListener listener){
        requestQueue.add(TConstant.Method.WHAT_SERVERTIME, createStringRequest(TConstant.Method.serverTime,getTreeMap(),false), listener);
    }

    /**
     * 获取短信验证码
     * @param listener
     */
    public void getSms(String phone,OnModelListener listener){
        TreeMap<String,String> map = getTreeMap();
        map.put("mobile",phone);
        requestQueue.add(TConstant.Method.WHAT_SMS, createStringRequest(TConstant.Method.sms,map,false), listener);
    }

    /**
     * 登录
     * @param listener
     */
    public void login(String phone,String code,OnModelListener listener){
        TreeMap<String,String> map = getTreeMap();
        map.put("mobile",phone);
        map.put("sms_code",code);
        map.put("timestamp", DateUtils.currentTimeMillis());
        requestQueue.add(TConstant.Method.WHAT_LOGIN, createStringRequest(TConstant.Method.login,map,false), listener);
    }

    /**
     * 更多
     * @param listener
     */
    public void moreUrl(OnResponseListener listener){
        TreeMap<String,String> map = getTreeMap();
        requestQueue.add(TConstant.Method.WHAT_MOREURL, createStringRequest(TConstant.Method.moreUrl,map,true), listener);
    }

    /**
     * 订单列表
     * @param listener
     */
    public void orderList(String days,OnResponseListener listener){
        TreeMap<String,String> map = getTreeMap();
        map.put("days",days);
        requestQueue.add(TConstant.Method.WHAT_ORDERLIST, createStringRequest(TConstant.Method.orderList,map,true), listener);
    }

    /**
     * 拨打电话
     * @param listener
     */
    public void callPhone(int oid,OnResponseListener listener){
        TreeMap<String,String> map = getTreeMap();
        map.put("oid",String.valueOf(oid));
        requestQueue.add(TConstant.Method.WHAT_CALLPHONE, createStringRequest(TConstant.Method.callPhone,map,true), listener);
    }

    /**
     * 定位
     * @param listener
     */
    public void position(String location,String lng,String lat,OnResponseListener listener){
        TreeMap<String,String> map = getTreeMap();
        map.put("location",String.valueOf(location));
        map.put("lng",lng);
        map.put("lat",lat);
        map.put("time",DateUtils.currentTimeMillis());
        requestQueue.add(TConstant.Method.WHAT_LOCATION, createStringRequest(TConstant.Method.location,map,true), listener);
    }

    /**
     * 确认订单
     * @param listener
     */
    public void orderSave(int oid, int workingTime, int addTime, int addMoney, int customMoney, String notes,OnResponseListener listener){
        TreeMap<String,String> map = getTreeMap();
        map.put("oid",String.valueOf(oid));
        map.put("working_time",String.valueOf(workingTime));
        map.put("add_time",String.valueOf(addTime));
        map.put("my_far_money",String.valueOf(addMoney));
        map.put("customer_far_money",String.valueOf(customMoney));
        map.put("scene_notes",notes);
        requestQueue.add(TConstant.Method.WHAT_ORDER_SAVE, createStringRequest(TConstant.Method.ORDER_SAVE,map,true), listener);
    }

    /**
     * 上传凭证
     * @param listener
     */
    public void uploadToken(int oid,OnResponseListener listener){
        TreeMap<String,String> map = getTreeMap();
        map.put("oid",String.valueOf(oid));
        map.put("get_token","1");
        requestQueue.add(TConstant.Method.WHAT_UPLOAD_TOKEN, createStringRequest(TConstant.Method.UPLOAD_TOKEN,map,true), listener);
    }

    /**
     * 上传文件
     * @param listener
     */
    public void uploadFile(String uploadToken, ArrayList<String> list, OnResponseListener listener){
        Request<String> request = NoHttp.createStringRequest(TConstant.Method.UPLOAD_FILE, RequestMethod.POST);
        request.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
        request.add("uploadToken",uploadToken);
        for (int i = 1;i <= list.size() ; i++){
            File file = new File(list.get(i-1));
            request.add("file"+i,file);
        }
        requestQueue.add(TConstant.Method.WHAT_UPLOAD_FILE, request, listener);
    }

    /**
     * 版本更新
     * @param listener
     */
    public void checkUpdateApp(OnResponseListener listener){
        TreeMap<String,String> map = getTreeMap();
        requestQueue.add(TConstant.Method.WHAT_CHECK_APP, createStringRequest(TConstant.Method.CHECK_APP,map,false), listener);
    }

    /**
     * 上传通讯记录
     * @param callTime
     * @param incoming 对方的号码
     * @param explout  我的号码
     * @param duration
     */
    public Response<String> uploadCall(String callTime,String incoming,String explout,String duration,String type){
        TreeMap<String,String> map = getTreeMap();
        map.put("type",type);
//        map.put("type",type);
        map.put("call_time",callTime);
        map.put("incoming",incoming);
        map.put("explout",explout);
        map.put("duration",duration);
        Request<String> request = createStringRequest(TConstant.Method.UPLOAD_CALL,map,true);
        return NoHttp.startRequestSync(request);
    }

    /**
     * 上传短信记录
     * @param sender
     * @param receiver
     * @param time
     * @param content
     */
    public Response<String> uploadSms(String sender,String receiver,String time,String content){
        TreeMap<String,String> map = getTreeMap();
        map.put("sender",sender);
        map.put("receiver",receiver);
        map.put("happen_time",time);
        map.put("content",content);
        Request<String> request = createStringRequest(TConstant.Method.UPLOAD_SMS,map,true);
        return NoHttp.startRequestSync(request);
    }


    /**
     * 围栏报警
     * @param listener
     */
    public void uploadFence(String type,String time,String oid,OnResponseListener listener){
        TreeMap<String,String> map = getTreeMap();
        map.put("type",type);
        map.put("happen_time",time);
        map.put("oid",oid);
        requestQueue.add(TConstant.Method.WHAT_UPLOAD_FENCE, createStringRequest(TConstant.Method.UPLOAD_FENCE,map,true), listener);
    }
}
