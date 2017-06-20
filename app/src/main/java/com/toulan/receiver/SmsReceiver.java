package com.toulan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明：SMSBroadcastreceiver
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 14:43
 * <p/>
 * 版本：verson 1.0
 */
public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private OnSmsListener mListener;

    public interface OnSmsListener{
        void getSms(String code);
    }

    public SmsReceiver(OnSmsListener listener) {
        mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            if (pdus != null && pdus.length > 0) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[0]);
                Pattern pattern = Pattern.compile("[^0-9]");
                Matcher matcher = pattern.matcher(smsMessage.getDisplayMessageBody());
                if (mListener != null){
                    mListener.getSms(matcher.replaceAll("").substring(2));
                }
            }
        }
    }
}
