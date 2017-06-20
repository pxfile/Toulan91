package com.toulan.ui.service;

import android.app.IntentService;
import android.content.Intent;

import com.fast.library.utils.DateUtils;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.NetUtils;
import com.fast.library.utils.StringUtils;
import com.toulan.bean.CallLogBean;
import com.toulan.bean.SmsInfoBean;
import com.toulan.database.CallDao;
import com.toulan.database.SmsDao;
import com.toulan.helper.WriterHelper;
import com.toulan.model.TDataManager;
import com.toulan.utils.TConstant;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;

/**
 * 说明：上传
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/21 23:48
 * <p/>
 * 版本：verson 1.0
 */
public class UploadSmsCallService extends IntentService{

    private CallDao callDao = null;
    private SmsDao smsDao = null;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public UploadSmsCallService() {
        super("UploadSmsCallService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (!NetUtils.isNetConnected()){
            return;
        }
        String params = intent.getStringExtra(TConstant.ACTION_UPLOAD_PARAMS);
        if (TConstant.ACTION_UPLOAD_CALL.equals(params)){
            WriterHelper.writePhoneLog("开始上传通话记录");
            //上传通话记录
            if (callDao == null){
                callDao = new CallDao(this);
            }
            ArrayList<CallLogBean> callList = (ArrayList<CallLogBean>) callDao.queryForAll();
            if (callList != null && !callList.isEmpty()){
                for (CallLogBean bean:callList){
                    Response<String> response = TDataManager.getInstance().uploadCall(bean.getCallTime(),bean.getNumberPhone(),bean.getCallName(),bean.getDuration(),bean.getPhoneType());
                    if (response.isSucceed()){
                        String data = response.get();
                        int code = GsonUtils.optInt(data,"code");
                        if (!StringUtils.isEmpty(data) && code == 1){
                            //上传成功
                            WriterHelper.writePhoneLog("上传成功："+bean.toString());
                            callDao.delete(bean);
                        }else if (code == 1006){//重复数据
                            //重复数据
                            callDao.delete(bean);
                            WriterHelper.writePhoneLog("上传重复数据："+data+"=="+bean.toString());
                        }else {
                            //上传失败
                            WriterHelper.writePhoneLog("上传失败："+data+"=="+bean.toString());
                        }
                    }else {
                       //上传失败
                       WriterHelper.writePhoneLog("上传失败："+response.getException().toString()+"=="+bean.toString());
                    }
                }
            }else {
                WriterHelper.writePhoneLog("上传通话记录为空！");
            }
        }else if (TConstant.ACTION_UPLOAD_SMS.equals(params)){
            WriterHelper.writeSmsLog("开始上传短信记录");
            //上传通话记录
            if (smsDao == null){
                smsDao = new SmsDao(this);
            }
            ArrayList<SmsInfoBean> smsList = (ArrayList<SmsInfoBean>) smsDao.queryForAll();
            if (smsList != null && !smsList.isEmpty()){
                for (SmsInfoBean bean:smsList){
                    WriterHelper.writeSmsLog("查询到数据："+bean.toString());
                    Response<String> response = TDataManager.getInstance().uploadSms(bean.getSender(),bean.getReceiver(), bean.getCreateTime(),bean.getContent());
                    if (response.isSucceed()){
                        String data = response.get();
                        int code = GsonUtils.optInt(data,"code");
                        if (!StringUtils.isEmpty(data) && code == 1){
                            //上传成功
                            WriterHelper.writeSmsLog("上传成功："+bean.toString());
                            smsDao.delete(bean);
                        }else if (code == 1006){
                            //重复数据
                            smsDao.delete(bean);
                            WriterHelper.writeSmsLog("上传重复数据："+data+"=="+bean.toString());
                        }else {
                            //上传失败
                            WriterHelper.writeSmsLog("上传失败："+data+"=="+bean.toString());
                        }
                    }else {
                        //上传失败
                        WriterHelper.writeSmsLog("上传失败："+response.getException().toString()+"=="+bean.toString());
                    }
                }
            }else {
                WriterHelper.writeSmsLog("上传短信记录为空！");
            }
        }
    }
}
