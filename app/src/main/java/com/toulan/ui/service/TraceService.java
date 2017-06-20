package com.toulan.ui.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnGeoFenceListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.fast.library.tools.EventCenter;
import com.fast.library.utils.DateUtils;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.ToolUtils;
import com.toulan.bean.FenceBean;
import com.toulan.bean.UserBean;
import com.toulan.helper.WriterHelper;
import com.toulan.model.OnStringListener;
import com.toulan.model.TDataManager;
import com.toulan.utils.TConstant;
import com.toulan.utils.Tsp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 说明：百度鹰眼
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/10/24 21:50
 * <p/>
 * 版本：verson 1.0
 */
public class TraceService extends Service{

    private static String TAG = "TraceService";
//    轨迹服务
    private static Trace sTrace = null;
//     轨迹服务类型
//    0 : 不建立socket长连接，
//    1 : 建立socket长连接但不上传位置数据，
//    2 : 建立socket长连接并上传位置数据）
    private int traceType = 2;
//    轨迹服务客户端
    public static LBSTraceClient client = null;
    // Entity监听器
    public static OnEntityListener entityListener = null;
    // 开启轨迹服务监听器
    protected OnStartTraceListener startTraceListener = null;
    // 停止轨迹服务监听器
    protected static OnStopTraceListener stopTraceListener = null;
    // 采集周期（单位 : 秒）
    private int gatherInterval = TConstant.BaiDuTrace.gatherInterval;
    // 设置打包周期(单位 : 秒)
    private int packInterval = TConstant.BaiDuTrace.packInterval;

    protected static boolean isTraceStart = false;

    private OnGeoFenceListener mOnGeoFenListener = new OnGeoFenceListener() {
        @Override
        public void onRequestFailedCallback(String s) {

        }
    };

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isTraceStart) {
            init();
        }
        return super.onStartCommand(intent, START_FLAG_RETRY, startId);
    }

    //被销毁时反注册广播接收器
    public void onDestroy() {
        stopTrace();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 初始化
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void init() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        UserBean bean = Tsp.getUserInfo();
        // 初始化轨迹服务客户端
        client = new LBSTraceClient(this);
        // 设置定位模式
        client.setLocationMode(LocationMode.High_Accuracy);
        // 初始化轨迹服务
        sTrace = new Trace(this, TConstant.BaiDuTrace.TRACE_SERVICE_ID, bean.getFenceName(), traceType);
        // 采集周期,上传周期
        client.setInterval(gatherInterval, packInterval);
        // 设置http请求协议类型0:http,1:https
        client.setProtocolType(0);
        // 初始化监听器
        initListener();
        // 启动轨迹上传
        startTrace();
    }
    // 开启轨迹服务
    private void startTrace() {
        // 通过轨迹服务客户端client开启轨迹服务
        client.startTrace(sTrace, startTraceListener);
    }

    // 停止轨迹服务
    public static void stopTrace() {
        // 通过轨迹服务客户端client停止轨迹服务
        if(isTraceStart){
            client.stopTrace(sTrace, stopTraceListener);
        }
    }

    // 初始化监听器
    private void initListener() {
        initOnEntityListener();
        // 初始化开启轨迹服务监听器
        initOnStartTraceListener();
        // 初始化停止轨迹服务监听器
        initOnStopTraceListener();
    }


    /**
     * 初始化OnStartTraceListener
     */
    private void initOnStartTraceListener() {
        // 初始化startTraceListener
        startTraceListener = new OnStartTraceListener() {
            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            public void onTraceCallback(int arg0, String arg1) {
                WriterHelper.writeFenceLog("开启轨迹回调接口 [消息编码 : " + arg0 + "，消息内容 : " + arg1 + "]");
                if (0 == arg0 || 10006 == arg0) {
                    isTraceStart = true;
                    WriterHelper.writeFenceLog("开启轨迹成功");
                }else {
                    WriterHelper.writeFenceLog("开启轨迹失败");
                }
            }
            // 轨迹服务推送接口（用于接收服务端推送消息，type : 消息类型，data : 消息内容，详情查看类参考）
            public void onTracePushCallback(byte type, String data) {
//                围栏报警
                WriterHelper.writeFenceLog("轨迹服务推送接口消息 [消息类型 : " + type + "，消息内容 : " + data + "]");
                if (0x03 == type || 0x04 == type) {
                    try {
                        JSONObject dataJson = new JSONObject(data);
                        if (null != dataJson) {
                            String mPerson = dataJson.getString("monitored_person");
//                            String action = dataJson.getInt("action") == 1 ? "进入" : "离开";
                            int action = dataJson.getInt("action");
                            String fence = dataJson.getString("fence");
                            long time = dataJson.getLong("time");
                            String date = DateUtils.getLongToStr(time,DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_3);
                            long fenceId = dataJson.getLong("fence_id");
                            WriterHelper.writeFenceLog(
                                    "监控对象[" + mPerson + "]于" + date + " [" + action + "][" + fenceId + "号]围栏");
                            uploadFenceInfo(mPerson,action,time,fenceId,fence);
                        }

                    } catch (JSONException e) {
                        WriterHelper.writeFenceLog(ToolUtils.collectErrorInfo(e));
                    }
                }
            }
        };
    }

    /**
     * 创建围栏
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void createCircularFence(EventCenter<FenceBean> event){
        if (TConstant.EventType.createCircularFence.equals(event.type)){
            FenceBean fenceBean = event.data;
            if (client != null && fenceBean != null){
                //创建围栏
                client.createCircularFence(fenceBean.getServiceId(), fenceBean.getCreator(), fenceBean.getFenceName(),
                        fenceBean.getFenceDesc(), fenceBean.getMonitoredPersons(), fenceBean.getObservers(),
                        null, 0, null, null, fenceBean.getCoordType(), fenceBean.getCenter(), fenceBean.getRadius(), 0, 0,mOnGeoFenListener);
                WriterHelper.writeFenceLog("创建围栏："+fenceBean.toString());
            }
        }
    }

    // 初始化OnStopTraceListener
    private void initOnStopTraceListener() {
        stopTraceListener = new OnStopTraceListener() {
            // 轨迹服务停止成功
            public void onStopTraceSuccess() {
                WriterHelper.writeFenceLog("停止轨迹服务成功");
                isTraceStart = false;
                stopSelf();
            }
            // 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
            public void onStopTraceFailed(int arg0, String arg1) {
                WriterHelper.writeFenceLog("停止轨迹服务接口消息 [错误编码 : " + arg0 + "，消息内容 : " + arg1 + "]");
            }
        };
    }

    // 初始化OnEntityListener
    private void initOnEntityListener() {
        entityListener = new OnEntityListener() {
            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                WriterHelper.writeFenceLog("entity请求失败回调接口消息 : " + arg0);
            }
            // 添加entity回调接口
            @Override
            public void onAddEntityCallback(String arg0) {
                WriterHelper.writeFenceLog("添加entity回调接口消息 : " + arg0);
            }
            // 查询entity列表回调接口
            @Override
            public void onQueryEntityListCallback(String message) {
                WriterHelper.writeFenceLog("onQueryEntityListCallback : " + message);
            }
            @Override
            public void onReceiveLocation(TraceLocation location) {
            }
        };
    }

    /**
     * 上传围栏信息
     * @param person
     * @param action
     * @param time
     * @param fenceId
     */
    private void uploadFenceInfo(final String person,final int action,final long time,final long fenceId,String fence){
        TDataManager.getInstance().uploadFence(String.valueOf(action), String.valueOf(time), fence, new OnStringListener() {
            @Override
            public void onSuccess(int what, String node) {
                if (GsonUtils.optInt(node,"code") == 1){
                    WriterHelper.writeFenceLog(person+"--"+action+"--"+time+"--"+fenceId+"--上传成功");
                }else {
                    WriterHelper.writeFenceLog(person+"--"+action+"--"+time+"--"+fenceId+"--上传失败"+node);
                }
            }

            @Override
            public void onFail(int what, int code, String msg) {
                WriterHelper.writeFenceLog(person+"--"+action+"--"+time+"--"+fenceId+"--上传失败"+msg);
            }
        });
    }

}
