package com.toulan;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.aop.AdviceBinder;
import com.alibaba.mobileim.aop.PointCutEnum;
import com.alibaba.wxlib.util.SysUtil;
import com.fast.library.FastFrame;
import com.fast.library.utils.UIUtils;
import com.liulishuo.filedownloader.FileDownloader;
import com.tencent.bugly.Bugly;
import com.toulan.database.SmsDao;
import com.toulan.location.LocationService;
import com.toulan.openIM.ChattingOperationCustomSample;
import com.toulan.openIM.ContactsOperationCustomSample;
import com.toulan.openIM.ContactsUiCustom;
import com.toulan.openIM.ConversationListOperationCustomSample;
import com.toulan.openIM.ConversationListUI;
import com.toulan.openIM.NotificationInitSampleHelper;
import com.toulan.openIM.SelectTribeAtMemberSample;
import com.toulan.openIM.SendAtMsgDetailUISample;
import com.toulan.openIM.YWSDKGlobalConfigSample;
import com.toulan.utils.TConstant;
import com.toulan.utils.TCrash;
import com.toulan.utils.TSpeech;
import com.toulan91.R;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;

/**
 * 说明：TApplication
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/13 9:17
 * <p/>
 * 版本：verson 1.0
 */
public class TApplication extends Application{

    private static TApplication mApplication;
    private LocationService mLocationService;
    private SmsDao mSmsDao;

    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this);

        MultiDex.install(this);

        FileDownloader.init(getApplication());

        //初始化FastFrame
        FastFrame.init(this,true);

        //崩溃日志收集
        TCrash.getInstance().init();

        Bugly.init(getApplicationContext(),"841a83b540",false);

        //NoHttp
        NoHttp.initialize(this);
        Logger.setDebug(true);

//        百度语音
        TSpeech.init(this);

//        百度地图
        initLocation();

//        OpenIm
        initOpenIm();

    }

    public synchronized SmsDao getSmsDao(){
        if (mSmsDao == null){
            mSmsDao = new SmsDao(this);
        }
        return mSmsDao;
    }

    private void initOpenIm(){
        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(getApplicationContext())){
            return;
        }
        if (SysUtil.isMainProcess()){
            YWAPI.init(getApplication(),TConstant.OPENIM_APP_KEY);
        }
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_UI_POINTCUT, ConversationListUI.class);
        AdviceBinder.bindAdvice(PointCutEnum.CONTACTS_UI_POINTCUT, ContactsUiCustom.class);
        AdviceBinder.bindAdvice(PointCutEnum.CONVERSATION_FRAGMENT_OPERATION_POINTCUT, ConversationListOperationCustomSample.class);
        AdviceBinder.bindAdvice(PointCutEnum.CONTACTS_OP_POINTCUT, ContactsOperationCustomSample.class);
        //消息通知栏
        AdviceBinder.bindAdvice(PointCutEnum.NOTIFICATION_POINTCUT, NotificationInitSampleHelper.class);
        AdviceBinder.bindAdvice(PointCutEnum.CHATTING_FRAGMENT_POINTCUT, ChattingOperationCustomSample.class);
        AdviceBinder.bindAdvice(PointCutEnum.TRIBE_FRAGMENT_AT_MSG_DETAIL, SendAtMsgDetailUISample.class);
        AdviceBinder.bindAdvice(PointCutEnum.TRIBE_ACTIVITY_SELECT_AT_MEMBER, SelectTribeAtMemberSample.class);
        AdviceBinder.bindAdvice(PointCutEnum.YWSDK_GLOBAL_CONFIG_POINTCUT, YWSDKGlobalConfigSample.class);
    }

    private void initLocation() {
        if (mLocationService == null){
            mLocationService = new LocationService(getApplicationContext());
        }
    }

    public LocationService getLocationService(){
        initLocation();
        return mLocationService;
    }

    /**
     * 赋值
     *
     * @param application
     */
    private static void setApplication(TApplication application) {
        mApplication = application;
    }

    public static TApplication getApplication() {
        return mApplication;
    }

    public boolean isTest(){
        return "http://wap.91toulan.com/employee-erp/".equals(UIUtils.getString(R.string.url));
    }
}
