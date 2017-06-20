package com.toulan.ui.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.fast.library.utils.DateUtils;
import com.fast.library.utils.StringUtils;
import com.toulan.TApplication;
import com.toulan.helper.LocationHelper;
import com.toulan.helper.WriterHelper;
import com.toulan.location.LocationResultBean;
import com.toulan.location.LocationService;
import com.toulan.utils.TConstant;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.toulan.utils.TUtils.getTime;

/**
 * 说明：后台定位
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/21 9:03
 * <p/>
 * 版本：verson 1.0
 */
public class TLocationService extends IntentService implements LocationService.LocationCallBack {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public TLocationService() {
        super("TLocationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        TApplication.getApplication().getLocationService().setLocationListener(this);
        TApplication.getApplication().getLocationService().start();
    }

    @Override
    public void onLocSuccess(final LocationResultBean bean) {
        if (checkIsInServerTime()){
            Observable.just(bean)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<LocationResultBean>() {
                        @Override
                        public void call(LocationResultBean locationResultBean) {
                            if (locationResultBean != null && !StringUtils.isEmpty(locationResultBean.getLatitude() + "") && !StringUtils.isEmpty(locationResultBean.getLongitude() + "")) {
                                LocationHelper.saveLocation(TLocationService.this, String.valueOf(locationResultBean.getLongitude()), String.valueOf(locationResultBean.getLatitude()));
                                WriterHelper.writeLocationLog("定位成功；定位地址："+locationResultBean.getAddress()+" 经纬度："+locationResultBean.getLatitude()+":"+locationResultBean.getLongitude());
                            }else {
                                if (locationResultBean != null){
                                    WriterHelper.writeLocationLog("定位失败；"+locationResultBean.getErrorInfo());
                                }else {
                                    WriterHelper.writeLocationLog("定位失败；null");
                                }
                            }
                        }
                    });
        }
    }

    private boolean checkIsInServerTime(){
        long current = DateUtils.getStrToLong(DateUtils.getLongToStr(getTime(),DateUtils.FORMAT_HH_MM_1),DateUtils.FORMAT_HH_MM_1);
        long start = DateUtils.getStrToLong(TConstant.Postion.LOCATION_START,DateUtils.FORMAT_HH_MM_1);
        long end = DateUtils.getStrToLong(TConstant.Postion.LOCATION_END,DateUtils.FORMAT_HH_MM_1);
        return current >= start && current <= end;
    }

    @Override
    public void onLocFail(int code, String errInfo) {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
