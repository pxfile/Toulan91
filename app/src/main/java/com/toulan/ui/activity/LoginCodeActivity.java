package com.toulan.ui.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.fast.library.ui.ActivityStack;
import com.fast.library.ui.ContentView;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;
import com.jungly.gridpasswordview.GridPasswordView;
import com.toulan.bean.EmptyBean;
import com.toulan.bean.UserBean;
import com.toulan.model.OnModelListener;
import com.toulan.model.TDataManager;
import com.toulan.receiver.SmsReceiver;
import com.toulan.ui.CommonActivity;
import com.toulan.ui.dialog.CommandDialog;
import com.toulan.ui.dialog.CommonDialog;
import com.toulan.utils.OpenImUtils;
import com.toulan.utils.TUtils;
import com.toulan.utils.Tsp;
import com.toulan91.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 说明：登录验证码
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/17 13:47
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.activity_login_code)
public class LoginCodeActivity extends CommonActivity {

    @Bind(R.id.tv_login_code_mobile)
    TextView tvLoginCodeMobile;
    @Bind(R.id.gpv_login_code)
    GridPasswordView gpvLoginCode;
    @Bind(R.id.tv_login_code_resend)
    TextView tvLoginCodeResend;
    @Bind(R.id.btn_login_next)
    Button btnLoginNext;

    public static final String MOBILE = "mobile";
    public static final int RESEND_WAIT = 60 * 1000;
    private String myMobile;
    private CodeDownTimer codeDownTimer;

//    private SmsReceiver mSmsReceiver;

    private class CodeDownTimer extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CodeDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            if (tvLoginCodeResend != null){
                tvLoginCodeResend.setEnabled(false);
            }
        }

        @Override
        public void onTick(long millisUntilFinished) {
            int time = (int) millisUntilFinished / 1000;
            String text = String.format(UIUtils.getString(R.string.str_login_code_resend), String.valueOf(time));
            ViewTools.setText(tvLoginCodeResend, text);
        }

        @Override
        public void onFinish() {
            if (tvLoginCodeResend != null){
                tvLoginCodeResend.setEnabled(true);
                String text = String.format(UIUtils.getString(R.string.str_login_code_resend), String.valueOf(60));
                ViewTools.setText(tvLoginCodeResend, text);
            }

        }
    }

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        myMobile = intent.getStringExtra(MOBILE);
    }

    @Override
    public void onInitStart() {
        codeDownTimer = new CodeDownTimer(RESEND_WAIT, 1000);
        ViewTools.setText(tvLoginCodeMobile, TUtils.formatPhone(myMobile));
        gpvLoginCode.togglePasswordVisibility();
        gpvLoginCode.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                if (StringUtils.isEmpty(psw) || psw.length() != 4) {
                    btnLoginNext.setEnabled(false);
                } else {
                    btnLoginNext.setEnabled(true);
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
        codeDownTimer.start();
    }

    @Override
    public void registerBroadcast() {
        super.registerBroadcast();
//        mSmsReceiver = new SmsReceiver(new SmsReceiver.OnSmsListener() {
//            @Override
//            public void getSms(String code) {
//                gpvLoginCode.setPassword(code);
//                btnLoginNext.setEnabled(true);
//            }
//        });
//        //实例化过滤器并设置要过滤的广播
//        IntentFilter intentFilter = new IntentFilter(SmsReceiver.SMS_RECEIVED_ACTION);
//        intentFilter.setPriority(1000);
////        注册广播
//        registerReceiver(mSmsReceiver, intentFilter);
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected void setToolbarTitle(TextView tvTitle) {
        ViewTools.setText(tvTitle, UIUtils.getString(R.string.app_name));
    }

    @OnClick({R.id.tv_login_code_resend, R.id.btn_login_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login_code_resend:
                TDataManager.getInstance().getSms(myMobile, new OnModelListener<ArrayList<EmptyBean>>(EmptyBean.class,true) {
                    @Override
                    public void onStart(int what) {
                        showLoading();
                    }

                    @Override
                    public void onFinish(int what) {
                        dismissLoading();
                    }

                    @Override
                    public void onFail(int whta, int code, String msg) {
                        showCommandDialog(msg, UIUtils.getString(R.string.str_i_know), new CommandDialog.OnComandListener() {
                            @Override
                            public void onCommand(CommonDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(int what, ArrayList<EmptyBean> bean) {
                    }
                });
                break;
            case R.id.btn_login_next:
                TDataManager.getInstance().login(myMobile, gpvLoginCode.getPassWord(), new OnModelListener<UserBean>(UserBean.class) {

                    @Override
                    public void onStart(int what) {
                        showLoading();
                    }

                    @Override
                    public void onFinish(int what) {
                    }

                    @Override
                    public void onFail(int what, int code, String msg) {
                        dismissLoading();
                        showCommandDialog(msg, UIUtils.getString(R.string.str_i_know), new CommandDialog.OnComandListener() {
                            @Override
                            public void onCommand(CommonDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onSuccess(int what, UserBean bean) {
                        Tsp.saveUserInfo(bean);
                        Tsp.setMyPhoneNumber(myMobile);
                        connectOpenIm(bean);
                    }
                });
                break;
        }
    }

    private void connectOpenIm(UserBean bean){
        OpenImUtils.login(bean.getImUserId(), bean.getImPwd(), new OpenImUtils.OnLoginListener() {
            @Override
            public void loginSuccess() {
                dismissLoading();
                toMain();
            }

            @Override
            public void loginError(String s) {
                dismissLoading();
                shortToast(s);
            }
        });
    }

    @Override
    protected void onDestroy() {
//        if (mSmsReceiver != null){
//            unregisterReceiver(mSmsReceiver);
//        }
        super.onDestroy();
    }

    private void toMain(){
        skipActivity(MainActivity.class);
    }

}
