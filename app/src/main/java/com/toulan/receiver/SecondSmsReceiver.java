package com.toulan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.toulan.bean.SmsInfoBean;
import com.toulan.helper.WriterHelper;
import com.toulan.utils.Tsp;

/**
 * 说明：拦截短信
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/20 22:27
 * <p/>
 * 版本：verson 1.0
 */
public class SecondSmsReceiver extends BroadcastReceiver{

    private Context mContext;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_DELIVER_ACTION = "android.provider.Telephony.SMS_DELIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
//        WriterHelper.writeSmsLog("拦截到短信");
//        if (SMS_RECEIVED_ACTION.equals(intent.getAction()) || SMS_DELIVER_ACTION.equals(intent.getAction())) {
//            WriterHelper.writeSmsLog("开始接收到短信");
//            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                Object[] pdus = (Object[])bundle.get("pdus");
//                if (pdus != null && pdus.length > 0) {
//                    SmsMessage[] messages = new SmsMessage[pdus.length];
//                    for (int i = 0; i < pdus.length; i++) {
//                        byte[] pdu = (byte[]) pdus[i];
//                        messages[i] = SmsMessage.createFromPdu(pdu);
//                    }
//                    for (SmsMessage message : messages) {
//                        String content = message.getMessageBody();// 得到短信内容
//                        String sender = message.getOriginatingAddress();// 得到发信息的号码
//                        SmsInfoBean smsInfoBean = new SmsInfoBean(content,sender, Tsp.getMyPhoneNumber());
//                        boolean result = smsInfoBean.save();
//                        WriterHelper.writeSmsLog(smsInfoBean.toString());
//                        if (result){
//                            WriterHelper.writeSmsLog("保存成功");
//                        }else {
//                            WriterHelper.writeSmsLog("保存失败");
//                        }
//                    }
//                }
//            }else {
//                WriterHelper.writeSmsLog("短信：null");
//            }
//        }
    }
}
