package com.toulan.helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;

import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.ToolUtils;
import com.toulan.bean.CallLogBean;
import com.toulan.database.CallDao;
import com.toulan.ui.service.UploadSmsCallService;
import com.toulan.utils.TConstant;

import java.util.ArrayList;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 说明：获取通化记录
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/21 19:36
 * <p/>
 * 版本：verson 1.0
 */
public class CallPhoneHelper {

    /**
     * 查找一个时间点之后的所有通话记录
     *
     * @param oldTime
     * @param listener
     */
    public static void selectAllCallPhone(final CallDao dao,final Context context, final String oldTime, final OnLastCallListener listener) {
        if (listener == null) {
            return;
        }
        WriterHelper.writePhoneLog("开始查询通话记录");
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                            WriterHelper.writePhoneLog("权限不足！");
                            listener.callInfo(null);
                            return;
                        }
                        CallLogBean bean = null;
                        Cursor cursor = null;
                        ArrayList<CallLogBean> callList = new ArrayList<>();
                        try {
                            cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                do {
                                    //号码
                                    String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                                    //呼叫类型
                                    String type;
                                    switch (Integer.parseInt(cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE)))) {
                                        case CallLog.Calls.INCOMING_TYPE:
                                            type = "呼入";
                                            break;
                                        case CallLog.Calls.OUTGOING_TYPE:
                                            type = "呼出";
                                            break;
                                        case CallLog.Calls.MISSED_TYPE:
                                            type = "未接";
                                            break;
                                        default:
                                            type = "挂断";//应该是挂断.根据我手机类型判断出的
                                            break;
                                    }
                                    //呼叫时间
                                    String time = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                                    //联系人
                                    String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                                    //通话时间,单位:s
                                    String duration = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                                    if (oldTime.compareTo(time) < 0){
                                        bean = new CallLogBean(number, type, time, name, duration);
                                        callList.add(new CallLogBean(number, type, time, name, duration));
                                    }
                                } while (cursor.moveToNext());
                            }
                            if (!callList.isEmpty()){
                                if (dao.insertForBatch(callList)){
                                    WriterHelper.writePhoneLog("添加通话记录到数据库成功"+callList.size()+"条："+ GsonUtils.toJson(callList));
                                }else {
                                    WriterHelper.writePhoneLog("添加通话记录到数据库失败"+callList.size()+"条："+ GsonUtils.toJson(callList));
                                }
                            }else {
                                WriterHelper.writePhoneLog("没有最新的数据");
                            }
                            listener.callInfo(bean);
                        } catch (Exception e) {
                            listener.callInfo(null);
                            WriterHelper.writePhoneLog("查询通话记录异常："+ ToolUtils.collectErrorInfo(e));
                        } finally {
                            closeCursor(cursor);
                        }
                    }
                });
    }

    /**
     * 查找记录通话记录最新的一条数据
     */
    public static void selectCallPhoneTopOne(final Context context, final OnLastCallListener listener) {
        if (listener == null) {
            return;
        }
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                            WriterHelper.writePhoneLog("权限不足！");
                            listener.callInfo(null);
                            return;
                        }
                        String _id;
                        CallLogBean bean = null;
                        Cursor cursor = null;
                        Cursor cursor2 = null;
                        try {
                            cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                                    new String[]{"_id"}, null, null, "_id desc limit 1");
                            if (cursor != null && cursor.moveToFirst()) {
                                do {
                                    _id = cursor.getString(cursor.getColumnIndex(CallLog.Calls._ID));
                                } while (cursor.moveToNext());
                                if (!StringUtils.isEmpty(_id)) {
                                    cursor2 = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                                            new String[]{CallLog.Calls.NUMBER,
                                                    CallLog.Calls.TYPE,
                                                    CallLog.Calls.DATE,
                                                    CallLog.Calls.CACHED_NAME,
                                                    CallLog.Calls.DURATION}, "_id=?", new String[]{_id}, null);
                                    if (cursor2 != null && cursor2.moveToFirst()) {
                                        do {
                                            //号码
                                            String number = cursor2.getString(cursor2.getColumnIndex(CallLog.Calls.NUMBER));
                                            //呼叫类型
                                            String type;
                                            switch (Integer.parseInt(cursor2.getString(cursor2.getColumnIndex(CallLog.Calls.TYPE)))) {
                                                case CallLog.Calls.INCOMING_TYPE:
                                                    type = "呼入";
                                                    break;
                                                case CallLog.Calls.OUTGOING_TYPE:
                                                    type = "呼出";
                                                    break;
                                                case CallLog.Calls.MISSED_TYPE:
                                                    type = "未接";
                                                    break;
                                                default:
                                                    type = "挂断";//应该是挂断.根据我手机类型判断出的
                                                    break;
                                            }
                                            //呼叫时间
                                            String time = cursor2.getString(cursor2.getColumnIndexOrThrow(CallLog.Calls.DATE));
                                            //联系人
                                            String name = cursor2.getString(cursor2.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                                            //通话时间,单位:s
                                            String duration = cursor2.getString(cursor2.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                                            bean = new CallLogBean(number, type, time, name, duration);
                                        } while (cursor.moveToNext());
                                    }
                                }
                            }
                            listener.callInfo(bean);
                        } catch (Exception e) {
                            listener.callInfo(null);
                        } finally {
                            closeCursor(cursor, cursor2);
                        }
                    }
                });
    }

    private static void closeCursor(Cursor... cursors) {
        try {
            for (Cursor cursor : cursors) {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 上传数据
     * @param context
     */
    public static void uploadCall(Context context){
        Intent intent = new Intent(context, UploadSmsCallService.class);
        intent.setAction(TConstant.ACTION_UPLOAD);
        Bundle bundle2 = new Bundle();
        bundle2.putString(TConstant.ACTION_UPLOAD_PARAMS, TConstant.ACTION_UPLOAD_CALL);
        intent.putExtras(bundle2);
        context.startService(intent);
    }

    /**
     * 上传数据
     * @param context
     */
    public static void uploadSms(Context context){
        Intent intent = new Intent(context, UploadSmsCallService.class);
        intent.setAction(TConstant.ACTION_UPLOAD);
        Bundle bundle2 = new Bundle();
        bundle2.putString(TConstant.ACTION_UPLOAD_PARAMS, TConstant.ACTION_UPLOAD_SMS);
        intent.putExtras(bundle2);
        context.startService(intent);
    }

    public interface OnLastCallListener {
        void callInfo(CallLogBean bean);
    }
}
