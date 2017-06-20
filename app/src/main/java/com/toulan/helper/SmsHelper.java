package com.toulan.helper;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.NumberUtils;
import com.toulan.bean.SmsInfoBean;
import com.toulan.database.SmsDao;

import java.util.ArrayList;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 说明：短信帮助类
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/26 22:48
 * <p/>
 * 版本：verson 1.0
 */
public class SmsHelper {

    public interface OneLastSmsListener {
        void smsInfo(SmsInfoBean bean);
    }

    /**
     * 查找最近的一条
     * @param context
     * @param listener
     */
    public static void findOneLastSms(final Context context,final OneLastSmsListener listener) {
        if (listener == null) {
            return;
        }
        WriterHelper.writeSmsLog("开始查询最近一条短信记录");
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                            WriterHelper.writePhoneLog("查询最近一条短信记录权限不足！");
                            listener.smsInfo(null);
                            return;
                        }
                        Uri SMS_INBOX = Uri.parse("content://sms/");
                        ContentResolver cr = context.getContentResolver();
                        Cursor cur = cr.query(SMS_INBOX, null, null, null, null);
                        if (null != cur && cur.moveToFirst()){
                            SmsInfoBean bean = null;
                            do {
                                String number = cur.getString(cur.getColumnIndex("address"));// 手机号
//                                String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
                                String body = cur.getString(cur.getColumnIndex("body"));
                                String date = cur.getString(cur.getColumnIndex("date"));
                                String type = cur.getString(cur.getColumnIndex("type"));
                                bean = new SmsInfoBean(body, number,date,type);
                                WriterHelper.writeSmsLog("查找到最近一条数据:"+bean.toString());
                                break;
                            } while (cur.moveToNext());
                            listener.smsInfo(bean);
                        }else {
                            listener.smsInfo(null);
                            WriterHelper.writeSmsLog("最近一条没有数据");
                        }
                        if (cur != null && !cur.isClosed()){
                            cur.close();
                        }
                    }
                });
    }

    /**
     * 查找所有短信
     * @param smsDao
     * @param context
     * @param time
     */
    public static void findAllSms(final SmsDao smsDao, final Context context, final long time, final OneLastSmsListener listener) {
        if (listener == null){
            return;
        }
        WriterHelper.writeSmsLog("开始查询所有短信记录");
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                            WriterHelper.writePhoneLog("查询所有短信记录权限不足！");
                            return;
                        }
                        Uri SMS_INBOX = Uri.parse("content://sms/");
                        ContentResolver cr = context.getContentResolver();
                        Cursor cur = cr.query(SMS_INBOX, null, null, null, null);
                        SmsInfoBean bean = null;
                        if (null != cur && cur.moveToFirst()){
                            ArrayList<SmsInfoBean> list = new ArrayList<SmsInfoBean>();
                            do {
                                String number = cur.getString(cur.getColumnIndex("address"));// 手机号
//                                String name = cur.getString(cur.getColumnIndex("person"));// 联系人姓名列表
                                String body = cur.getString(cur.getColumnIndex("body"));
                                String date = cur.getString(cur.getColumnIndex("date"));
                                String type = cur.getString(cur.getColumnIndex("type"));
                                if (NumberUtils.toLong(date) > time){
                                    bean = new SmsInfoBean(body, number,date,type);
                                    list.add(bean);
                                }
                                break;
                            } while (cur.moveToNext());
                            if (list != null && !list.isEmpty()){
                                if (smsDao.insertForBatch(list)){
                                    WriterHelper.writePhoneLog("短信数据保存本地成功！"+list.size()+"条！"+ GsonUtils.toJson(list));
                                }else {
                                    WriterHelper.writePhoneLog("短信数据保存本地失败！"+list.size()+"条！"+ GsonUtils.toJson(list));
                                }
                            }else {
                                WriterHelper.writePhoneLog("没有最新的短信数据！");
                            }
                        }else {
                            WriterHelper.writePhoneLog("最新的短信数据null！");
                        }
                        listener.smsInfo(bean);
                        if (cur != null && !cur.isClosed()){
                            cur.close();
                        }
                    }
                });
    }
}
