package com.toulan.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.fast.library.ui.ContentView;
import com.fast.library.utils.GsonUtils;
import com.fast.library.utils.LogUtils;
import com.fast.library.utils.NetUtils;
import com.fast.library.utils.UIUtils;
import com.toulan.bean.EmptyBean;
import com.toulan.bean.OrderBean;
import com.toulan.bean.UserBean;
import com.toulan.model.OnModelListener;
import com.toulan.model.TDataManager;
import com.toulan.ui.CommonActivity;
import com.toulan.ui.dialog.CancelSureDialog;
import com.toulan.ui.dialog.CommandDialog;
import com.toulan.ui.dialog.CommonDialog;
import com.toulan.ui.interfaces.OrderTimerFunction;
import com.toulan.utils.OrderSpeankUtils;
import com.toulan.utils.TConstant;
import com.toulan.utils.TUtils;
import com.toulan.utils.Tsp;
import com.toulan.utils.VibratorUtils;
import com.toulan.utils.WakeLockUtils;
import com.toulan.view.CountDownProgress;
import com.toulan91.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 说明：倒计时页面
 * <p/>
 * 作者：fanly
 * <p/>
 * 类型：Class
 * <p/>
 * 时间：2016/9/23 15:54
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.activity_order_timer)
public class OrderTimerActivity extends CommonActivity implements CountDownProgress.OnStatusChanged ,OrderTimerFunction{

    public static final String ORDER_BEAN = "ORDER_BEAN";

    @Bind(R.id.cdp_timer)
    CountDownProgress cdpTimer;
    @Bind(R.id.tv_ordertimer_left)
    TextView tvLeft;
    @Bind(R.id.tv_ordertimer_right)
    TextView tvRight;
    @Bind(R.id.tv_ordertimer_center)
    TextView tvCenter;
    @Bind(R.id.iv_ordertimer_left)
    ImageView ivLeft;
    @Bind(R.id.iv_ordertimer_right)
    ImageView ivRight;
    @Bind(R.id.iv_ordertimer_center)
    ImageView ivCenter;
    @Bind(R.id.tv_order_timer_time)
    TextView tvOrderTime;
    @Bind(R.id.tv_order_timer_id)
    TextView tvOrderId;

    @Bind(R.id.rl_ordertimer_center)
    RelativeLayout rlOrderCenter;

    UserBean mUserBean;
    OrderBean mOrderBean;
    WakeLockUtils wakeLockUtils;

    private static final String STR_START = "开始";
    private static final String STR_EXIT = "退出";
    private static final String STR_ADD_TIME = "加时";
    private static final String STR_FINISH = "服务完成";
    private static final String STR_CALL = "联系客户";
    private static final String STR_PASUE = "暂停";

    private static final String STR_TIMER_STATUS_NORMAL = "正常";
    private static final String STR_TIMER_STATUS_ADD_TIME = "加时中";
    private static final String STR_TIMER_STATUS_PAUSE = "暂停";
    private static final String STR_TIMER_STATUS_FINISH = "完成";

    private static final int STR_START_COLOR = Color.parseColor("#50BF99");
    private static final int STR_EXIT_COLOR = Color.parseColor("#FE918F");
    private static final int STR_ADD_TIME_COLOR = Color.parseColor("#5d8ef0");
    private static final int STR_FINISH_COLOR = Color.parseColor("#00d0f3");
    private static final int STR_CALL_COLOR = Color.parseColor("#f97f51");
    private static final int STR_PASUE_COLOR = Color.parseColor("#FE918F");

    private static final int ICON_START = R.drawable.order_timer_start;
    private static final int ICON_EXIT = R.drawable.order_timer_exit;
    private static final int ICON_ADD_TIME = R.drawable.order_timer_add;
    private static final int ICON_FINISH = R.drawable.order_timer_finish;
    private static final int ICON_CALL = R.drawable.order_timer_call;
    private static final int ICON_PASUE = R.drawable.order_timer_pause;

    private boolean isAddTime = false;
    private static final int MIN_30 = 1800;//30分钟
    private static final int MAX_ADD_TIME = 28800;//8小时

    CancelSureDialog mCancelSureDialog;
    SimpleDateFormat formatter = new SimpleDateFormat("HH小时mm分钟");
    SimpleDateFormat downFormatter = new SimpleDateFormat("HH:mm");

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        mUserBean = Tsp.getUserInfo();
        mOrderBean = GsonUtils.toBean(intent.getStringExtra(ORDER_BEAN), OrderBean.class);
    }

    @Override
    public void onInitStart() {
        wakeLockUtils = new WakeLockUtils();
        wakeLockUtils.acquirWakeLock(this,this.getClass().getSimpleName());

        cdpTimer.setOnStatusChangedListener(this);
        tvOrderId.setText("订单号："+mOrderBean.getOrderCountdown().getOid());
        tvOrderTime.setText("服务时间："+ TUtils.convertCountDownTime(mOrderBean.getOrderCountdown().getCountdown_time()) + "/人");

        cdpTimer.initTimer(mOrderBean.getOrderCountdown().getCountdown_time(),downFormatter);
        setIconStatus(tvLeft,ivLeft,STR_START,ICON_START,STR_START_COLOR);//开始
        setIconStatus(tvRight,ivRight,STR_EXIT,ICON_EXIT,STR_EXIT_COLOR);//退出

//        setIconStatus(tvCenter,ivCenter,STR_FINISH,ICON_FINISH,STR_FINISH_COLOR);//服务完成
//        setIconStatus(tvRight,ivRight,STR_CALL,ICON_CALL,STR_CALL_COLOR);//联系客户
//        setIconStatus(tvLeft,ivLeft,STR_PASUE,ICON_PASUE,STR_PASUE_COLOR);//暂停
//        setIconStatus(tvLeft,ivLeft,STR_ADD_TIME,ICON_ADD_TIME,STR_ADD_TIME_COLOR);//加时
    }

    public void changeTimerCloseDesc(String text){
        cdpTimer.setStateDesc(text);
    }

    private void setIconStatus(TextView tv,ImageView iv,String text,int icon,int textColor){
        try {
            if (tv != null && iv != null){
                tv.setText(text);
                tv.setTextColor(textColor);
                iv.setImageResource(icon);
                iv.setTag(text);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected void setToolbarTitle(TextView tvTitle) {
        String title = String.format(UIUtils.getString(R.string.str_main_title), mUserBean.getUserNo(), mUserBean.getName());
        ViewTools.setText(tvTitle, title);
    }

    @OnClick({R.id.iv_ordertimer_left,R.id.iv_ordertimer_center,R.id.iv_ordertimer_right})
    public void onClick(View view) {
        String tag = (String) view.getTag();
        switch (view.getId()){
            case R.id.iv_ordertimer_left:
                if (STR_START.equals(tag)){
                    if (!rlOrderCenter.isShown()){
                        checkStartCondition();
                    }else {
                        clickStartTimer();
                    }
                }else if (STR_ADD_TIME.equals(tag)){
                    OrderSpeankUtils.getInstance().speak(String.format(TConstant.OrderTimerSpeech.ADD_TIME_SPEECH,mUserBean.getUserNo()));
//                    加时
                    showSureDialog(R.string.str_order_timer_add_time_tip, R.string.str_sure_add_time,R.string.str_cancel_add_time, new CancelSureDialog.OnCancelSureListener() {
                        @Override
                        public void onCancel() {
                            mCancelSureDialog.dismiss();
                        }

                        @Override
                        public void onSure() {
                            clickAddTime();
                            mCancelSureDialog.dismiss();
                        }
                    });
                }else if (STR_PASUE.equals(tag)){
//                    暂停
                    clickTimerPause();
                }
                break;
            case R.id.iv_ordertimer_center:
                if (STR_FINISH.equals(tag)){
//                    服务完成
                    clickServerFinish();
                }
                break;
            case R.id.iv_ordertimer_right:
                if (STR_EXIT.equals(tag)){
//                    退出
                    clickExit();
                }else if (STR_CALL.equals(tag)){
//                    联系客户
                    clickCallCustom();
                }
                break;
        }
    }

    private String formatter(int second){
        return formatter.format(second*1000- TimeZone.getDefault().getRawOffset());
    }

    private void checkStartCondition(){
//        已执行过倒计时操作
        if (mOrderBean.getOrderFinish().getWorkingTime() > 0 || Tsp.getOrderTimerServerTime(mOrderBean.getOrderDetail().getOid()) > 0){
            showCommandDialog(R.string.str_order_timer_has_take, R.string.str_i_know, new CommandDialog.OnComandListener() {
                @Override
                public void onCommand(CommonDialog dialog) {
                    finish();
                }
            });
        }else if (TUtils.orderTimerOverTime(mOrderBean)){
//            订单超时
            showCommandDialog(R.string.str_order_timer_over_time, R.string.str_i_know, new CommandDialog.OnComandListener() {
                @Override
                public void onCommand(CommonDialog dialog) {
                    finish();
                }
            });
        }else if (TUtils.orderTimerComeSoon(mOrderBean)){
//            来的太早
            showCommandDialog(R.string.str_order_timer_come_fast, R.string.str_i_know, new CommandDialog.OnComandListener() {
                @Override
                public void onCommand(CommonDialog dialog) {
                    dialog.dismiss();
                }
            });
        }else {
            //确认开工
            showSureDialog(R.string.str_order_timer_start_work_tip, R.string.str_sure_start_work, R.string.str_cancel_start_work, new CancelSureDialog.OnCancelSureListener() {
                @Override
                public void onCancel() {
                    mCancelSureDialog.dismiss();
                }

                @Override
                public void onSure() {
                    clickStartTimer();
                    mCancelSureDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onTimerStart() {
    }

    @Override
    public void onTimerPause() {
    }

    @Override
    public void onTimerStop() {
        setIconStatus(tvCenter,ivCenter,STR_FINISH,ICON_FINISH,STR_FINISH_COLOR);//服务完成
        setIconStatus(tvRight,ivRight,STR_CALL,ICON_CALL,STR_CALL_COLOR);//联系客户
        setIconStatus(tvLeft,ivLeft,STR_ADD_TIME,ICON_ADD_TIME,STR_ADD_TIME_COLOR);//加时

        changeTimerCloseDesc(STR_TIMER_STATUS_FINISH);

        if (isAddTime){
            ivLeft.setEnabled(false);
            OrderSpeankUtils.getInstance().speak(String.format(TConstant.OrderTimerSpeech.CONFIRM_SPEECH,mUserBean.getUserNo(),formatter(cdpTimer.getAllTime())));
            clickServerFinish();
        }else {
            OrderSpeankUtils.getInstance().speak(String.format(TConstant.OrderTimerSpeech.STOP_SPEECH,mUserBean.getUserNo()));
        }
    }

    @Override
    public void onTimerProgress(int currentTime) {
        LogUtils.e("fanly",currentTime+"秒");
        if (MIN_30 == currentTime && !isAddTime){
            VibratorUtils.vibrate(this,TConstant.ORDER_TIMER_30_MIN_VIB_TIME);
            OrderSpeankUtils.getInstance().speak(String.format(TConstant.OrderTimerSpeech.MIN_30_SPEECH,mUserBean.getUserNo()));
        }
    }

    @Override
    public void clickStartTimer() {
        if (!rlOrderCenter.isShown()){
//            第一次开始倒计时
            OrderSpeankUtils.getInstance().speak(String.format(TConstant.OrderTimerSpeech.START_SPEECH,mUserBean.getUserNo()));
        }else {
            OrderSpeankUtils.getInstance().speak(String.format(TConstant.OrderTimerSpeech.PAUSE_TO_START_SPEECH,mUserBean.getUserNo()));
        }

        ViewTools.VISIBLE(rlOrderCenter);
        setIconStatus(tvCenter,ivCenter,STR_FINISH,ICON_FINISH,STR_FINISH_COLOR);//服务完成
        setIconStatus(tvRight,ivRight,STR_CALL,ICON_CALL,STR_CALL_COLOR);//联系客户
        setIconStatus(tvLeft,ivLeft,STR_PASUE,ICON_PASUE,STR_PASUE_COLOR);//暂停

        changeTimerCloseDesc(isAddTime ? STR_TIMER_STATUS_ADD_TIME :STR_TIMER_STATUS_NORMAL);

        cdpTimer.start();
    }

    @Override
    public void clickExit() {
        finish();
    }

    @Override
    public void clickAddTime() {
        isAddTime = true;
        cdpTimer.addTimer(MAX_ADD_TIME);

        setIconStatus(tvCenter,ivCenter,STR_FINISH,ICON_FINISH,STR_FINISH_COLOR);//服务完成
        setIconStatus(tvRight,ivRight,STR_CALL,ICON_CALL,STR_CALL_COLOR);//联系客户
        setIconStatus(tvLeft,ivLeft,STR_PASUE,ICON_PASUE,STR_PASUE_COLOR);//暂停

        OrderSpeankUtils.getInstance().speak(String.format(TConstant.OrderTimerSpeech.CLICK_ADD_TIME_SPEECH,mUserBean.getUserNo()));

        changeTimerCloseDesc(STR_TIMER_STATUS_ADD_TIME);
        cdpTimer.start();
    }

    @Override
    public void clickServerFinish() {
        cdpTimer.pause();
        showSureDialog(String.format(UIUtils.getString(R.string.str_order_timer_server_time_tip),formatter(cdpTimer.getCountDownTime()),formatter(cdpTimer.getAddTime()),formatter(cdpTimer.getAllTime())), UIUtils.getString(R.string.str_sure_finish), UIUtils.getString(R.string.str_cancel), new CancelSureDialog.OnCancelSureListener() {
            @Override
            public void onCancel() {
                mCancelSureDialog.dismiss();
                cdpTimer.start();
            }

            @Override
            public void onSure() {
                submit();
                mCancelSureDialog.dismiss();
            }
        });
    }

    private void submit(){
        Tsp.saveOrderTimerServerTime(mOrderBean.getOrderDetail().getOid(),cdpTimer.getCountDownTime());
        Tsp.saveOrderTimerAddTime(mOrderBean.getOrderDetail().getOid(),cdpTimer.getAddTime());
        finish();
    }

    @Override
    public void clickCallCustom() {
        if (NetUtils.isNetConnected()){
            showSureDialog("确定打电话给客户吗？", "确定", "取消", new CancelSureDialog.OnCancelSureListener() {
                @Override
                public void onCancel() {
                    mCancelSureDialog.dismiss();
                }

                @Override
                public void onSure() {
                    TDataManager.getInstance().callPhone(mOrderBean.getOrderDetail().getOid(), new OnModelListener<ArrayList<EmptyBean>>(EmptyBean.class,true) {
                        @Override
                        public void onFail(int what, int code, String msg) {
                            shortToast(msg);
                        }

                        @Override
                        public void onSuccess(int what, ArrayList<EmptyBean> been) {
                            shortToast(R.string.call_success);
                        }
                    });
                }
            });
        }else {
            shortToast(R.string.network_error);
        }
    }

    @Override
    public void clickTimerPause() {
        OrderSpeankUtils.getInstance().speak(String.format(TConstant.OrderTimerSpeech.PAUSE_SPEECH,mUserBean.getUserNo()));

        setIconStatus(tvCenter,ivCenter,STR_FINISH,ICON_FINISH,STR_FINISH_COLOR);//服务完成
        setIconStatus(tvRight,ivRight,STR_CALL,ICON_CALL,STR_CALL_COLOR);//联系客户
        setIconStatus(tvLeft,ivLeft,STR_START,ICON_START,STR_START_COLOR);//开始

        changeTimerCloseDesc(STR_TIMER_STATUS_PAUSE);

        cdpTimer.pause();
    }

    private void showSureDialog(int content, int sure, int cancel, CancelSureDialog.OnCancelSureListener listener){
        if (mCancelSureDialog == null){
            mCancelSureDialog = new CancelSureDialog(this);
        }
        mCancelSureDialog.setCancelText(UIUtils.getString(cancel))
                .setSureText(UIUtils.getString(sure))
                .setContentText(UIUtils.getString(content))
                .setCancelSureListener(listener)
                .show();
    }

    private void showSureDialog(String content, String sure, String cancel, CancelSureDialog.OnCancelSureListener listener){
        if (mCancelSureDialog == null){
            mCancelSureDialog = new CancelSureDialog(this);
        }
        mCancelSureDialog.setCancelText(cancel)
                .setSureText(sure)
                .setContentText(content)
                .setCancelSureListener(listener)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeLockUtils.releaseWakeLock();
    }
}
