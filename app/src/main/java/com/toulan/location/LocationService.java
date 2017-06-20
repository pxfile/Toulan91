package com.toulan.location;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.toulan.utils.TConstant;

/**
 * 说明：定位服务
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/21 8:30
 * <p/>
 * 版本：verson 1.0
 */
public class LocationService implements BDLocationListener {

    private LocationClient client = null;
    @SuppressWarnings("unused")
    private LocationClientOption option , myOption;
    private LocationCallBack resultListener;
    //正在定位
    private boolean isLocated = false;

    public LocationService(Context context){
        if (client == null) {
            client = new LocationClient(context);
            client.setLocOption(getDefLocationOption());
        }
    }

    /**
     * 说明：获取默认的设置
     * @return
     */
    private LocationClientOption getDefLocationOption(){
        if (option == null) {
            option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
            option.setCoorType("bd09ll");
            //设置定位间隔，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
            option.setScanSpan(0);
            //设置是否需要地址信息
            option.setIsNeedAddress(true);
            //设置是否使用GPS
            option.setOpenGps(true);
            //设置是否当GPS有效时按照1S1次频率输出GPS结果
            option.setLocationNotify(false);
            //设置是否 需要位置语义结果
//			option.setIsNeedLocationDescribe(true);
            //设置是否需要POI结果
//			option.setIsNeedLocationPoiList(true);
            //定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            option.setIgnoreKillProcess(false);
            //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//			option.setEnableSimulateGps(false);
            //禁止使用缓存地址
			option.disableCache(true);
        }
        return option;
    }

    /**
     * 说明：是否正在定位
     * @return true:正在定位
     */
    public boolean isLocated(){
        return isLocated;
    }

    public void start(){
        synchronized (LocationService.class) {
            client.registerLocationListener(this);
            if (client != null && !client.isStarted()) {
                client.start();
            }else {
                client.requestLocation();
            }
        }
    }

    public void stop(){
        synchronized (LocationService.class) {
            if (client != null && client.isStarted()) {
                client.unRegisterLocationListener(this);
                client.stop();
            }
        }
    }

    public void setLocationListener(LocationCallBack callBack){
        this.resultListener = callBack;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        isLocated = false;
        if (resultListener != null) {
            LocationResultBean bean = new LocationResultBean(location);
            if (bean.isSuccess()) {
                resultListener.onLocSuccess(bean);
            }else {
                resultListener.onLocFail(bean.getErrorCode(), bean.getErrorInfo());
            }
        }
        stop();
    }

    /**
     * 说明：定位结果
     * @author fanly
     *
     */
    public interface LocationCallBack{
        void onLocSuccess(LocationResultBean bean);
        void onLocFail(int code, String errInfo);
    }

}
