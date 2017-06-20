package com.toulan.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.fast.library.tools.ViewTools;
import com.fast.library.ui.ActivityStack;
import com.fast.library.ui.ContentView;
import com.fast.library.utils.StringUtils;
import com.fast.library.utils.UIUtils;
import com.pgyersdk.update.PgyUpdateManager;
import com.toulan.TApplication;
import com.toulan.bean.AppVersionInfoBean;
import com.toulan.bean.EmptyBean;
import com.toulan.model.OnModelListener;
import com.toulan.model.TDataManager;
import com.toulan.ui.CommonActivity;
import com.toulan.ui.dialog.AppUpdateDialog;
import com.toulan.ui.dialog.CommandDialog;
import com.toulan.ui.dialog.CommonDialog;
import com.toulan.utils.TRouter;
import com.toulan.utils.TUtils;
import com.toulan91.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 说明：登录
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2016/9/14 22:44
 * <p/>
 * 版本：verson 1.0
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends CommonActivity {

    @Bind(R.id.et_login_mobile)
    EditText etLoginMobile;

    public String mobile;
    AppUpdateDialog mAppUpdateDialog;
    AppUpdateDialog.AppUpdateListener mAppUpdateListener = new AppUpdateDialog.AppUpdateListener() {
        @Override
        public void install(String url) {
            TRouter.startUpdateService(LoginActivity.this,url);
        }

        @Override
        public void cancel() {

        }
    };

    private CommandDialog.OnComandListener commandDialogListener = new CommandDialog.OnComandListener() {
        @Override
        public void onCommand(CommonDialog dialog) {
            dialog.dismiss();
        }
    };

    private class MobileTextWatch implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            format(s.toString());
        }

        private void format(String mobile){
            if (!StringUtils.isEmpty(mobile)){
                String temp = TUtils.formatPhone(mobile);
                if (!mobile.equals(temp)){
                    etLoginMobile.setText(temp);
                    etLoginMobile.setSelection(temp.length());
                }
            }
        }
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
                            mAppUpdateDialog = new AppUpdateDialog(LoginActivity.this,mAppUpdateListener);
                        }
                        mAppUpdateDialog.setDownloadUrl(appVersionInfoBean.getDownload());
                        mAppUpdateDialog.show();
                    }
                }
            });
        }
    }

    @Override
    public void onInitStart() {
        etLoginMobile.addTextChangedListener(new MobileTextWatch());
        ActivityStack.create().finishOtherActivity(LoginActivity.class);
        checkUpdateApp();
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected void setToolbarTitle(TextView tvTitle) {
        ViewTools.setText(tvTitle, UIUtils.getString(R.string.app_name));
    }

    @OnClick(R.id.btn_login_next)
    public void onClick() {
        mobile = etLoginMobile.getText().toString().replace(" ","");
        if (checkMobile()){
            TDataManager.getInstance().getSms(mobile, new OnModelListener<ArrayList<EmptyBean>>(EmptyBean.class,true) {
                @Override
                public void onStart(int what) {
                    showLoading();
                }
                @Override
                public void onFinish(int what) {
                    dismissLoading();
                }
                @Override
                public void onFail(int what, int code, String msg) {
                    showCommandDialog(msg,UIUtils.getString(R.string.str_i_know),commandDialogListener);
                }
                @Override
                public void onSuccess(int what, ArrayList<EmptyBean> bean) {
//                    跳转输入验证码界面
                    Bundle bundle = new Bundle();
                    bundle.putString(LoginCodeActivity.MOBILE,mobile);
                    showActivity(LoginCodeActivity.class,bundle);
                }
            });
        }
    }

    private boolean checkMobile(){
        if (StringUtils.isEmpty(mobile)){
            return false;
        }else {
            if (!mobile.startsWith("1") || mobile.length() != 11){
                showCommandDialog(R.string.str_login_mobile_error,R.string.str_i_know,commandDialogListener);
                return false;
            }else {
                return true;
            }
        }
    }
}
