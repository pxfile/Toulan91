package com.toulan.helper;

import android.content.Context;

import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.ToolUtils;
import com.toulan.bean.LocationInfoBean;
import com.toulan.bean.MyLocationBean;
import com.toulan.database.LocationDao;
import com.toulan.model.OnStringListener;
import com.toulan.model.TDataManager;
import com.toulan.utils.TConstant;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 说明：LocationHelper
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/24 13:45
 * <p/>
 * 版本：verson 1.0
 */
public class LocationHelper {

    public static LocationDao mLocationDao;

    private static void checkDao(Context context){
        if (mLocationDao == null){
            mLocationDao = new LocationDao(context);
        }
    }

    public static void saveLocation(Context context,String lng,String lat){
        try {
            checkDao(context);
            MyLocationBean bean = new MyLocationBean(lng,lat);
            mLocationDao.insert(bean);
            List<MyLocationBean> list = mLocationDao.queryForAll();
            if (list != null && list.size() >= TConstant.Postion.UPDATE_SIZE){
                submitLocation(context);
            }else {
                WriterHelper.writeLocationLog("save:为空！");
            }
        }catch (Exception e){
            WriterHelper.writeLocationLog("保存位置信息异常；"+ToolUtils.collectErrorInfo(e));
        }
    }

    public static void submitLocation(final Context context){
        WriterHelper.writeLocationLog("开始提交位置信息");
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        try {
                            checkDao(context);
                            final ArrayList<MyLocationBean> list = (ArrayList<MyLocationBean>) mLocationDao.queryForAll();
                            if (list != null && !list.isEmpty()){
                                ArrayList<LocationInfoBean> locationInfoBeens = GsonUtils.toList(GsonUtils.toJson(list),LocationInfoBean.class);
                                final String json = GsonUtils.toJson(locationInfoBeens);
                                if (!StringUtils.isEmpty(json) && locationInfoBeens.size() > 0){
                                    TDataManager.getInstance().position(json, locationInfoBeens.get(0).getLng(),
                                            locationInfoBeens.get(0).getLat(), new OnStringListener() {
                                                @Override
                                                public void onSuccess(int what, String node) {
                                                    if (!mLocationDao.deleteForBatch(list)){
                                                        WriterHelper.writeLocationLog("上传服务器异常；批量删除数据异常；"+json);
                                                    }
                                                }

                                                @Override
                                                public void onFail(int what, int code, String msg) {
                                                    WriterHelper.writeLocationLog("上传服务器异常；"+ msg);
                                                }
                                            });
                                }else {
                                    WriterHelper.writeLocationLog("查询位置信息为空2");
                                }
                            }else {
                                WriterHelper.writeLocationLog("查询位置信息为空1");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            WriterHelper.writeLocationLog("上传位置信息异常；"+ ToolUtils.collectErrorInfo(e));
                        }
                    }
                });
    }

}
