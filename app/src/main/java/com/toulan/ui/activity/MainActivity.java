package com.toulan.ui.activity;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.fast.library.Adapter.viewpager.ViewPagerAdapter;
import com.fast.library.bean.ViewPageInfo;
import com.fast.library.tools.BackExit;
import com.fast.library.tools.BackTools;
import com.fast.library.tools.EventCenter;
import com.fast.library.ui.ActivityStack;
import com.fast.library.ui.ContentView;
import com.fast.library.utils.DateUtils;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.NetUtils;
import com.fast.library.utils.NumberUtils;
import com.fast.library.utils.StringUtils;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.toulan.TApplication;
import com.toulan.alarm.AlarmManagerUtils;
import com.toulan.bean.AppVersionInfoBean;
import com.toulan.bean.CallLogBean;
import com.toulan.bean.SmsInfoBean;
import com.toulan.bean.TabEntity;
import com.toulan.database.CallDao;
import com.toulan.database.SmsDao;
import com.toulan.helper.CallPhoneHelper;
import com.toulan.helper.LocationHelper;
import com.toulan.helper.SmsHelper;
import com.toulan.helper.WriterHelper;
import com.toulan.model.OnModelListener;
import com.toulan.model.TDataManager;
import com.toulan.openIM.UserProfileSampleHelper;
import com.toulan.ui.CommonActivity;
import com.toulan.ui.dialog.AppUpdateDialog;
import com.toulan.ui.fragment.JobFragment;
import com.toulan.ui.fragment.MessageFragment;
import com.toulan.ui.fragment.MoreFragment;
import com.toulan.utils.OpenImUtils;
import com.toulan.utils.OrderSpeankUtils;
import com.toulan.utils.ServerTimeUtils;
import com.toulan.utils.TConstant;
import com.toulan.utils.TRouter;
import com.toulan.utils.TUtils;
import com.toulan.utils.Tsp;
import com.toulan.view.DisableViewPager;
import com.toulan91.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;

@ContentView(R.layout.activity_main)
public class MainActivity extends CommonActivity implements OnTabSelectListener {

    @Bind(R.id.main_tablayout)
    CommonTabLayout mainTablayout;
    @Bind(R.id.main_fragment_viewpager)
    DisableViewPager mainFragmentViewpager;
    AppUpdateDialog mAppUpdateDialog;
    private CallDao mCallDao;
    private SmsDao mSmsDao;

    AppUpdateDialog.AppUpdateListener mAppUpdateListener = new AppUpdateDialog.AppUpdateListener() {
        @Override
        public void install(String url) {
            TRouter.startUpdateService(MainActivity.this,url);
        }

        @Override
        public void cancel() {

        }
    };

    private ViewPagerAdapter mFragmentAdapter;
    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IYWConversationService mConversationService;
    private YWIMKit mIMKit;

    private static int[] mIconSelectIds = {
            R.drawable.main_job_selected, R.drawable.main_message_selected,
            R.drawable.main_more_selected};

    private static int[] mIconUnselectIds= {
            R.drawable.main_job_normal, R.drawable.main_message_normal,
            R.drawable.main_more_normal};

    public static ArrayList<ViewPageInfo> createFragment() {
        ArrayList<ViewPageInfo> list = new ArrayList<>(3);
        list.add(0, new ViewPageInfo("",JobFragment.class,null));
        list.add(1, new ViewPageInfo("",MessageFragment.class,null));
        list.add(2, new ViewPageInfo("",MoreFragment.class,null));
        return list;
    }

    private void initConversationServiceAndListener() {
        if (mIMKit == null){
            return;
        }
        mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {
            //当未读数发生变化时会回调该方法，开发者可以在该方法中更新未读数
            @Override
            public void onUnreadChange() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final YWIMKit imKit = OpenImUtils.getImKit();
                        mConversationService = imKit.getConversationService();
                        //获取当前登录用户的所有未读数
                        int unReadCount = mConversationService.getAllUnreadCount();
                        //设置桌面角标的未读数
                        mIMKit.setShortcutBadger(unReadCount);
                        EventBus.getDefault().post(new EventCenter<Integer>(TConstant.EventType.mainShowMessageNumber,unReadCount));
                    }
                });
            }
        };
        if (mConversationService == null){
            mConversationService = OpenImUtils.getImKit().getConversationService();
            mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
        }
    }

    @Override
    public void onInitStart() {
        mIMKit = OpenImUtils.getImKit();

        mFragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this, createFragment());
        mainFragmentViewpager.setAdapter(mFragmentAdapter);
        mainFragmentViewpager.setOffscreenPageLimit(mFragmentAdapter.getCount());

        mainTablayout.setTabData(createTab());
        mainTablayout.setOnTabSelectListener(this);
//        默认选中首页
        onTabSelect(0);
//        初始化语音
        OrderSpeankUtils.getInstance().init();

        if (Tsp.isLogin()){
            WriterHelper.writeLocationLog("位置信息Main");
            LocationHelper.submitLocation(this);
        }
        if (ActivityStack.create().getCount() > 1){
            ActivityStack.create().finishOtherActivity(MainActivity.class);
        }
        UserProfileSampleHelper.initProfileCallback();
        initConversationServiceAndListener();
        checkUpdateApp();
//        上传通话记录
        getLastAllCallPhone();
//        上传短信记录
        getLastAllSmsPhone();
//        开启鹰眼服务
        TRouter.startTraceService(this);
    }

    private void checkUpdateApp(){
        if (TApplication.getApplication().isTest()){
//            测试环境用蒲公英检查更新
            PgyUpdateManager.register(this);
        }else {
            TDataManager.getInstance().checkUpdateApp(new OnModelListener<AppVersionInfoBean>(AppVersionInfoBean.class) {

                @Override
                public void onFail(int what, int code, String msg) {
                }

                @Override
                public void onSuccess(int what, AppVersionInfoBean appVersionInfoBean) {
                    if (appVersionInfoBean != null && appVersionInfoBean.isUpdate()){
                        if (mAppUpdateDialog == null){
                            mAppUpdateDialog = new AppUpdateDialog(MainActivity.this,mAppUpdateListener);
                        }
                        mAppUpdateDialog.setDownloadUrl(appVersionInfoBean.getDownload());
                        mAppUpdateDialog.show();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetUtils.initNetworkChanggedListener(this);
        //resume时需要检查全局未读消息数并做处理，因为离开此界面时删除了全局消息监听器
        if (mConversationUnreadChangeListener != null){
            mConversationUnreadChangeListener.onUnreadChange();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        NetUtils.clearNetworkChangedListener(this);
    }

    @Override
    public void onBackPressed() {
        BackTools.onBackPressed(new BackExit() {
            @Override
            public void showTips() {
                shortToast("再按一次退出!");
            }

            @Override
            public void exit() {
                finish();
            }
        });
    }

    public static ArrayList<CustomTabEntity> createTab(){
        String []titles = new String[]{"工作","内聊","更多"};
        ArrayList<CustomTabEntity> list = new ArrayList<>(titles.length);
        for (int i = 0;i < titles.length;i++){
            list.add(new TabEntity(titles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        return list;
    }

    @Override
    public void onTabSelect(int position) {
        mainTablayout.setCurrentTab(position);
        mainFragmentViewpager.setCurrentItem(position,false);
        if (position == 1){
            EventBus.getDefault().post(new EventCenter<Integer>(TConstant.EventType.mainShowMessageNumber,0));
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void registerService() {
        super.registerService();
        int []time = TUtils.getStartLocationTime();
        AlarmManagerUtils.cancelAlarm(this, TConstant.Postion.POSITION_ID);
        AlarmManagerUtils.setAlarm(this,time[0],time[1],TConstant.Postion.POSITION_ID,TConstant.Postion.LOCATION_TIME_SPAN);
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showMessageNumber(EventCenter<Integer> eventCenter){
        if (TConstant.EventType.mainShowMessageNumber.equals(eventCenter.type)){
            if (eventCenter.data <= 0){
                mainTablayout.hideMsg(1);
            }else {
                mainTablayout.showMsg(1,eventCenter.data);
            }
        }
    }

    /**
     * 获取所有的短信记录并上传
     */
    private void getLastAllSmsPhone(){
        if (mSmsDao == null){
            mSmsDao = new SmsDao(this);
        }
        SmsHelper.OneLastSmsListener onLastSmsListener = new SmsHelper.OneLastSmsListener() {
            @Override
            public void smsInfo(SmsInfoBean bean) {
                String time = ServerTimeUtils.getInstance().getTime(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_1);
                if (bean != null){
                    time = bean.getCreateTime();
                }
                Tsp.sp.write(Tsp.KEY.lastSmsRecord,time);
            }
        };
//        如果上次没有记录最后一次短信时间，就查找最近一条短信记录，保存时间
        String lastRecord = Tsp.sp.readString(Tsp.KEY.lastPhoneRecord);
        if (StringUtils.isEmpty(lastRecord)){
            SmsHelper.findOneLastSms(this, onLastSmsListener);
        }else {
//            如果上次记录有时间，就查询这次时间之后的所有通过记录，保存到数据库，并且记录最后一条数据
            SmsHelper.findAllSms(mSmsDao,this, NumberUtils.toLong(lastRecord), new SmsHelper.OneLastSmsListener() {
                @Override
                public void smsInfo(SmsInfoBean bean) {
                    if (bean != null){
                        Tsp.sp.write(Tsp.KEY.lastPhoneRecord,bean.getCreateTime());
                    }
                    CallPhoneHelper.uploadSms(MainActivity.this);
                }
            });
        }
    }

    /**
     * 获取所有的通话记录并上传
     */
    private void getLastAllCallPhone(){
        if (mCallDao == null){
            mCallDao = new CallDao(this);
        }
        CallPhoneHelper.OnLastCallListener onLastCallListener = new CallPhoneHelper.OnLastCallListener() {
            @Override
            public void callInfo(CallLogBean bean) {
                String time = ServerTimeUtils.getInstance().getTime(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_1);
                if (bean != null){
                    time = bean.getCallTime();
                }
                Tsp.sp.write(Tsp.KEY.lastPhoneRecord,time);
            }
        };
//        如果上次没有记录最后一次通化时间，就查找最近一条通化记录，保存时间
        String lastRecord = Tsp.sp.readString(Tsp.KEY.lastPhoneRecord);
        if (StringUtils.isEmpty(lastRecord)){
            CallPhoneHelper.selectCallPhoneTopOne(this, onLastCallListener);
        }else {
//            如果上次记录有时间，就查询这次时间之后的所有通过记录，保存到数据库，并且记录最后一条数据
            CallPhoneHelper.selectAllCallPhone(mCallDao,this, lastRecord, new CallPhoneHelper.OnLastCallListener() {
                @Override
                public void callInfo(CallLogBean bean) {
                    if (bean != null){
                        Tsp.sp.write(Tsp.KEY.lastPhoneRecord,bean.getCallTime());
                    }
                    CallPhoneHelper.uploadCall(MainActivity.this);
                }
            });
        }
    }
}
